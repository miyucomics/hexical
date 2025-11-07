package miyucomics.hexical.features.periwinkle.enchantments

import com.google.common.collect.Multimap
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.ItemStack

abstract class AbstractLeiEnchantment : Enchantment(Rarity.COMMON, EnchantmentTarget.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD)) {
	override fun isAcceptableItem(stack: ItemStack) = stack.isOf(HexicalItems.LEI)
	open fun modifyAttributes(original: Multimap<EntityAttribute, EntityAttributeModifier>) {}
}