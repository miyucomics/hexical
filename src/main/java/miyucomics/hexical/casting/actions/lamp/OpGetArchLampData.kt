package miyucomics.hexical.casting.actions.lamp

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexical.casting.mishaps.NeedsArchLampMishap
import miyucomics.hexical.features.lamps.ArchLampField
import miyucomics.hexical.features.lamps.getArchLampField
import miyucomics.hexical.features.lamps.hasActiveArchLamp
import net.minecraft.server.network.ServerPlayerEntity

class OpGetArchLampData(private val process: (CastingEnvironment, ArchLampField) -> List<Iota>) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val caster = env.castingEntity
		if (caster !is ServerPlayerEntity)
			return listOf(NullIota())
		if (!hasActiveArchLamp(caster))
			throw NeedsArchLampMishap()
		return process(env, caster.getArchLampField())
	}
}