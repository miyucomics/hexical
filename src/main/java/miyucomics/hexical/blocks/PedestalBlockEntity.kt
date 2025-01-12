package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import miyucomics.hexical.casting.env.ArchLampCastEnv
import miyucomics.hexical.casting.env.TurretLampCastEnv
import miyucomics.hexical.casting.env.TurretLampMishapEnv
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.items.ArchLampItem
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
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World
import java.util.*
import kotlin.math.abs
import kotlin.math.min

class PedestalBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(HexicalBlocks.PEDESTAL_BLOCK_ENTITY, pos, state), Inventory {
	private var heldItemStack: ItemStack = ItemStack.EMPTY
	private var heldItemEntity: ItemEntity? = null
	private var permanentUUID: UUID? = null
	private val normalVector: Vec3i = cachedState.get(PedestalBlock.FACING).vector

	init {
		if (getWorld() != null && !getWorld()!!.isClient) {
			permanentUUID = generateUniqueUUID()
			makeNewItemEntity()
			markDirty()
		}
	}

	fun onRemoved() {
		if (heldItemEntity != null)
			heldItemEntity!!.discard()
		if (world !is ServerWorld)
			return
		val heightOffset = HEIGHT - 0.5 + 0.01
		(world as ServerWorld).spawnEntity(ItemEntity(world, pos.x + 0.5 + heightOffset * normalVector.x, pos.y + 0.5 + heightOffset * normalVector.y, pos.z + 0.5 + heightOffset * normalVector.z, heldItemStack, 0.0, 0.0, 0.0))
		markDirty()
	}

	fun onUse(player: PlayerEntity, hand: Hand): ActionResult {
		val heldStack = player.getStackInHand(hand)

		if (heldItemStack.isEmpty) {
			if (heldStack.isEmpty)
				return ActionResult.PASS
			setStack(0, heldStack.copy())
			heldStack.decrement(heldStack.count)
			return ActionResult.SUCCESS
		}

		if (ItemStack.canCombine(heldItemStack, heldStack)) {
			if (!world!!.isClient) {
				val amount = min((heldItemStack.maxCount - heldItemStack.count), heldStack.count)
				heldItemStack.increment(amount)
				heldStack.decrement(amount)
				syncItemAndEntity(true)
			}
			return ActionResult.SUCCESS
		}

		if (ItemStack.canCombine(heldItemStack, player.getStackInHand(if (hand == Hand.MAIN_HAND) Hand.OFF_HAND else Hand.MAIN_HAND)))
			return ActionResult.PASS

		if (!world!!.isClient) {
			val stackToGive = removeStack(0)
			if (hand == Hand.MAIN_HAND) {
				setStack(0, heldStack.copy())
				heldStack.decrement(heldStack.count)
			}

			if (!stackToGive.isEmpty) {
				if (player.mainHandStack.isEmpty)
					player.setStackInHand(Hand.MAIN_HAND, stackToGive)
				else
					player.inventory.offerOrDrop(stackToGive)
			}
		}

		return ActionResult.SUCCESS
	}

	fun tick(world: World, pos: BlockPos) {
		if (world.isClient())
			return

		syncItemAndEntity(false)

		val inputtedItems = getInputItemEntities()
			.sortedWith { a: ItemEntity, b: ItemEntity -> (pos.getSquaredDistance(a.pos) - pos.getSquaredDistance(b.pos)).toInt() }

		var wasItemUpdated = false

		for (newItemEntity in inputtedItems) {
			val newStack = newItemEntity.stack

			if (heldItemStack.isEmpty) {
				heldItemStack = newStack.copy()
				newStack.decrement(newStack.count)
				wasItemUpdated = true
				break
			}

			if (ItemStack.canCombine(heldItemStack, newStack)) {
				val amount = min((heldItemStack.maxCount - heldItemStack.count), newStack.count)
				heldItemStack.increment(amount)
				newStack.decrement(amount)
				wasItemUpdated = true
				break
			}
		}

		if (wasItemUpdated)
			syncItemAndEntity(true)

		if (heldItemStack.isOf(HexicalItems.ARCH_LAMP_ITEM) && heldItemStack.orCreateNbt.getBoolean("active")) {
			syncItemAndEntity(true)
			val vm = CastingVM(CastingImage(), TurretLampCastEnv(heldItemEntity!!, world as ServerWorld))
			vm.queueExecuteAndWrapIotas((heldItemStack.item as ArchLampItem).getHex(heldItemStack, world)!!, world)
		}
	}

	private fun getInputItemEntities() =
		world!!.getEntitiesByClass(ItemEntity::class.java, Box.from(BlockBox(pos))) { item -> item.uuid != permanentUUID && EntityPredicates.VALID_ENTITY.test(item) }

