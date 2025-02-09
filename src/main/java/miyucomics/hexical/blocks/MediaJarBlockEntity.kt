package miyucomics.hexical.blocks

import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos
import kotlin.math.max

class MediaJarBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(HexicalBlocks.MEDIA_JAR_BLOCK_ENTITY, pos, state) {
	private var media: Long = 0

	fun getMedia() = this.media
	fun setMedia(media: Long) {
		this.media = media
		markDirty()
	}
	fun addMedia(media: Long) {
		setMedia(getMedia() + media)
	}
	fun withdrawMedia(media: Long): Boolean {
		if (getMedia() >= media) {
			setMedia(getMedia() - media)
			return true
		} else {
			setMedia(0)
			return false
		}
	}

	override fun writeNbt(nbt: NbtCompound) {
		nbt.putLong("media", media)
	}

	override fun readNbt(nbt: NbtCompound) {
		this.media = nbt.getLong("media")
	}

	override fun toInitialChunkDataNbt(): NbtCompound = createNbt()
	override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)
}