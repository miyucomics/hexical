package miyucomics.hexical.features.periwinkle

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.inits.InitHook
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Items
import net.minecraft.potion.Potion
import net.minecraft.potion.Potions
import net.minecraft.recipe.BrewingRecipeRegistry
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object WooleyedEffectRegister : InitHook() {
	private val WOOLEYED_POTION = Potion(StatusEffectInstance(WooleyedEffect, 12000, 0))
	private val LONG_WOOLEYED_POTION = Potion(StatusEffectInstance(WooleyedEffect, 48000, 0))
	private val STRONG_WOOLEYED_POTION = Potion(StatusEffectInstance(WooleyedEffect, 6000, 1))

	override fun init() {
		Registry.register(Registries.STATUS_EFFECT, HexicalMain.id("wooleyed"), WooleyedEffect)
		Registry.registerReference(Registries.POTION, HexicalMain.id("wooleyed"), WOOLEYED_POTION)
		Registry.registerReference(Registries.POTION, HexicalMain.id("long_wooleyed"), LONG_WOOLEYED_POTION)
		Registry.registerReference(Registries.POTION, HexicalMain.id("strong_wooleyed"), STRONG_WOOLEYED_POTION)

		BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, HexicalBlocks.PERIWINKLE_FLOWER_ITEM, WOOLEYED_POTION)
		BrewingRecipeRegistry.registerPotionRecipe(WOOLEYED_POTION, Items.REDSTONE, LONG_WOOLEYED_POTION)
		BrewingRecipeRegistry.registerPotionRecipe(WOOLEYED_POTION, Items.GLOWSTONE_DUST, STRONG_WOOLEYED_POTION)
	}
}

object WooleyedEffect : StatusEffect(StatusEffectCategory.BENEFICIAL, 0xff_a678f1.toInt())