package miyucomics.hexical.features.misc_actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getPositiveDoubleUnderInclusive
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.particle.VibrationParticleEffect
import net.minecraft.util.math.Vec3d
import net.minecraft.world.event.BlockPositionSource
import net.minecraft.world.event.EntityPositionSource
import net.minecraft.world.event.PositionSource

class OpVibrate : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val test = args[1]
		val to = when (test) {
			is EntityIota -> EntityPositionSource(test.entity, test.entity.getEyeHeight(test.entity.pose))
			is Vec3Iota -> BlockPositionSource(args.getBlockPos(1, argc))
			else -> throw MishapInvalidIota.of(args[0], 1, "entity_or_vector")
		}
		return SpellAction.Result(Spell(args.getVec3(0, argc), to, args.getPositiveDoubleUnderInclusive(2, 10.0, argc) * 20), MediaConstants.DUST_UNIT / 16, listOf())
	}

	private data class Spell(val from: Vec3d, val to: PositionSource, val duration: Double) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val particle = VibrationParticleEffect(to, duration.toInt())
			for (player in env.world.getPlayers())
				env.world.spawnParticles(player, particle, true, from.x, from.y, from.z, 1, 0.0, 0.0, 0.0, 1.0)
		}
	}
}