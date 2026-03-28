package miyucomics.hexical.features.pedestal

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage.ParenthesizedIota
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box

class CarpetedPedestalBlockEntity(pos: BlockPos, state: BlockState) : PedestalBlockEntity(HexicalBlocks.CARPETED_PEDESTAL_BLOCK_ENTITY, pos, state) {
	override fun modifyImage(image: CastingImage): CastingImage {
		val iota = getIota()
		return if (image.parenCount == 0) {
			image.copy(stack = image.stack + iota)
		} else {
			image.copy(parenthesized = image.parenthesized + ParenthesizedIota(iota, false))
		}
	}

	override fun writeable() = false
	override fun writeIota(iota: Iota?, simulate: Boolean) = false
	override fun readIotaTag(): NbtCompound = IotaType.serialize(getIota())

	private fun getIota(): Iota {
		if (this.heldEntity != null)
			return EntityIota(this.heldEntity!!)
		val entity = this.world!!.getOtherEntities(null, Box(this.pos.up())) { entity -> !entity.isSpectator && !entity.isRemoved }.firstOrNull() ?: return NullIota()
		return EntityIota(entity)
	}
}