package miyucomics.hexical.features.block_mimicry

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadItem
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.ItemEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.recipe.AbstractCookingRecipe
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.DynamicRegistryManager

class OpCook<T : AbstractCookingRecipe>(private val recipeType: RecipeType<T>, private val mishapMessage: String) : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val item = args.getItemEntity(0, argc)
		env.assertEntityInRange(item)

		val recipe = env.world.recipeManager
			.getAllMatches(recipeType, SimpleInventory(item.stack), env.world)
			.firstOrNull()
			?: throw MishapBadItem.of(item, mishapMessage)

		return SpellAction.Result(Spell(recipe, item), MediaConstants.DUST_UNIT * recipe.cookTime / 200 * item.stack.count, listOf())
	}

	private data class Spell(val recipe: AbstractCookingRecipe, val item: ItemEntity) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val input = item.stack.copyAndEmpty()
			val result = recipe.craft(SimpleInventory(input), DynamicRegistryManager.EMPTY)
			result.count *= input.count
			env.world.spawnEntity(ItemEntity(env.world, item.x, item.y, item.z, result))
		}
	}
}