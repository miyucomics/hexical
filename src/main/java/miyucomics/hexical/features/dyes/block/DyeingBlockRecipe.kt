package miyucomics.hexical.features.dyes.block

import at.petrak.hexcasting.common.recipe.ingredient.StateIngredient
import miyucomics.hexical.features.dyes.DyeOption
import net.minecraft.block.BlockState
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import net.minecraft.world.World

class DyeingBlockRecipe(private val id: Identifier, val group: Identifier, val dye: DyeOption, val inputs: List<StateIngredient>, val output: BlockState) : Recipe<Inventory> {
	override fun getId() = this.id
	override fun getType() = Type.INSTANCE
	override fun fits(width: Int, height: Int) = false
	override fun getSerializer() = DyeingBlockSerializer.INSTANCE
	override fun matches(inventory: Inventory, world: World) = false
	override fun getOutput(dynamicRegistryManager: DynamicRegistryManager): ItemStack = ItemStack.EMPTY.copy()
	override fun craft(inventory: Inventory, dynamicRegistryManager: DynamicRegistryManager): ItemStack = ItemStack.EMPTY.copy()

	fun canDye(state: BlockState, wanted: DyeOption) = dye == wanted && inputs.firstOrNull { it.test(state) } != null

	class Type : RecipeType<DyeingBlockRecipe> {
		companion object {
			val INSTANCE = Type()
		}
	}
}