package miyucomics.hexical.casting.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.iota.getDye
import net.minecraft.block.BedBlock
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry

class OpDye : SpellAction {

	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val position = args.getBlockPos(0, argc)
		val dye = args.getDye(1, argc)
		return Triple(BlockSpell(position, dye), MediaConstants.DUST_UNIT * 3, listOf())
	}

	private data class BlockSpell(val position: BlockPos, val dye: DyeColor) : RenderedSpell {
		private val suffixes = listOf("shulker_box", "bed", "stained_glass", "stained_glass_pane", "candle", "glazed_terracotta", "terracotta", "wool", "carpet", "concrete", "concrete_powder", "candle_cake")

		override fun cast(ctx: CastingContext) {
			val name = Registry.BLOCK.getId(ctx.world.getBlockState(position).block)
			for (suffix in suffixes) {
				if (name.path.endsWith(suffix)) {
					ctx.world.setBlockState(position, Registry.BLOCK.get(Identifier(name.namespace, dye.name + "_" + suffix)).defaultState)
					return
				}
			}
		}
	}
}