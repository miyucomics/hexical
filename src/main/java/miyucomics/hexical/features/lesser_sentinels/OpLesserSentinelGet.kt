package miyucomics.hexical.features.lesser_sentinels

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import net.minecraft.server.network.ServerPlayerEntity

class OpLesserSentinelGet : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()
		val caster = env.castingEntity as ServerPlayerEntity
		return caster.currentLesserSentinels.map { Vec3Iota(it) }.asActionResult
	}
}