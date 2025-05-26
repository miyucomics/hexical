package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.util.math.Vec3d

class OpHorrible : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getVec3(0, argc)
		env.assertVecInRange(pos)
		return SpellAction.Result(Spell(pos), 0, listOf())
	}

	private data class Spell(val pos: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {

		}
	}
}