	override fun size() = 1
	override fun isEmpty() = heldItemStack.isEmpty

	override fun getStack(slot: Int): ItemStack {
		if (slot == 0)
			return heldItemStack
		return ItemStack.EMPTY
	}

	override fun setStack(slot: Int, stack: ItemStack) {
		if (slot == 0) {
			heldItemStack = stack
			syncItemAndEntity(true)
			markDirty()
		}
	}

	override fun removeStack(slot: Int): ItemStack {
		if (slot == 0) {
			val temp = heldItemStack
			heldItemStack = ItemStack.EMPTY
			syncItemAndEntity(true)
			markDirty()
			return temp
		}
		return ItemStack.EMPTY
	}

	override fun removeStack(slot: Int, amount: Int): ItemStack {
		if (slot == 0) {
			val newSplit = heldItemStack.split(amount)
			syncItemAndEntity(true)
			markDirty()
			return newSplit
		}
		return ItemStack.EMPTY
	}

	override fun canPlayerUse(player: PlayerEntity) = false

	override fun clear() {
		heldItemStack = ItemStack.EMPTY
		syncItemAndEntity(true)
		markDirty()
	}

	override fun markDirty() {
		if (world !is ServerWorld)
			return
		(world as ServerWorld).chunkManager.markForUpdate(pos)
		super.markDirty()
	}

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
		var world = getWorld()!!
		if (world.isClient)
			return
		world = world as ServerWorld

		if (heldItemStack.isEmpty)
			return

		if (permanentUUID == null)
			permanentUUID = generateUniqueUUID()

		if (heldItemEntity != null) {
			heldItemEntity!!.discard()
			heldItemEntity = null
		}

		val heightOffset = HEIGHT - 0.5 + 0.01
		val xPos = pos.x + 0.5 + (heightOffset + 0.2f) * normalVector.x
		val yPos = pos.y + 0.2 + (heightOffset * normalVector.y) + abs(0.3 * normalVector.y) + (if (normalVector.y < 0) -0.7 else 0.0)
		val zPos = pos.z + 0.5 + (heightOffset + 0.2f) * normalVector.z
		val possibleOverItem = world.getEntity(permanentUUID)
		if (possibleOverItem is ItemEntity) {
			heldItemEntity = possibleOverItem
			heldItemEntity!!.stack = heldItemStack
		} else {
			heldItemEntity = ItemEntity(world, xPos, yPos, zPos, heldItemStack, 0.0, 0.0, 0.0)
			world.spawnEntity(heldItemEntity)
		}

		heldItemEntity!!.setPos(xPos, yPos, zPos)
		heldItemEntity!!.uuid = permanentUUID
		heldItemEntity!!.setNoGravity(true)
		heldItemEntity!!.noClip = true
		heldItemEntity!!.setPickupDelayInfinite()
		heldItemEntity!!.setNeverDespawn()
		heldItemEntity!!.isInvulnerable = true
		markDirty()
	}

	private fun syncItemAndEntity(changeItemEntity: Boolean) {
		if (world!!.isClient())
			return

		if (heldItemStack.isEmpty) {
			if (heldItemEntity != null) {
				heldItemEntity!!.stack = heldItemStack
				heldItemEntity = null
				markDirty()
			}
			return
		}

		if (heldItemEntity == null || heldItemEntity!!.isRemoved) {
			if (heldItemEntity != null && (heldItemEntity!!.removalReason == RemovalReason.DISCARDED || heldItemEntity!!.removalReason == RemovalReason.KILLED) && !changeItemEntity) {
				heldItemStack = ItemStack.EMPTY
				markDirty()
				return
			}

			makeNewItemEntity()
			return
		}

		if (heldItemStack != heldItemEntity!!.stack) {
			if (changeItemEntity)
				heldItemEntity!!.stack = heldItemStack
			else {
				heldItemStack = heldItemEntity!!.stack
				markDirty()
			}
		}
	}

	override fun readNbt(nbt: NbtCompound) {
		super.readNbt(nbt)
		this.heldItemStack = ItemStack.fromNbt(nbt.getCompound("item"))
		if (nbt.containsUuid("persistent_uuid")) this.permanentUUID = nbt.getUuid("persistent_uuid")
	}

	override fun writeNbt(nbt: NbtCompound) {
		super.writeNbt(nbt)
		nbt.put("item", heldItemStack.writeNbt(NbtCompound()))
		if (permanentUUID != null)
			nbt.putUuid("persistent_uuid", permanentUUID)
	}

	override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)
	override fun toInitialChunkDataNbt(): NbtCompound {
		val tag = NbtCompound()
		this.writeNbt(tag)
		return tag
	}

	companion object {
		const val HEIGHT = 0.75f
	}
}