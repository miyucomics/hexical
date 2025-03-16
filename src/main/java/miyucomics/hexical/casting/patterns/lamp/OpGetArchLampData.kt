package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexical.casting.environments.TurretLampCastEnv
import miyucomics.hexical.casting.mishaps.NeedsArchGenieLampMishap
import miyucomics.hexical.data.ArchLampState
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import miyucomics.hexical.items.hasActiveArchLamp
import net.minecraft.server.network.ServerPlayerEntity

class OpGetArchLampData(private val process: (CastingEnvironment, ArchLampState) -> List<Iota>) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env is TurretLampCastEnv)
			return process(env, env.archLampState)

		val caster = env.castingEntity
		if (caster !is ServerPlayerEntity)
			return listOf(NullIota())
		if (!hasActiveArchLamp(caster))
			throw NeedsArchGenieLampMishap()
		return process(env, (caster as PlayerEntityMinterface).getArchLampState())
	}
}