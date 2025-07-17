package miyucomics.hexical.features.transmuting

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import net.minecraft.world.World

class TransmutingRecipe(private val id: Identifier, val input: Ingredient, val cost: Long, val output: List<ItemStack>) : Recipe<Inventory> {
	override fun getId() = this.id
	override fun getType() = Type.INSTANCE
	override fun fits(width: Int, height: Int) = false
	override fun getSerializer() = TransmutingSerializer.INSTANCE
	override fun getOutput(dynamicRegistryManager: DynamicRegistryManager) = output[0]
	override fun matches(inventory: Inventory, world: World) = input.test(inventory.getStack(0))
	override fun craft(inventory: Inventory, dynamicRegistryManager: DynamicRegistryManager): ItemStack = ItemStack.EMPTY

	class Type : RecipeType<TransmutingRecipe> {
		companion object {
			val INSTANCE = Type()
		}
	}
}