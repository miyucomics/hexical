package miyucomics.hexical.casting.operators.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.iota.getIdentifier
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.util.registry.Registry

class OpGetEnchantmentStrength : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val stack = args.getItemStack(0, argc)
		val id = args.getIdentifier(1, argc)
		if (!Registry.ENCHANTMENT.containsId(id))
			throw MishapInvalidIota.of(args[1], 1, "enchantment")
		val data = EnchantmentHelper.get(stack)
		val enchantment = Registry.ENCHANTMENT.get(id)
		if (!data.containsKey(enchantment))
			return listOf(DoubleIota(0.0))
		return listOf(DoubleIota(data.getValue(enchantment).toDouble()))
	}
}