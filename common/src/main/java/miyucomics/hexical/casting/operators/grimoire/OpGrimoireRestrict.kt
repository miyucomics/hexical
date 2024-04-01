package miyucomics.hexical.casting.operators.grimoire

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import miyucomics.hexical.casting.mishaps.GrimoireAccessDeniedMishap
import miyucomics.hexical.items.GrimoireItem
import net.minecraft.item.ItemStack

class OpGrimoireRestrict : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val pattern = args.getPattern(0, argc)
		val uses = args.getPositiveInt(1, argc)
		val (stack, hand) = ctx.getHeldItemToOperateOn { it.item is GrimoireItem }
		if (stack.item !is GrimoireItem)
			throw MishapBadOffhandItem.of(stack, hand, "grimoire")
		val currentUses = GrimoireItem.getUses(stack, pattern) ?: return Triple(Spell(pattern, stack, uses), 0, listOf())
		if (uses > currentUses)
			throw GrimoireAccessDeniedMishap()
		return Triple(Spell(pattern, stack, uses), 0, listOf())
	}

	private data class Spell(val pattern: HexPattern, val stack: ItemStack, val uses: Int) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			GrimoireItem.restrict(stack, pattern, uses)
		}
	}
}