package miyucomics.hexical.casting.patterns.scrying

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iotas.getIdentifier
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.registry.Registries

class OpGetEnchantmentStrength : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val item = args.getItemEntity(0, argc)
		env.assertEntityInRange(item)
		val enchantment = Registries.ENCHANTMENT.get(args.getIdentifier(1, argc)) ?: throw MishapInvalidIota.of(args[1], 0, "enchantment_id")
		val data = EnchantmentHelper.get(item.stack)
		if (!data.containsKey(enchantment))
			return (0).asActionResult
		return data.getValue(enchantment).asActionResult
	}
}