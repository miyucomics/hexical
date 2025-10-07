package miyucomics.hexical.features.flora

import miyucomics.hexical.features.dyes.block.DyeingBlockSerializer
import net.minecraft.block.BlockState
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import net.minecraft.world.World

class ConjureFloraRecipe(private val id: Identifier, val block: BlockState, val cost: Long) : Recipe<Inventory> {
	override fun getId() = this.id
	override fun getType() = Type.INSTANCE
	override fun fits(width: Int, height: Int) = false
	override fun getSerializer() = DyeingBlockSerializer.INSTANCE
	override fun matches(inventory: Inventory, world: World) = false
	override fun getOutput(dynamicRegistryManager: DynamicRegistryManager): ItemStack = ItemStack.EMPTY.copy()
	override fun craft(inventory: Inventory, dynamicRegistryManager: DynamicRegistryManager): ItemStack = ItemStack.EMPTY.copy()

	class Type : RecipeType<ConjureFloraRecipe> {
		companion object {
			val INSTANCE = Type()
		}
	}
}