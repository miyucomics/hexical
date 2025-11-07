package miyucomics.hexical.features.periwinkle

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.periwinkle.enchantments.AbstractLeiEnchantment
import miyucomics.hexical.features.periwinkle.enchantments.AnalyzingEnchantment
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EquipmentSlot
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object LeiEnchantmentsHook : InitHook() {
	override fun init() {
		Registry.register(Registries.ENCHANTMENT, HexicalMain.id("analyzing"), AnalyzingEnchantment)

		ModifyItemAttributeModifiersCallback.EVENT.register { stack, slot, original ->
			EnchantmentHelper.get(stack).forEach { (enchantment, level) ->
				if (enchantment is AbstractLeiEnchantment && level > 0 && slot == EquipmentSlot.HEAD)
					enchantment.modifyAttributes(original)
			}
		}
	}
}