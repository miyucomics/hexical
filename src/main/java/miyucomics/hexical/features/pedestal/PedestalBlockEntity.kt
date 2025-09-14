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
	}

	fun onBlockPlace() {
		this.persistentUUID = UUID.randomUUID()
		val position = Vec3d.ofCenter(this.pos)
		this.heldEntity = ItemEntity(this.world, position.x, position.y, position.z, this.heldStack)
		this.heldEntity!!.uuid = this.persistentUUID
		configureItemEntity()
		this.world?.spawnEntity(this.heldEntity)
	}

	fun onUse(player: PlayerEntity, hand: Hand) {
		val playerStack = player.getStackInHand(hand)

		// if can merge, merge
		if (ItemStack.canCombine(this.heldStack, playerStack)) {
			if (!world!!.isClient) {
				val amount = min(this.heldStack.maxCount - this.heldStack.count, playerStack.count)
				this.heldStack.increment(amount)
				playerStack.decrement(amount)
				updateItemEntity()
			}
			markDirty()
			return
		}

		// else just swap with player hand
		player.setStackInHand(hand, this.heldStack)
		this.heldStack = playerStack
		updateItemEntity()
		markDirty()
	}

	fun tick(world: World) {
		if (world.isClient)
			return

		tryFillItemEntity()
		updateItemStack()
		suckOrMergeItems()
		updateItemEntity()
		configureItemEntity()
	}

	private fun suckOrMergeItems() {
		world!!.getEntitiesByClass(ItemEntity::class.java, Box.from(BlockBox(this.pos)).contract(0.1)) { it.uuid != persistentUUID && !it.isRemoved && !it.cannotPickup() }.forEach { item ->
			val stack = item.stack
			if (this.heldStack.isEmpty) {
				this.heldStack = stack.copyAndEmpty()
				item.discard()
				updateItemEntity()
				markDirty()
				return
			}

			if (ItemStack.canCombine(this.heldStack, stack)) {
				val toTransfer = min(this.heldStack.maxCount - this.heldStack.count, stack.count)
				this.heldStack.increment(toTransfer)
				stack.decrement(toTransfer)
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

	private fun updateItemStack() {
		if (world!!.isClient) return
		if (this.heldEntity == null || this.heldEntity!!.isRemoved) {
			heldStack = ItemStack.EMPTY
			markDirty()
			return
		}
		this.heldStack = this.heldEntity!!.stack
		markDirty()
	}

	private fun updateItemEntity() {
		if (world!!.isClient)
			return

		if (this.heldStack.isEmpty) {
			this.heldEntity?.discard()
			this.heldEntity = null
			markDirty()
			return
		}

		val serverWorld = this.world as? ServerWorld ?: return

		if (this.heldEntity == null || this.heldEntity!!.isRemoved && !tryFillItemEntity()) {
			val position = Vec3d.ofCenter(this.pos)
			this.heldEntity = ItemEntity(serverWorld, position.x, position.y, position.z, this.heldStack)
			this.heldEntity!!.uuid = this.persistentUUID
			configureItemEntity()
			serverWorld.spawnEntity(this.heldEntity)
		}

		this.heldEntity?.stack = this.heldStack
	}

	fun tryFillItemEntity(): Boolean {
		if (this.persistentUUID == null)
			this.persistentUUID = UUID.randomUUID()
		if (this.heldEntity != null)
			return true
		val serverWorld = this.world as? ServerWorld ?: return true
		val existing = serverWorld.getEntity(persistentUUID!!)
		if (existing is ItemEntity) {
			this.heldEntity = existing
			return true
		}
		return false
	}

	fun configureItemEntity() {
		this.heldEntity?.let {
			it.setPosition(getItemPosition().subtract(Vec3d.of(normalVector).multiply(0.1)))
			it.boundingBox = Box(getItemPosition(), getItemPosition()).expand(0.25)
			it.noClip = true
			it.setNeverDespawn()
			it.setNoGravity(true)
			it.isInvisible = true
			it.isInvulnerable = true
			it.velocity = Vec3d.ZERO
			it.setPickupDelayInfinite()
		}
	}

	override fun readNbt(nbt: NbtCompound) {
		super.readNbt(nbt)
		heldStack = ItemStack.fromNbt(nbt.getCompound("item"))
		if (nbt.containsUuid("persistent_uuid"))
			persistentUUID = nbt.getUuid("persistent_uuid")
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

	fun getItemPosition(): Vec3d = Vec3d.ofCenter(this.pos).add(Vec3d.of(normalVector).multiply(HEIGHT))

	companion object {
		const val HEIGHT = 0.75
	}
}