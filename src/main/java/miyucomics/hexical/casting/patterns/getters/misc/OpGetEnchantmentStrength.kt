package miyucomics.hexical.casting.patterns.getters.misc

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.getIdentifier
import miyucomics.hexical.casting.patterns.getters.getItemStack
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.util.registry.Registry

class OpGetEnchantmentStrength : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val stack = args.getItemStack(0, argc)
		val enchantment = Registry.ENCHANTMENT.get(args.getIdentifier(1, argc))?: throw MishapInvalidIota.of(args[1], 1, "enchantment_id")
		val data = EnchantmentHelper.get(stack)
		if (!data.containsKey(enchantment))
			return (0).asActionResult
		return data.getValue(enchantment).asActionResult
	}
}