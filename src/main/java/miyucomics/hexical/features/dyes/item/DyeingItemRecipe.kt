package miyucomics.hexical.features.dyes.item

import miyucomics.hexical.features.dyes.DyeOption
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import net.minecraft.world.World

class DyeingItemRecipe(private val id: Identifier, val group: Identifier, val dye: DyeOption, val inputs: Ingredient, val output: ItemStack) : Recipe<Inventory> {
	override fun getId() = this.id
	override fun getType() = Type.INSTANCE
	override fun fits(width: Int, height: Int) = false
	override fun getSerializer() = DyeingItemSerializer.INSTANCE
	override fun matches(inventory: Inventory, world: World) = false
	override fun getOutput(dynamicRegistryManager: DynamicRegistryManager): ItemStack = ItemStack.EMPTY.copy()
	override fun craft(inventory: Inventory, dynamicRegistryManager: DynamicRegistryManager): ItemStack = ItemStack.EMPTY.copy()

	fun canDye(stack: ItemStack, wanted: DyeOption) = dye == wanted && inputs.test(stack)

	class Type : RecipeType<DyeingItemRecipe> {
		companion object {
			val INSTANCE = Type()
		}
	}
}