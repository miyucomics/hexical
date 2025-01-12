package miyucomics.hexical.blocks

import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity.RemovalReason
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World
import java.util.*
import kotlin.math.abs
import kotlin.math.min

class PedestalBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(HexicalBlocks.PEDESTAL_BLOCK_ENTITY, pos, state),
	Inventory {
	private var storedItem: ItemStack = ItemStack.EMPTY
	private var itemEnt: ItemEntity? = null
	private var persistentUUID: UUID? = null
	private val normal: Vec3i = cachedState.get(PedestalBlock.FACING).vector

	private fun generateUniqueUUID(): UUID {
		val world = getWorld()
		if (world !is ServerWorld)
			return UUID.randomUUID()

		var newUUID = UUID.randomUUID()
		while (world.getEntity(newUUID) != null)
			newUUID = UUID.randomUUID()
		return newUUID
	}


	private fun makeNewItemEntity() {
		val world = getWorld()
		if (world is ServerWorld) {
			if (storedItem.isEmpty) {
				return
			}
			if (persistentUUID == null) {
				persistentUUID = generateUniqueUUID()
			}
			// first need to deal with old one -- maybe
			if (itemEnt != null) {
				itemEnt!!.discard()
				itemEnt = null
			}
			val heightOffset = HEIGHT - 0.5 + 0.01
			val xPos = pos.x + 0.5 + (heightOffset + 0.2f) * normal.x
			val yPos =
				pos.y + 0.2 + (heightOffset * normal.y) + abs(0.3 * normal.y) + (if (normal.y < 0) -0.7 else 0.0)
			val zPos = pos.z + 0.5 + (heightOffset + 0.2f) * normal.z
			var needsToSpawn = false
			val maybeItemEnt = world.getEntity(persistentUUID)
			if (maybeItemEnt is ItemEntity) {
				itemEnt = maybeItemEnt
				itemEnt!!.stack = storedItem
			} else {
				needsToSpawn = true
				itemEnt = ItemEntity(
					world,
					xPos, yPos, zPos,
					storedItem, 0.0, 0.0, 0.0
				)
			}
			itemEnt!!.setPos(xPos, yPos, zPos)
			itemEnt!!.uuid = persistentUUID
			itemEnt!!.setNoGravity(true)
			itemEnt!!.noClip = true
			itemEnt!!.setPickupDelayInfinite()
			itemEnt!!.setNeverDespawn()
			itemEnt!!.isInvulnerable = true
			if (needsToSpawn) {
				world.spawnEntity(itemEnt)
			}
			markDirty()
		}
	}

	fun onRemoved() {
		if (itemEnt != null)
			itemEnt!!.discard()
		if (world is ServerWorld) {
			val heightOffset = HEIGHT - 0.5 + 0.01
			itemEnt = ItemEntity(world, pos.x + 0.5 + heightOffset * normal.x, pos.y + 0.5 + heightOffset * normal.y, pos.z + 0.5 + heightOffset * normal.z, storedItem, 0.0, 0.0, 0.0)
			(world as ServerWorld).spawnEntity(itemEnt)
			markDirty()
		}
	}

	private val interactedCheck: MutableSet<PlayerEntity> = HashSet()

	init {
		if (getWorld() != null && !getWorld()!!.isClient()) {
			persistentUUID = generateUniqueUUID()
			makeNewItemEntity()
			markDirty()
		}
	}

	fun use(player: PlayerEntity, hand: Hand, hit: BlockHitResult?): ActionResult {
		val heldStack = player.getStackInHand(hand)
		val otherStack = player.getStackInHand(if (hand == Hand.MAIN_HAND) Hand.OFF_HAND else Hand.MAIN_HAND)
		if (storedItem.isEmpty) {
			if (world!!.isClient) {
				return if (heldStack.isEmpty) ActionResult.PASS else ActionResult.SUCCESS
			}
			if (heldStack.isEmpty) {
				return ActionResult.PASS
			} else {
				setStack(0, heldStack.copy())
				heldStack.decrement(heldStack.count)
				return ActionResult.SUCCESS
			}
		}
		if (ItemStack.canCombine(storedItem, heldStack)) {
			if (!world!!.isClient) {
				val amtToMove =
					min((storedItem.maxCount - storedItem.count).toDouble(), heldStack.count.toDouble()).toInt()
				storedItem.increment(amtToMove)
				heldStack.decrement(amtToMove)
				syncItemWithEntity(true)
			}
			return ActionResult.SUCCESS
		}
		if (ItemStack.canCombine(storedItem, otherStack)) {
			return ActionResult.PASS
		}
		if (!world!!.isClient) {
			val returnedStack = removeStack(0)
			if (hand == Hand.MAIN_HAND) {
				setStack(0, heldStack.copy())
				heldStack.decrement(heldStack.count)
			}
			if (!returnedStack.isEmpty) {
				if (player.mainHandStack.isEmpty) {
					player.setStackInHand(Hand.MAIN_HAND, returnedStack)
				} else {
					player.inventory.offerOrDrop(returnedStack)
				}
			}
		}

		return ActionResult.SUCCESS
	}

	override fun readNbt(nbt: NbtCompound) {
		super.readNbt(nbt)
		this.storedItem = ItemStack.fromNbt(nbt.getCompound(ITEM_DATA_TAG))
		if (nbt.containsUuid(PERSISTENT_UUID_TAG)) this.persistentUUID = nbt.getUuid(PERSISTENT_UUID_TAG)
	}

	override fun writeNbt(nbt: NbtCompound) {
		super.writeNbt(nbt)
		nbt.put(ITEM_DATA_TAG, storedItem.writeNbt(NbtCompound()))
		if (persistentUUID != null) {
			nbt.putUuid(PERSISTENT_UUID_TAG, persistentUUID)
		}
	}

	private fun syncItemWithEntity(forceBlock: Boolean) {
		if (world!!.isClient()) return
		if (storedItem.isEmpty) {
			if (itemEnt != null) {
				itemEnt!!.stack = storedItem
				itemEnt = null
				markDirty()
			}
			return
		} else {
			if (itemEnt == null || itemEnt!!.isRemoved) {
				if (itemEnt != null &&
					(itemEnt!!.removalReason == RemovalReason.DISCARDED ||
							(itemEnt!!.removalReason == RemovalReason.KILLED &&
									(itemEnt!!.stack == null || itemEnt!!.stack.isEmpty)
									))
					&& !forceBlock
				) {
					storedItem = ItemStack.EMPTY
					markDirty()
				} else {
					makeNewItemEntity()
				}
			} else {
				if (storedItem != itemEnt!!.stack) {
					if (forceBlock) {
						itemEnt!!.stack = storedItem
					} else {
						storedItem = itemEnt!!.stack
						markDirty()
					}
				}
			}
		}
	}

	fun tick(world: World, pos: BlockPos, state: BlockState?) {
		if (world.isClient()) return
		interactedCheck.clear()
		val hopperableItemEnts = getInputItemEntities()
		hopperableItemEnts.sortedWith { a: ItemEntity, b: ItemEntity ->
			(pos.getSquaredDistanceFromCenter(a.pos.x, a.pos.y, a.pos.z) - pos.getSquaredDistanceFromCenter(
				b.pos.x,
				b.pos.y,
				b.pos.z
			)).toInt()
		}

		syncItemWithEntity(false)
		var wasItemUpdated = false
		for (iEnt in hopperableItemEnts) {
			wasItemUpdated = true
			val entStack = iEnt.stack
			if (storedItem.isEmpty) {
				storedItem = entStack.copy()
				entStack.decrement(entStack.count)
				break
			}
			if (ItemStack.canCombine(storedItem, entStack)) {
				val amtToMove =
					min((storedItem.maxCount - storedItem.count).toDouble(), entStack.count.toDouble()).toInt()
				storedItem.increment(amtToMove)
				entStack.decrement(amtToMove)
				break
			}
		}
		if (wasItemUpdated) syncItemWithEntity(true)
	}

	private fun getInputItemEntities(): List<ItemEntity> {
		val box = Box(
			pos.x.toDouble(),
			pos.y.toDouble(),
			pos.z.toDouble(),
			(pos.x + 1).toDouble(),
			(pos.y + 1).toDouble(),
			(pos.z + 1).toDouble()
		)
		return world!!.getEntitiesByClass(
			ItemEntity::class.java,
			box
		) { ent -> ent.uuid != persistentUUID && EntityPredicates.VALID_ENTITY.test(ent) }
	}

	override fun size() = 1
	override fun isEmpty() = storedItem.isEmpty

	override fun getStack(slot: Int): ItemStack {
		if (slot == 0)
			return storedItem
		return ItemStack.EMPTY
	}

	override fun setStack(slot: Int, stack: ItemStack) {
		if (slot == 0) {
			storedItem = stack
			syncItemWithEntity(true)
			markDirty()
		}
	}

	override fun removeStack(slot: Int): ItemStack {
		if (slot == 0) {
			val temp = storedItem
			storedItem = ItemStack.EMPTY
			syncItemWithEntity(true)
			markDirty()
			return temp
		}
		return ItemStack.EMPTY
	}

	override fun removeStack(slot: Int, amount: Int): ItemStack {
		if (slot == 0) {
			val newSplit = storedItem.split(amount)
			syncItemWithEntity(true)
			markDirty()
			return newSplit
		}
		return ItemStack.EMPTY
	}

	override fun canPlayerUse(player: PlayerEntity) = false

	override fun clear() {
		storedItem = ItemStack.EMPTY
		syncItemWithEntity(true)
		markDirty()
	}

	override fun markDirty() {
		(world as? ServerWorld)?.chunkManager?.markForUpdate(pos)
		super.markDirty()
	}

	override fun toInitialChunkDataNbt(): NbtCompound {
		val tag = NbtCompound()
		this.writeNbt(tag)
		return tag
	}

	override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)

	companion object {
		const val ITEM_DATA_TAG: String = "inv_storage"
		const val PERSISTENT_UUID_TAG: String = "persistent_uuid"
		const val HEIGHT = 0.75f
	}
}