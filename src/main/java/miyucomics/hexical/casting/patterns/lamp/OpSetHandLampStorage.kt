package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.NeedsSourceMishap

class OpSetHandLampStorage : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if ((env as CastingEnvironmentMinterface).getSpecializedSource() != SpecializedSource.HAND_LAMP)
			throw NeedsSourceMishap("hand_lamp")
		return SpellAction.Result(Spell(args[0]), 0, listOf())
	}

	private data class Spell(val iota: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			env.caster.getStackInHand(env.castingHand).orCreateNbt.putCompound("storage", HexIotaTypes.serialize(iota))
		}
	}
}