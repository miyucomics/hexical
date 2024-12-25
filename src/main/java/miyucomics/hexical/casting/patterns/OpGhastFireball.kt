package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.projectile.FireballEntity
import net.minecraft.util.math.Vec3d

class OpGhastFireball : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getVec3(0, argc)
		env.assertVecInRange(pos)
		return SpellAction.Result(Spell(pos), MediaConstants.DUST_UNIT * 3, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val fireball = FireballEntity(env.world, env.castingEntity, 0.0, 0.0, 0.0, 1)
			fireball.setPosition(position)
			env.world.spawnEntity(fireball)
		}
	}
}