package miyucomics.hexical.casting.patterns.circle

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.circle.MishapNoSpellCircle
import miyucomics.hexical.casting.mishaps.OutsideCircleMishap
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d

class OpDisplace : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env !is CircleCastEnv)
			throw MishapNoSpellCircle()

		val circle = env.impetus ?: throw MishapNoSpellCircle()
		val bounds = circle.executionState!!.bounds

		val entity = args.getEntity(0, argc)
		if (bounds.contains(entity.pos))
			throw OutsideCircleMishap()

		val destination = args.getVec3(1, argc)
		if (bounds.contains(destination))
			throw OutsideCircleMishap()

		return SpellAction.Result(Spell(entity, destination), 0, listOf())
	}

	private data class Spell(val entity: Entity, val destination: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			entity.teleport(destination.x, destination.y, destination.z)
		}
	}
}