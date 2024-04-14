package miyucomics.hexical.casting.operators.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.iota.getIdentifier
import miyucomics.hexical.iota.getItemStack
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.util.registry.Registry

class OpGetEnchantmentStrength : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val stack = args.getItemStack(0, argc)
		val enchantment = args.getIdentifier(1, argc)
		if (!Registry.ENCHANTMENT.containsId(enchantment))
			throw MishapInvalidIota.of(args[0], 0, "enchantment")
		return listOf(DoubleIota(EnchantmentHelper.getLevel(Registry.ENCHANTMENT.get(enchantment), stack).toDouble()))
	}
}