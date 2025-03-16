package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.hexical.casting.environments.TurretLampCastEnv
import miyucomics.hexical.casting.mishaps.NeedsArchGenieLampMishap
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import miyucomics.hexical.items.hasActiveArchLamp
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.server.network.ServerPlayerEntity

class OpSetArchLampStorage : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val iota = args[0]
		CastingUtils.assertNoTruename(iota, env)

		if (env is TurretLampCastEnv) {
			env.archLampState.storage = IotaType.serialize(iota)
			return emptyList()
		}

		val caster = env.castingEntity
		if (caster !is ServerPlayerEntity)
			throw MishapBadCaster()
		if (!hasActiveArchLamp(caster))
			throw NeedsArchGenieLampMishap()
		(env.castingEntity!! as PlayerEntityMinterface).getArchLampState().storage = IotaType.serialize(iota)

		return emptyList()
	}
}