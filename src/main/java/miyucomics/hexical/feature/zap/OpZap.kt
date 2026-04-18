package miyucomics.hexical.feature.zap

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f

object OpZap : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val a = args.getVec3(0, argc)
		env.assertVecInRange(a)
		val b = args.getVec3(1, argc)
		env.assertVecInRange(b)
		return SpellAction.Result(Spell(a, b.subtract(a)), MediaConstants.DUST_UNIT / 100, listOf())
	}

	private data class Spell(val position: Vec3d, val offset: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			env.world.spawnParticles(ZapParticleEffect(Vector3f(offset.x.toFloat(), offset.y.toFloat(), offset.z.toFloat())), position.x, position.y, position.z, 1, 0.0, 0.0, 0.0, 0.0)
		}
	}
}