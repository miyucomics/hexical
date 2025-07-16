package miyucomics.hexical.casting.actions.telepathy

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexical.features.peripherals.serverKeybindActive
import miyucomics.hexical.features.peripherals.serverKeybindDuration
import net.minecraft.server.network.ServerPlayerEntity

class OpGetKeybind(private val key: String) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val caster = env.castingEntity
		if (caster !is ServerPlayerEntity)
			return listOf(NullIota())
		if (!caster.serverKeybindActive().getOrDefault(key, false))
			return (-1).asActionResult
		return caster.serverKeybindDuration().getOrDefault(key, 0.0).asActionResult
	}
}