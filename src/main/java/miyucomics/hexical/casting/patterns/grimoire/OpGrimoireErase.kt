package miyucomics.hexical.casting.patterns.grimoire

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.items.GrimoireItem
import net.minecraft.item.ItemStack

class OpGrimoireErase : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = env.caster.getStackInHand(env.otherHand)
		if (!stack.isOf(HexicalItems.GRIMOIRE_ITEM))
			throw MishapBadOffhandItem.of(stack, env.otherHand, "grimoire")
		val pattern = args.getPattern(0, argc)
		return SpellAction.Result(Spell(stack, pattern), 0, listOf())
	}

	private data class Spell(val stack: ItemStack, val key: HexPattern) : RenderedSpell {
		override fun cast(ctx: CastingEnvironment) {
			GrimoireItem.erase(stack, key)
		}
	}
}