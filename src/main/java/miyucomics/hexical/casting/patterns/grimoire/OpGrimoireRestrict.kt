package miyucomics.hexical.casting.patterns.grimoire

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import miyucomics.hexical.casting.mishaps.GrimoireAccessDeniedMishap
import miyucomics.hexical.items.GrimoireItem
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.item.ItemStack

class OpGrimoireRestrict : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val stack = ctx.caster.getStackInHand(ctx.otherHand)
		if (!stack.isOf(HexicalItems.GRIMOIRE_ITEM))
			throw MishapBadOffhandItem.of(stack, ctx.otherHand, "grimoire")
		val key = args.getPattern(0, argc)
		val uses = args.getPositiveInt(1, argc)
		if (uses > (GrimoireItem.getUses(stack, key) ?: return Triple(Spell(stack, key, uses), 0, listOf())))
			throw GrimoireAccessDeniedMishap()
		return Triple(Spell(stack, key, uses), 0, listOf())
	}

	private data class Spell(val stack: ItemStack, val key: HexPattern, val uses: Int) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			GrimoireItem.restrict(stack, key, uses)
		}
	}
}