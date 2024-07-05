package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.misc.DiscoveryHandlers
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.registry.HexicalItems
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

	private class Spell(val stack: ItemStack?) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			for (slot in ctx.caster.inventory.main)
				if (slot.item == HexicalItems.ARCH_LAMP_ITEM)
					slot.orCreateNbt.putBoolean("active", false)
			if (stack == null)
				return
			stack.orCreateNbt.putBoolean("active", true)
		}
	}
}