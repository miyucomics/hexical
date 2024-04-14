package miyucomics.hexical.casting.operators.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.iota.IdentifierIota
import miyucomics.hexical.iota.getItemStack
import net.minecraft.item.PotionItem
import net.minecraft.potion.PotionUtil
import net.minecraft.util.registry.Registry

class OpGetItemEffects : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val stack = args.getItemStack(0, argc)
		if (stack.item !is PotionItem && !stack.item.isFood)
			throw MishapInvalidIota.of(args[0], 0, "potion_food")

		if (stack.item is PotionItem) {
			val effects = mutableListOf<IdentifierIota>()
			for (effect in PotionUtil.getPotionEffects(stack))
				effects.add(IdentifierIota(Registry.STATUS_EFFECT.getId(effect.effectType)!!))
			return listOf(ListIota(effects.toList()))
		}

		val effects = mutableListOf<IdentifierIota>()
		for (statusEffect in stack.item.foodComponent!!.statusEffects)
			effects.add(IdentifierIota(Registry.STATUS_EFFECT.getId(statusEffect.first.effectType)!!))
		return listOf(ListIota(effects.toList()))
	}
}