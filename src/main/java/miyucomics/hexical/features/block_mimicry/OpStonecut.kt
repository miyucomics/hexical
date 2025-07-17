package miyucomics.hexical.features.block_mimicry

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadItem
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexpose.iotas.getIdentifier
import net.minecraft.entity.ItemEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.StonecuttingRecipe
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.Registries

class OpStonecut : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val item = args.getItemEntity(0, argc)
		env.assertEntityInRange(item)

		val id = args.getIdentifier(1, argc)
		if (!Registries.ITEM.containsId(id))
			throw MishapInvalidIota.of(args[1], 0, "item_id")
		val type = Registries.ITEM.get(id)

		val recipe = env.world.recipeManager
			.getAllMatches(RecipeType.STONECUTTING, SimpleInventory(item.stack), env.world)
			.firstOrNull { it.getOutput(DynamicRegistryManager.EMPTY).isOf(type) }
			?: throw MishapBadItem.of(item, "target.stonecutting")

		return SpellAction.Result(Spell(recipe, item), MediaConstants.DUST_UNIT / 8, listOf())
	}

	private data class Spell(val recipe: StonecuttingRecipe, val item: ItemEntity) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val stack = item.stack
			val result = recipe.craft(SimpleInventory(stack), DynamicRegistryManager.EMPTY)
			result.count *= stack.count
			stack.count = 0
			val resultItem = ItemEntity(env.world, item.x, item.y, item.z, result)
			env.world.spawnEntity(resultItem)
		}
	}
}