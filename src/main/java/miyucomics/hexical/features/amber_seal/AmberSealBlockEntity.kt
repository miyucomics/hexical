package miyucomics.hexical.features.amber_seal

import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.enums.ChestType
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos

class AmberSealBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(HexicalBlocks.AMBER_SEAL_BLOCK_ENTITY, pos, state) {
	var encasedState: BlockState? = null
	var encasedEntity: BlockEntity? = null

	fun init(state: BlockState, blockEntity: BlockEntity?) {
		this.encasedState = state
		this.encasedEntity = blockEntity
		if (this.encasedState!!.contains(Properties.CHEST_TYPE))
			this.encasedState = this.encasedState!!.with(Properties.CHEST_TYPE, ChestType.SINGLE)
		this.encasedEntity?.setWorld(this.world)
	}

	override fun readNbt(nbt: NbtCompound) {
		BlockState.CODEC.decode(NbtOps.INSTANCE, nbt.getCompound("EncasedBlockState")).map { this.encasedState = it.first }
		this.encasedEntity = createFromNbt(this.pos, this.encasedState, nbt.getCompound("EncasedBlockEntity"))
		this.encasedEntity?.setWorld(world)
	}

	override fun writeNbt(nbt: NbtCompound) {
		nbt.put("EncasedBlockState", BlockState.CODEC.encodeStart(NbtOps.INSTANCE, this.encasedState).result().get())
		if (this.encasedEntity != null)
			nbt.put("EncasedBlockEntity", this.encasedEntity!!.createNbtWithId())
	}

	fun toItem(): ItemStack {
		val nbt = NbtCompound()
		writeNbt(nbt)
		val stack = ItemStack(HexicalBlocks.AMBER_SEAL_ITEM)
		BlockItem.setBlockEntityNbt(stack, this.type, nbt)
		return stack
	}

	fun onPlaced(ctx: ItemPlacementContext) {
		if (this.encasedState == null)
			return

		this.encasedState!!.block.getPlacementState(ctx)?.let { placementState ->
			val newState = AmberSealBlock.copyRotationParameters(this.encasedState!!, placementState)
			if (this.encasedState != newState) {
				this.encasedState = newState
				this.markDirty()
			}
		}
	}
}