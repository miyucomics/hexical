package miyucomics.hexical.casting.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.data.DyeData
import miyucomics.hexical.iota.getDye
import net.minecraft.entity.mob.ShulkerEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos

class OpDye : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val position = args.getBlockPos(0, argc)
		val dye = args.getDye(1, argc)
		return Triple(BlockSpell(position, dye), MediaConstants.DUST_UNIT * 3, listOf())
	}

	private data class BlockSpell(val position: BlockPos, val dye: DyeColor) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			ctx.world.setBlockState(position, DyeData.getNewBlock(ctx.world.getBlockState(position).block, dye))
		}
	}

	private data class SheepSpell(val sheep: SheepEntity, val dye: DyeColor) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			sheep.color = dye
		}
	}

	private data class ShulkerSpell(val shulker: ShulkerEntity, val dye: DyeColor) : RenderedSpell {
		override fun cast(ctx: CastingContext) {

		}
	}
}