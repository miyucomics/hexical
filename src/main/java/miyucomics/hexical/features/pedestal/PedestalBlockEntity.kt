package miyucomics.hexical.features.pedestal

import at.petrak.hexcasting.api.addldata.ADIotaHolder
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage.ParenthesizedIota
import at.petrak.hexcasting.api.casting.iota.Iota
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
import kotlin.math.min

open class PedestalBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(HexicalBlocks.PEDESTAL_BLOCK_ENTITY, pos, state), Inventory, ADIotaHolder {
	var heldEntity: ItemEntity? = null
	val heldStack: ItemStack
		get() = this.heldEntity?.stack ?: ItemStack.EMPTY

	private var pendingStack: ItemStack? = null
	val normalVector: Vec3i = cachedState.get(PedestalBlock.FACING).vector

	fun getItemPosition(): Vec3d = Vec3d.ofCenter(this.pos).add(Vec3d.of(normalVector).multiply(HEIGHT))

	// Sets the held item stack if the item entity exists, or spawns one in if it does not. Marks dirty. Will set by reference
	private fun setHeldItemAndEntity(stack: ItemStack) {
		val world = this.world ?: return
		if (world.isClient) return

		if (stack.isEmpty) {
			this.heldEntity?.discard()
			this.heldEntity = null
			markDirty()
			return
		}

		val held = this.heldEntity
		if (held != null && !held.isRemoved) {
			held.stack = stack
		} else {
			val pos = getItemPosition()
			val new = ItemEntity(world, pos.x, pos.y, pos.z, stack.copy())
			world.spawnEntity(new)
			new.renewPermanence()
			this.heldEntity = new
		}

		markDirty()
	}

	fun onBlockBreak() {
		this.heldEntity?.discard()
	}

	fun createItemEntityFromPending() {
		this.heldEntity = ItemEntity(this.world, this.pos.x + 0.5, this.pos.y + 0.5, this.pos.z + 0.5, this.pendingStack)
		this.heldEntity?.renewPermanence()
		this.world?.spawnEntity(this.heldEntity)
	}

	fun onUse(player: PlayerEntity, hand: Hand): ActionResult {
		val world = this.world ?: return ActionResult.PASS
		if (world.isClient) return ActionResult.SUCCESS

		val playerStack = player.getStackInHand(hand)
		val held = this.heldEntity

		if (held != null && ItemStack.canCombine(this.heldStack, playerStack)) {
			val amount = min(this.heldStack.maxCount - this.heldStack.count, playerStack.count)
			if (amount > 0) {
				this.heldStack.increment(amount)
				playerStack.decrement(amount)
				held.stack = this.heldStack
				markDirty()
				return ActionResult.SUCCESS
			}
		}

		val pedestalStack = this.heldStack.copy()
		setHeldItemAndEntity(playerStack)
		player.setStackInHand(hand, pedestalStack)

		return ActionResult.SUCCESS
	}

	fun tick(world: World) {
		if (world.isClient)
			return

		if (this.pendingStack != null) {
			setHeldItemAndEntity(this.pendingStack!!)
			this.pendingStack = null
		}

		if (this.heldEntity != null && this.heldStack.isEmpty)
			setHeldItemAndEntity(ItemStack.EMPTY)

		suckOrMergeItems()
		this.heldEntity?.renewPermanence()
	}

	private fun suckOrMergeItems() {
		val world = this.world ?: return
		if (world.isClient) return

		world.getEntitiesByClass(ItemEntity::class.java, Box.from(BlockBox(this.pos)).contract(0.1)) { !it.isRemoved && !it.cannotPickup() }.forEach { target ->
			val held = this.heldEntity

			// adopt an item entity as the held entity
			if (held == null || this.heldStack.isEmpty) {
				this.heldEntity = target
				target.renewPermanence()
				markDirty()
				return
			}

			if (ItemStack.canCombine(this.heldStack, target.stack)) {
				val transfer = min(this.heldStack.maxCount - this.heldStack.count, target.stack.count)

				if (transfer > 0) {
					this.heldStack.increment(transfer)
					target.stack.decrement(transfer)

					held.stack = this.heldStack
					if (target.stack.isEmpty) {
						target.discard()
					} else {
						target.stack = target.stack
					}

					markDirty()
					return
				}
			}
		}
	}

	open fun modifyImage(image: CastingImage): CastingImage {
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
	override fun canPlayerUse(player: PlayerEntity) = false

	override fun getStack(slot: Int): ItemStack = if (slot == 0) heldStack else ItemStack.EMPTY

	override fun setStack(slot: Int, stack: ItemStack) {
		if (slot != 0) return
		setHeldItemAndEntity(stack)
	}

	override fun removeStack(slot: Int): ItemStack {
		if (slot != 0) return ItemStack.EMPTY
		val removed = this.heldStack.copy()
		setHeldItemAndEntity(ItemStack.EMPTY)
		return removed
	}

	override fun removeStack(slot: Int, amount: Int): ItemStack {
		if (slot != 0 || this.heldStack.isEmpty) return ItemStack.EMPTY
		val held = this.heldEntity ?: return ItemStack.EMPTY

		val currentlyHeldStack = held.stack
		val splitStack = currentlyHeldStack.split(amount)

		if (currentlyHeldStack.isEmpty) {
			setHeldItemAndEntity(ItemStack.EMPTY)
		} else {
			held.stack = currentlyHeldStack
		}

		markDirty()
		return splitStack
	}

	override fun clear() = setHeldItemAndEntity(ItemStack.EMPTY)

	fun ItemEntity.renewPermanence() {
		setPosition(getItemPosition().subtract(Vec3d.of(normalVector).multiply(0.1)))
		boundingBox = Box(getItemPosition(), getItemPosition()).expand(0.25)
		noClip = true
		setNeverDespawn()
		setNoGravity(true)
		isInvisible = true
		isInvulnerable = true
		velocity = Vec3d.ZERO
		setPickupDelayInfinite()
	}

	override fun readNbt(nbt: NbtCompound) {
		super.readNbt(nbt)
		this.pendingStack = ItemStack.fromNbt(nbt.getCompound("stack"))
	}

	override fun writeNbt(nbt: NbtCompound) {
		super.writeNbt(nbt)
		nbt.putCompound("stack", this.heldStack.writeNbt(NbtCompound()))
	}

	override fun writeable() = true
	override fun readIotaTag() = IXplatAbstractions.INSTANCE.findDataHolder(this.heldStack)?.readIotaTag()
	override fun writeIota(iota: Iota?, simulate: Boolean) = IXplatAbstractions.INSTANCE.findDataHolder(this.heldStack)?.writeIota(iota, simulate) ?: false

	override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)
	override fun toInitialChunkDataNbt(): NbtCompound = NbtCompound().also { writeNbt(it) }
	override fun markDirty() {
		world?.updateListeners(pos, cachedState, cachedState, 3)
		super.markDirty()
	}

	companion object {
		const val HEIGHT = 0.75
	}
}