package miyucomics.hexical.features.zap

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f

object OpZap : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val a = args.getVec3(0, argc)
		env.assertVecInRange(a)
		val b = args.getVec3(1, argc)
		env.assertVecInRange(b)
		val color = args.getVec3(2, argc)
		return SpellAction.Result(Spell(a, b, color), MediaConstants.DUST_UNIT / 100, listOf())
	}

	private data class Spell(val a: Vec3d, val b: Vec3d, val color: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			env.world.spawnParticles(ZapParticleEffect(b.subtract(a), Vector3f(color.x.toFloat(), color.y.toFloat(), color.z.toFloat())), a.x, a.y, a.z, 1, 0.0, 0.0, 0.0, 0.0)
		}
	}
}