package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.pigment.FrozenPigment
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos

class HexCandleBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(HexicalBlocks.HEX_CANDLE_BLOCK_ENTITY, pos, state) {
	private var pigment: FrozenPigment = FrozenPigment.DEFAULT.get()

	fun getPigment() = this.pigment
	fun setPigment(pigment: FrozenPigment) {
		this.pigment = pigment
		markDirty()
	}

	override fun writeNbt(nbt: NbtCompound) {
		nbt.put("pigment", pigment.serializeToNBT())
	}

	override fun readNbt(nbt: NbtCompound) {
		pigment = FrozenPigment.fromNBT(nbt.getCompound("pigment"))
	}

	override fun toInitialChunkDataNbt(): NbtCompound = createNbt()
	override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)
}