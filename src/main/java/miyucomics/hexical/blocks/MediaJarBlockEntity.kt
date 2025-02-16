package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.lib.HexItems
import com.mojang.datafixers.util.Pair
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min

class MediaJarBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(HexicalBlocks.MEDIA_JAR_BLOCK_ENTITY, pos, state) {
	private var media: Long = 0

	fun scryingLensOverlay(lines: MutableList<Pair<ItemStack, Text>>) {
		lines.add(Pair(ItemStack(HexItems.AMETHYST_DUST), Text.translatable("hexcasting.tooltip.media", format.format(media.toFloat() / MediaConstants.DUST_UNIT.toFloat()))))
	}

	fun getMedia() = this.media
	private fun setMedia(media: Long) {
		this.media = max(min(media, MediaJarBlock.MAX_CAPACITY), 0)
		markDirty()
	}
	fun insertMedia(media: Long): Long {
		val currentMedia = this.media
		setMedia(this.media + media)
		return this.getMedia() - currentMedia
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

	companion object {
		private var format = DecimalFormat("###,###.##")
	}
}