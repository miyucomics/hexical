package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getItemEntity
import at.petrak.hexcasting.api.spell.iota.*
import miyucomics.hexical.iota.IdentifierIota
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.registry.Registry

class OpGetItemStackData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val stack = args.getItemStack(0, argc)
		return listOf(
			when (mode) {
				0 -> DoubleIota(stack.count.toDouble())
				1 -> DoubleIota(stack.maxCount.toDouble())
				2 -> DoubleIota(stack.damage.toDouble())
				3 -> DoubleIota(stack.maxDamage.toDouble())
				4 -> BooleanIota(stack.isFood)
				5 -> {
					var data = stack.enchantments
					if (stack.isOf(Items.ENCHANTED_BOOK))
						data = EnchantedBookItem.getEnchantmentNbt(stack)
					val enchantments = mutableListOf<IdentifierIota>()
					for ((enchantment, _) in EnchantmentHelper.fromNbt(data))
						enchantments.add(IdentifierIota(Registry.ENCHANTMENT.getId(enchantment)!!))
					ListIota(enchantments.toList())
				}
				else -> NullIota()
			}
		)
	}
}

fun List<Iota>.getItemStack(idx: Int, argc: Int = 0): ItemStack = getItemEntity(idx, argc).stack