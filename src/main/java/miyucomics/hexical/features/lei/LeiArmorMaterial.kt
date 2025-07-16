package miyucomics.hexical.features.lei

import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents

class LeiArmorMaterial : ArmorMaterial {
	override fun getDurability(type: ArmorItem.Type) = 0
	override fun getProtection(type: ArmorItem.Type) = 0
	override fun getEnchantability() = 100
	override fun getEquipSound(): SoundEvent = SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME
	override fun getRepairIngredient(): Ingredient = Ingredient.EMPTY
	override fun getName() = "lei"
	override fun getToughness() = 0f
	override fun getKnockbackResistance() = 0f

	companion object {
		val INSTANCE: LeiArmorMaterial = LeiArmorMaterial()
	}
}