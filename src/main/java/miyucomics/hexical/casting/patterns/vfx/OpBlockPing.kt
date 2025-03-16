package miyucomics.hexical.casting.patterns.vfx

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.particles.CubeParticleEffect
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f

class OpBlockPing : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		val color = args.getVec3(1, argc)
		val lifespan = args.getPositiveIntUnderInclusive(2, 200, argc)
		return SpellAction.Result(Spell(position, color, lifespan), MediaConstants.DUST_UNIT / 100, listOf())
	}

	private data class Spell(val position: Vec3d, val color: Vec3d, val lifespan: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			env.world.spawnParticles(CubeParticleEffect(Vector3f(color.x.toFloat(), color.y.toFloat(), color.z.toFloat()), lifespan), position.x, position.y, position.z, 1, 0.0, 0.0, 0.0, 0.0)
		}
	}
}