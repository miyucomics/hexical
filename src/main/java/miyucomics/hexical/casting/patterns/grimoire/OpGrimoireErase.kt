package miyucomics.hexical.casting.patterns.grimoire

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPattern
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import miyucomics.hexical.items.GrimoireItem
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.item.ItemStack

class OpGrimoireErase : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val stack = ctx.caster.getStackInHand(ctx.otherHand)
		if (!stack.isOf(HexicalItems.GRIMOIRE_ITEM))
			throw MishapBadOffhandItem.of(stack, ctx.otherHand, "grimoire")
		val pattern = args.getPattern(0, argc)
		return Triple(Spell(stack, pattern), 0, listOf())
	}

	private data class Spell(val stack: ItemStack, val key: HexPattern) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			GrimoireItem.erase(stack, key)
		}
	}
}