package miyucomics.hexical.casting.patterns.conjure

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.projectile.FireballEntity
import net.minecraft.util.math.Vec3d

class OpGhastFireball : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		return SpellAction.Result(Spell(position), MediaConstants.DUST_UNIT * 3, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			val fireball = FireballEntity(env.world, env.castingEntity, 0.0, 0.0, 0.0, 1)
			fireball.setPosition(position)
			env.world.spawnEntity(fireball)
			return image.copy(stack = image.stack.toList().plus(EntityIota(fireball)))
		}
	}
}