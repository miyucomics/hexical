package miyucomics.hexical.datagen.providers.dyeing

import at.petrak.hexcasting.common.recipe.ingredient.StateIngredientHelper
import com.google.gson.JsonObject
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.dyes.DyeOption
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

object DyeingProvider {
	val recipeJsons = mutableListOf<DyeRecipeJsonGenerator>()
	val recipePages = mutableListOf<JsonObject>()

	fun init() {
		blockFamilies.forEach { pattern ->
			val blocks = DyeOption.entries.mapNotNull { dye -> resolvePattern(Registries.BLOCK, pattern, dye)?.let { dye to it } }.toMap()
			blocks.forEach { (dye, block) ->
				val name = Registries.BLOCK.getId(block).path
				recipeJsons.add(DyeBlockGenerator(HexicalMain.id("dyeing/block/$name"), dye, blocks.values.map(StateIngredientHelper::of), block))
			}
		}

		itemFamilies.forEach { pattern ->
			val items = DyeOption.entries.mapNotNull { dye -> resolvePattern(Registries.ITEM, pattern, dye)?.let { dye to it } }.toMap()
			items.forEach { (dye, item) ->
				val name = Registries.ITEM.getId(item).path
				recipeJsons.add(DyeItemGenerator(HexicalMain.id("dyeing/item/$name"), dye, Ingredient.ofStacks(items.values.map(::ItemStack).stream()), ItemStack(item)))
			}
		}
	}

	val blockFamilies = listOf("minecraft:{color}bed", "minecraft:{color}candle", "minecraft:{color}candle_cake", "minecraft:{color}carpet", "minecraft:{color}concrete", "minecraft:{color}concrete_powder", "minecraft:{color}glazed_terracotta", "minecraft:{color}sand", "minecraft:{color}sandstone", "minecraft:cut_{color}sandstone", "minecraft:smooth_{color}sandstone", "minecraft:chiseled_{color}sandstone", "minecraft:{color}sandstone_slab", "minecraft:cut_{color}sandstone_slab", "minecraft:smooth_{color}sandstone_slab", "minecraft:{color}sandstone_stairs", "minecraft:smooth_{color}sandstone_stairs", "minecraft:{color}sandstone_walls", "minecraft:{color}shulker_box", "minecraft:{color}stained_glass", "minecraft:{color}stained_glass_pane", "minecraft:{color}terracotta", "minecraft:{color}tulip", "minecraft:{color}wool")
	val itemFamilies = listOf("minecraft:{color}dye", "hexcasting:dye_colorizer_{color}")

	private fun <T : Any> resolvePattern(registry: Registry<T>, pattern: String, dye: DyeOption) = listOfNotNull(
		registry.get(RegistryKey.of<T>(registry.key, Identifier(pattern.replace("{color}", dye.replacement)))),
		registry.get(RegistryKey.of<T>(registry.key, Identifier(pattern.replace("{color}", dye.replacement + "_"))))
	).firstOrNull()
}