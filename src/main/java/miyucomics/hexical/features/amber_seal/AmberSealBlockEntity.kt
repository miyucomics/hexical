package miyucomics.hexical.features.amber_seal

import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.enums.ChestType
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.Registries
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos

class AmberSealBlockEntity(pos: BlockPos, state: BlockState, var encasedState: BlockState) : BlockEntity(HexicalBlocks.AMBER_SEAL_BLOCK_ENTITY, pos, state) {
	constructor(pos: BlockPos, state: BlockState) : this(pos, state, Blocks.AIR.defaultState)

	// may not always have a block entity, but still sealable
	var encasedEntity: BlockEntity? = null

	fun init(state: BlockState, blockEntity: BlockEntity?) {
		this.encasedState = state
		this.encasedEntity = blockEntity
		if (this.encasedState.contains(Properties.CHEST_TYPE))
			this.encasedState = this.encasedState.with(Properties.CHEST_TYPE, ChestType.SINGLE)
		this.encasedEntity?.setWorld(this.world)
	}

	override fun readNbt(nbt: NbtCompound) {
		super.readNbt(nbt)
		this.encasedState = NbtHelper.toBlockState(Registries.BLOCK.readOnlyWrapper, nbt.getCompound("EncasedBlockState"))
		if (nbt.contains("EncasedBlockEntity")) {
			this.encasedEntity = createFromNbt(this.pos, this.encasedState, nbt.getCompound("EncasedBlockEntity"))
			this.encasedEntity?.setWorld(world)
		}
	}

	override fun writeNbt(nbt: NbtCompound) {
		super.writeNbt(nbt)
		nbt.put("EncasedBlockState", NbtHelper.fromBlockState(this.encasedState))
		this.encasedEntity?.let { nbt.put("EncasedBlockEntity", it.createNbtWithId()) }
	}

	fun toItem(): ItemStack {
		val nbt = NbtCompound()
		writeNbt(nbt)
		val stack = ItemStack(HexicalBlocks.AMBER_SEAL_ITEM)
		BlockItem.setBlockEntityNbt(stack, this.type, nbt)
		return stack
	}

	fun onPlaced(ctx: ItemPlacementContext) {
		this.encasedState.block.getPlacementState(ctx)?.let { placementState ->
			val newState = AmberSealBlock.copyRotationParameters(this.encasedState, placementState)
			if (this.encasedState != newState) {
				this.encasedState = newState
				this.markDirty()
			}
		}
	}

	override fun toInitialChunkDataNbt(): NbtCompound = createNbt()
	override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)
}