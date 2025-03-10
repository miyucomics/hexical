package miyucomics.hexical.registry

import at.petrak.hexcasting.common.lib.HexItems
import miyucomics.hexical.HexicalMain
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.potion.Potion
import net.minecraft.potion.Potions
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry

object HexicalEffects {
	@JvmField
	val WOOLEYED_EFFECT = WooleyedEffect()
	private val WOOLEYED_POTION: RegistryEntry<Potion> = Registry.registerReference(Registries.POTION, HexicalMain.id("wooleyed"), Potion(StatusEffectInstance(WOOLEYED_EFFECT, 12000, 0)))

	@JvmStatic
	fun init() {
		Registry.register(Registries.STATUS_EFFECT, HexicalMain.id("wooleyed"), WOOLEYED_EFFECT)
		FabricBrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Ingredient.ofItems(HexItems.CYPHER), WOOLEYED_POTION.comp_349())
	}
}

class WooleyedEffect : StatusEffect(StatusEffectCategory.BENEFICIAL, 0xff_a678f1.toInt())