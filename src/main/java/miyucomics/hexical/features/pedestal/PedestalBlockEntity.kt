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
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World
import java.util.*
import kotlin.math.min

class PedestalBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(HexicalBlocks.PEDESTAL_BLOCK_ENTITY, pos, state), Inventory {
	private var heldStack: ItemStack = ItemStack.EMPTY
	private var heldEntity: ItemEntity? = null
	private var persistentUUID: UUID? = null
	private val normalVector: Vec3i = cachedState.get(PedestalBlock.FACING).vector

	fun onBlockBreak() {
		heldEntity?.discard()
		if (world !is ServerWorld)
			return
		if (!heldStack.isEmpty) {
			val spawnPosition = getItemPosition()
			(world as ServerWorld).spawnEntity(ItemEntity(world, spawnPosition.x, spawnPosition.y, spawnPosition.z, heldStack))
		}
		markDirty()
	}

	fun onUse(player: PlayerEntity, hand: Hand): ActionResult {
		val playerStack = player.getStackInHand(hand)

		if (playerStack.isEmpty) {
			if (heldStack.isEmpty)
				return ActionResult.PASS
			setStack(0, playerStack.copy())
			playerStack.decrement(playerStack.count)
			return ActionResult.SUCCESS
		}

		if (ItemStack.canCombine(heldStack, playerStack)) {
			if (!world!!.isClient) {
				val amount = min((playerStack.maxCount - playerStack.count), playerStack.count)
				playerStack.increment(amount)
				playerStack.decrement(amount)
				updateItemEntity()
			}
			return ActionResult.SUCCESS
		}

		if (ItemStack.canCombine(heldStack, player.getStackInHand(if (hand == Hand.MAIN_HAND) Hand.OFF_HAND else Hand.MAIN_HAND)))
			return ActionResult.PASS

		if (!world!!.isClient) {
			val stackToGive = removeStack(0)
			if (hand == Hand.MAIN_HAND) {
				setStack(0, playerStack.copy())
				playerStack.decrement(playerStack.count)
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

		if (heldEntity != null) {
			val position = getItemPosition()
			heldEntity!!.setPos(position.x, position.y, position.z)
			heldEntity!!.setVelocity(0.0, 0.0, 0.0)
			heldEntity!!.setPickupDelayInfinite()
			heldEntity!!.isInvisible = true
		}

		updateItemStack()
		suckOrMergeItems()
	}

	fun suckOrMergeItems() {
		var itemUpdated = false

		val possibleEntities = world!!.getEntitiesByClass(ItemEntity::class.java, Box.from(BlockBox(pos))) { it.uuid != persistentUUID && !it.isRemoved }
		for (possibleEntity in possibleEntities) {
			val stack = possibleEntity.stack

			if (heldStack.isEmpty) {
				heldStack = stack.copyAndEmpty()
				itemUpdated = true
				break
			}

			if (ItemStack.canCombine(heldStack, stack)) {
				val amount = min((heldStack.maxCount - heldStack.count), stack.count)
				heldStack.increment(amount)
				stack.decrement(amount)
				itemUpdated = true
				break
			}
		}

		if (itemUpdated)
			updateItemEntity()
	}

	fun modifyImage(image: CastingImage): CastingImage {
		val data = IXplatAbstractions.INSTANCE.findDataHolder(heldStack) ?: return image
		val iota = data.readIota(world as ServerWorld) ?: return image
		return if (image.parenCount == 0) {
			val stack = image.stack.toMutableList()
			stack.add(iota)
			image.copy(stack = stack)
		} else {
			val parenthesized = image.parenthesized.toMutableList()
			parenthesized.add(ParenthesizedIota(iota, false))
			image.copy(parenthesized = parenthesized)
		}
	}

	override fun size() = 1
	override fun isEmpty() = heldStack.isEmpty

	override fun getStack(slot: Int): ItemStack {
		if (slot == 0)
			return heldStack
		return ItemStack.EMPTY
	}

	override fun setStack(slot: Int, stack: ItemStack) {
		if (slot == 0) {
			heldStack = stack
			updateItemEntity()
			markDirty()
		}
	}

	override fun removeStack(slot: Int): ItemStack {
		if (slot == 0) {
			val temp = heldStack
			heldStack = ItemStack.EMPTY
			updateItemEntity()
			markDirty()
			return temp
		}
		return ItemStack.EMPTY
	}

	override fun removeStack(slot: Int, amount: Int): ItemStack {
		if (slot == 0) {
			val removedAmount = heldStack.split(amount)
			updateItemEntity()
			markDirty()
			return removedAmount
		}
		return ItemStack.EMPTY
	}

	override fun canPlayerUse(player: PlayerEntity) = false

	override fun clear() {
		heldStack = ItemStack.EMPTY
		updateItemEntity()
		markDirty()
	}

	private fun generateUniqueUUID(): UUID {
		val world = getWorld()
		var newUUID = UUID.randomUUID()
		if (world !is ServerWorld)
			return newUUID
		while (world.getEntity(newUUID) != null)
			newUUID = UUID.randomUUID()
		return newUUID
	}

	private fun populateHeldItemEntity() {
		val serverWorld = world as? ServerWorld ?: return

		if (persistentUUID == null)
			persistentUUID = generateUniqueUUID()

		if (heldStack.isEmpty)
			return

		heldEntity?.discard()
		heldEntity = null

		val possibleOverItem = serverWorld.getEntity(persistentUUID)
		if (possibleOverItem is ItemEntity) {
			heldEntity = possibleOverItem
			heldEntity!!.stack = heldStack
		} else {
			val xPos = pos.x + 0.5 + (HEIGHT - 0.2) * normalVector.x
			val yPos = pos.y + 0.5 + (HEIGHT - 0.2) * normalVector.y
			val zPos = pos.z + 0.5 + (HEIGHT - 0.2) * normalVector.z
			heldEntity = ItemEntity(serverWorld, xPos, yPos, zPos, heldStack, 0.0, 0.0, 0.0)
			heldEntity!!.uuid = persistentUUID
			heldEntity!!.setNoGravity(true)
			heldEntity!!.noClip = true
			heldEntity!!.setNeverDespawn()
			heldEntity!!.isInvulnerable = true
			serverWorld.spawnEntity(heldEntity)
		}
	}

	private fun updateItemEntity() {
		if (world!!.isClient)
			return
		if (heldEntity == null)
			return

		// item stack is gone
		if (heldStack.isEmpty) {
			heldEntity!!.stack = ItemStack.EMPTY
			heldEntity = null
			markDirty()
			return
		}

		// item entity or item stack are out of sync
		if (heldStack != heldEntity!!.stack)
			heldEntity!!.stack = heldStack
	}

	private fun updateItemStack() {
		if (world!!.isClient)
			return

		// item entity is gone
		if (heldEntity == null) {
			populateHeldItemEntity()
			return
		}

		// item entity was removed
		if (heldEntity!!.isRemoved) {
			if (heldEntity!!.removalReason?.shouldDestroy() == true) {
				heldStack = ItemStack.EMPTY
				markDirty()
				return
			}
			populateHeldItemEntity()
			return
		}

		// item entity or item stack are out of sync
		if (heldStack != heldEntity?.stack) {
			heldStack = heldEntity!!.stack
			markDirty()
		}
	}

	private fun getItemPosition() = Vec3d.ofCenter(pos).add(Vec3d.of(normalVector).multiply(HEIGHT - 0.2))

	override fun readNbt(nbt: NbtCompound) {
		super.readNbt(nbt)
		this.heldStack = ItemStack.fromNbt(nbt.getCompound("item"))
		if (nbt.containsUuid("persistent_uuid"))
			this.persistentUUID = nbt.getUuid("persistent_uuid")
	}

	override fun writeNbt(nbt: NbtCompound) {
		super.writeNbt(nbt)
		nbt.putCompound("item", heldStack.writeNbt(NbtCompound()))
		if (persistentUUID != null)
			nbt.putUuid("persistent_uuid", persistentUUID)
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