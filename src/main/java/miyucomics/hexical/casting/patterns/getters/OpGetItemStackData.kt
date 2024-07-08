package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getItemEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.casting.iota.IdentifierIota
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.registry.Registry

class OpGetItemStackData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val stack = args.getItemStack(0, argc)
		return when (mode) {
			0 -> stack.count.asActionResult
			1 -> stack.damage.asActionResult
			2 -> {
				var data = stack.enchantments
				if (stack.isOf(Items.ENCHANTED_BOOK))
					data = EnchantedBookItem.getEnchantmentNbt(stack)
				val enchantments = mutableListOf<IdentifierIota>()
				for ((enchantment, _) in EnchantmentHelper.fromNbt(data))
					enchantments.add(IdentifierIota(Registry.ENCHANTMENT.getId(enchantment)!!))
				enchantments.asActionResult
			}
			else -> throw IllegalStateException()
		}
	}
}

fun List<Iota>.getItemStack(idx: Int, argc: Int = 0): ItemStack = getItemEntity(idx, argc).stack