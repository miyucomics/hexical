package miyucomics.hexical.casting.patterns.grimoire

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import miyucomics.hexical.items.GrimoireItem
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.item.ItemStack

class OpGrimoireWrite : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val stack = ctx.caster.getStackInHand(ctx.otherHand)
		if (!stack.isOf(HexicalItems.GRIMOIRE_ITEM))
			throw MishapBadOffhandItem.of(stack, ctx.otherHand, "grimoire")
		val key = args.getPattern(0, argc)
		val expansion = args.getList(1, argc).toList()
		CastingUtils.assertNoTruename(args[1], ctx.caster)
		return Triple(Spell(stack, key, expansion), 0, listOf())
	}

	private data class Spell(val stack: ItemStack, val key: HexPattern, val expansion: List<Iota>) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			GrimoireItem.write(stack, key, expansion)
		}
	}
}