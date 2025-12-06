package miyucomics.hexical.features.circle

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation
import at.petrak.hexcasting.api.casting.mishaps.circle.MishapNoSpellCircle
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.mod.HexConfig
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d

object OpDisplace : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env !is CircleCastEnv)
			throw MishapNoSpellCircle()

		val circle = env.impetus ?: throw MishapNoSpellCircle()
		val bounds = circle.executionState!!.bounds

		val entity = args.getEntity(0, argc)
		if (!bounds.contains(entity.pos))
			throw OutsideCircleMishap()

		val destination = args.getVec3(1, argc)
		if (!HexConfig.server().canTeleportInThisDimension(env.world.registryKey))
			throw MishapBadLocation(destination, "bad_dimension")
		if (!bounds.contains(destination))
			throw OutsideCircleMishap()

		return SpellAction.Result(Spell(entity, destination), MediaConstants.DUST_UNIT / 2, listOf(ParticleSpray.burst(destination, 1.0)))
	}

	private data class Spell(val entity: Entity, val destination: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			entity.teleport(destination.x, destination.y, destination.z)
		}
	}
}