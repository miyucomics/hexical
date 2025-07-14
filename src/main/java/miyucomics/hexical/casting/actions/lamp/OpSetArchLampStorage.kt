package miyucomics.hexical.casting.actions.lamp

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.hexical.casting.mishaps.NeedsArchGenieLampMishap
import miyucomics.hexical.features.items.hasActiveArchLamp
import miyucomics.hexical.features.player.fields.getArchLampField
import miyucomics.hexical.misc.CastingUtils
import net.minecraft.server.network.ServerPlayerEntity

class OpSetArchLampStorage : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val iota = args[0]
		CastingUtils.assertNoTruename(iota, env)

		val caster = env.castingEntity
		if (caster !is ServerPlayerEntity)
			throw MishapBadCaster()
		if (!hasActiveArchLamp(caster))
			throw NeedsArchGenieLampMishap()
		caster.getArchLampField().storage = IotaType.serialize(iota)

		return emptyList()
	}
}