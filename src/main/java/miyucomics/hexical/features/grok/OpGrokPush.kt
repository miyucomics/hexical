package miyucomics.hexical.features.grok

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.hexical.misc.CastingUtils
import net.minecraft.server.network.ServerPlayerEntity

object OpGrokPush : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is PlayerBasedCastEnv)
			throw MishapBadCaster()
		val iota = args[0]
		CastingUtils.assertNoTruename(iota, env)
		CastingUtils.giveIota(env.caster as ServerPlayerEntity, iota)
		return emptyList()
	}
}