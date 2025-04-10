package miyucomics.hexical.casting.patterns.conjure

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.projectile.thrown.EggEntity
import net.minecraft.util.math.Vec3d

class OpEgg : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		return SpellAction.Result(Spell(position), MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			val egg = EggEntity(env.world, position.x, position.y, position.z)
			egg.owner = env.castingEntity
			env.world.spawnEntity(egg)
			return image.copy(stack = image.stack.toList().plus(EntityIota(egg)))
		}
	}
}