package miyucomics.hexical.casting.operators.grimoire

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.spell.mishaps.MishapOthersName
import miyucomics.hexical.casting.mishaps.GrimoireTooFullMishap
import miyucomics.hexical.items.GrimoireItem
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.item.ItemStack

class OpGrimoireWrite : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val pattern = args.getPattern(0, argc)
		val hex = args.getList(1, argc).toList()
		val trueName = MishapOthersName.getTrueNameFromDatum(args[1], ctx.caster)
		if (trueName != null)
			throw MishapOthersName(trueName)
		val (stack, hand) = ctx.getHeldItemToOperateOn { it.isOf(HexicalItems.GRIMOIRE_ITEM) }
		if (stack.item !is GrimoireItem)
			throw MishapBadOffhandItem.of(stack, hand, "grimoire")
		if (GrimoireItem.getPatternsInGrimoire(stack).size >= 16)
			throw GrimoireTooFullMishap()
		return Triple(Spell(pattern, hex, stack), 0, listOf())
	}

	private data class Spell(val pattern: HexPattern, val hex: List<Iota>, val stack: ItemStack) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			GrimoireItem.write(stack, pattern, hex)
		}
	}
}