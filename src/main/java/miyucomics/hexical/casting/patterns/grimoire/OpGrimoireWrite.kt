package miyucomics.hexical.casting.patterns.grimoire

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.items.GrimoireItem
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.item.ItemStack

class OpGrimoireWrite : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = env.caster.getStackInHand(env.otherHand)
		if (!stack.isOf(HexicalItems.GRIMOIRE_ITEM))
			throw MishapBadOffhandItem.of(stack, env.otherHand, "grimoire")
		val key = args.getPattern(0, argc)
		val expansion = args.getList(1, argc).toList()
		CastingUtils.assertNoTruename(args[1], env.caster)
		return SpellAction.Result(Spell(stack, key, expansion), 0, listOf())
	}

	private data class Spell(val stack: ItemStack, val key: HexPattern, val expansion: List<Iota>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			GrimoireItem.write(stack, key, expansion)
		}
	}
}