package miyucomics.hexical.blocks

import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos

class MediaJarBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(HexicalBlocks.MEDIA_JAR_BLOCK_ENTITY, pos, state) {
	private var media: Int = 0

	fun getMedia() = this.media
	fun setMedia(media: Int) {
		this.media = media
		markDirty()
	}

	override fun writeNbt(nbt: NbtCompound) {
		nbt.putInt("media", media)
	}

	override fun readNbt(nbt: NbtCompound) {
		this.media = nbt.getInt("media")
	}

	override fun toInitialChunkDataNbt(): NbtCompound = createNbt()
	override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)
}