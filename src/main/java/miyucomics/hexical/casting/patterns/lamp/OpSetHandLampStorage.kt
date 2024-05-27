package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.NeedsSourceMishap
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.CastingContextMinterface

class OpSetHandLampStorage : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		if ((ctx as CastingContextMinterface).getSpecializedSource() != SpecializedSource.HAND_LAMP)
			throw NeedsSourceMishap("hand_lamp")
		return Triple(Spell(args[0]), 0, listOf())
	}

	private data class Spell(val iota: Iota) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			ctx.caster.getStackInHand(ctx.castingHand).orCreateNbt.putCompound("storage", HexIotaTypes.serialize(iota))
		}
	}
}