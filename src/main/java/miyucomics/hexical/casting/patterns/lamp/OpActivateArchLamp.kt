package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.misc.DiscoveryHandlers
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.item.ItemStack

class OpActivateArchLamp : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		for (stack in DiscoveryHandlers.collectItemSlots(ctx)) {
			if (stack.isEmpty)
				continue
			if (!stack.isOf(HexicalItems.ARCH_LAMP_ITEM))
				continue
			if (!stack.orCreateNbt.getBoolean("active"))
				return Triple(Spell(stack), MediaConstants.DUST_UNIT, listOf())
		}
		return Triple(Spell(null), 0, listOf())
	}

	private class Spell(val lampToActivate: ItemStack?) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			CastingUtils.getActiveArchLamp(ctx.caster)?.orCreateNbt?.putBoolean("active", false)
			if (lampToActivate == null)
				return
			lampToActivate.orCreateNbt.putBoolean("active", true)
		}
	}
}