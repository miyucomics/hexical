package miyucomics.hexical.features.zap

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.util.math.BlockPos

object OpZap : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getBlockPos(0, argc)
		env.assertPosInRange(pos)
		val power = args.getPositiveIntUnderInclusive(1, 15, argc)
		val time = args.getPositiveIntUnderInclusive(2, 100, argc)
		return SpellAction.Result(Spell(pos, power, time), MediaConstants.DUST_UNIT / 100, listOf())
	}

	private data class Spell(val pos: BlockPos, val power: Int, val time: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			ZapManager.triggerRedstone(env.world, pos, power, time)
		}
	}
}