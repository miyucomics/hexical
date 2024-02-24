package miyucomics.hexical.casting.patterns.spells

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.mishaps.MishapNeedActiveMasterLamp
import miyucomics.hexical.persistent_state.StateHandler
import miyucomics.hexical.registry.HexicalItems

class OpTerminateLamp : SpellAction {
	override val argc = 0

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val state = StateHandler.getPlayerState(ctx.caster)
		if (!state.active)
			throw MishapNeedActiveMasterLamp()
		return Triple(Spell(), 0, listOf())
	}

	private class Spell : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val state = StateHandler.getPlayerState(ctx.caster)
			state.active = false
			for (slot in ctx.caster.inventory.main) {
				if (slot.item == HexicalItems.MASTER_LAMP_ITEM)
					slot.orCreateNbt.putBoolean("active", false)
			}
		}
	}
}