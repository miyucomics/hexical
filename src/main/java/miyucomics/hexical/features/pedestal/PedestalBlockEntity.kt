package miyucomics.hexical.features.pedestal

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage.ParenthesizedIota
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.*
import net.minecraft.world.World
import java.util.*
import kotlin.math.min

class PedestalBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(HexicalBlocks.PEDESTAL_BLOCK_ENTITY, pos, state), Inventory {
	var heldStack: ItemStack = ItemStack.EMPTY
	private var heldEntity: ItemEntity? = null
	private var persistentUUID: UUID? = null
	val normalVector: Vec3i = cachedState.get(PedestalBlock.FACING).vector

	fun onBlockBreak() {
		heldEntity?.discard()
		if (world is ServerWorld && !heldStack.isEmpty) {
			val spawnPos = getItemPosition()
			(world as ServerWorld).spawnEntity(ItemEntity(world, spawnPos.x, spawnPos.y, spawnPos.z, heldStack))
		}
		markDirty()
	}

	fun onUse(player: PlayerEntity, hand: Hand): ActionResult {
		val stackInHand = player.getStackInHand(hand)
		if (stackInHand.isEmpty && !heldStack.isEmpty) {
			player.setStackInHand(hand, heldStack.copy())
			heldStack = ItemStack.EMPTY
			updateItemEntity()
			markDirty()
			return ActionResult.SUCCESS
		}

		if (ItemStack.canCombine(heldStack, stackInHand)) {
			if (!world!!.isClient) {
				val amount = min(heldStack.maxCount - heldStack.count, stackInHand.count)
				heldStack.increment(amount)
				stackInHand.decrement(amount)
				updateItemEntity()
			}
			markDirty()
			return ActionResult.SUCCESS
		}

		if (!world!!.isClient) {
			val previousStack = heldStack
			heldStack = stackInHand.copy()
			stackInHand.decrement(stackInHand.count)
			if (!previousStack.isEmpty) {
				if (!player.giveItemStack(previousStack)) {
					player.dropItem(previousStack, false)
				}
			}
			updateItemEntity()
		}

		markDirty()
		return ActionResult.SUCCESS
	}

	fun tick(world: World) {
		if (world.isClient)
			return

		updateItemStack()
		suckOrMergeItems()
		heldEntity?.apply {
			setPos(getItemPosition().x, getItemPosition().y, getItemPosition().z)
			setPickupDelayInfinite()
			velocity = Vec3d.ZERO
		}
	}

	private fun suckOrMergeItems() {
		val box = Box.from(BlockBox(pos)).expand(0.5)
		val candidates = world!!.getEntitiesByClass(ItemEntity::class.java, box) {
			it.uuid != persistentUUID && !it.isRemoved
		}
		for (entity in candidates) {
			val stack = entity.stack
			if (heldStack.isEmpty) {
				heldStack = stack.copy()
				entity.discard()
				updateItemEntity()
				markDirty()
				return
			} else if (ItemStack.canCombine(heldStack, stack)) {
				val toTransfer = min(heldStack.maxCount - heldStack.count, stack.count)
				heldStack.increment(toTransfer)
				stack.decrement(toTransfer)
				if (stack.isEmpty) entity.discard()
				updateItemEntity()
				markDirty()
				return
			}
		}
	}

	fun modifyImage(image: CastingImage): CastingImage {
		val data = IXplatAbstractions.INSTANCE.findDataHolder(heldStack) ?: return image
		val iota = data.readIota(world as ServerWorld) ?: return image
		return if (image.parenCount == 0) {
			image.copy(stack = image.stack + iota)
		} else {
			image.copy(parenthesized = image.parenthesized + ParenthesizedIota(iota, false))
		}
	}

	override fun size() = 1
	override fun isEmpty() = heldStack.isEmpty
	override fun getStack(slot: Int): ItemStack = if (slot == 0) heldStack else ItemStack.EMPTY
	override fun setStack(slot: Int, stack: ItemStack) {
		if (slot == 0) {
			heldStack = stack
			updateItemEntity()
			markDirty()
		}
	}

	override fun removeStack(slot: Int): ItemStack = if (slot == 0) {
		val removed = heldStack
		heldStack = ItemStack.EMPTY
		updateItemEntity()
		markDirty()
		removed
	} else ItemStack.EMPTY

	override fun removeStack(slot: Int, amount: Int): ItemStack = if (slot == 0) {
		val removed = heldStack.split(amount)
		updateItemEntity()
		markDirty()
		removed
	} else ItemStack.EMPTY

	override fun canPlayerUse(player: PlayerEntity) = false
	override fun clear() {
		heldStack = ItemStack.EMPTY
		updateItemEntity()
		markDirty()
	}

	private fun populateHeldItemEntity() {
		val serverWorld = world as? ServerWorld ?: return
		if (heldStack.isEmpty) return

		if (persistentUUID == null) {
			persistentUUID = generateUniqueUUID()
		}

		heldEntity?.discard()
		heldEntity = null

		val existing = serverWorld.getEntity(persistentUUID!!)
		heldEntity = if (existing is ItemEntity) {
			existing.stack = heldStack
			existing
		} else {
			val position = getItemPosition()
			ItemEntity(serverWorld, position.x, position.y, position.z, heldStack).apply {
				uuid = persistentUUID!!
				setNoGravity(true)
				noClip = true
				setNeverDespawn()
				isInvulnerable = true
				isInvisible = true
				serverWorld.spawnEntity(this)
			}
		}
	}

	private fun updateItemEntity() {
		if (world!!.isClient) return
		if (heldStack.isEmpty) {
			heldEntity?.discard()
			heldEntity = null
			markDirty()
		} else if (heldEntity == null || heldEntity!!.stack != heldStack) {
			populateHeldItemEntity()
		}
	}

	private fun updateItemStack() {
		if (world!!.isClient) return
		if (heldEntity == null || heldEntity!!.isRemoved) {
			populateHeldItemEntity()
		} else if (heldEntity!!.stack != heldStack) {
			heldStack = heldEntity!!.stack
			markDirty()
		}
	}

	fun getItemPosition(): Vec3d = Vec3d.of(pos).add(Vec3d(0.5, 0.5, 0.5)).add(Vec3d.of(normalVector).multiply(HEIGHT - 0.2))

	override fun readNbt(nbt: NbtCompound) {
		super.readNbt(nbt)
		heldStack = ItemStack.fromNbt(nbt.getCompound("item"))
		if (nbt.containsUuid("persistent_uuid")) {
			persistentUUID = nbt.getUuid("persistent_uuid")
		}
	}

	override fun writeNbt(nbt: NbtCompound) {
		super.writeNbt(nbt)
		nbt.putCompound("item", heldStack.writeNbt(NbtCompound()))
		persistentUUID?.let { nbt.putUuid("persistent_uuid", it) }
	}

	override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)
	override fun toInitialChunkDataNbt(): NbtCompound = NbtCompound().also { writeNbt(it) }

	override fun markDirty() {
		world?.updateListeners(pos, cachedState, cachedState, 3)
		super.markDirty()
	}

	private fun generateUniqueUUID(): UUID {
		val serverWorld = world as? ServerWorld ?: return UUID.randomUUID()
		var candidate: UUID
		do {
			candidate = UUID.randomUUID()
		} while (serverWorld.getEntity(candidate) != null)
		return candidate
	}

	companion object {
		const val HEIGHT = 0.75f
	}
}