package miyucomics.hexical.features.transmutation

import at.petrak.hexcasting.api.utils.isMediaItem
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.features.blocks.MediaJarBlock.Companion.MAX_CAPACITY
import miyucomics.hexical.inits.HexicalRecipe
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.world.World

object TransmutationHelper {
	fun transmuteItem(world: World, stack: ItemStack, media: Long, insertMedia: (Long) -> Long, withdrawMedia: (Long) -> Boolean): TransmutationResult {
		if (isMediaItem(stack) && media < MAX_CAPACITY) {
			val mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(stack)!!
			val consumed = insertMedia(mediaHolder.media)
			mediaHolder.withdrawMedia(consumed, false)
			return TransmutationResult.AbsorbedMedia
		}

		val recipe = getRecipe(stack, world)
		if (recipe != null && media >= recipe.cost) {
			stack.decrement(1)
			withdrawMedia(recipe.cost)
			return TransmutationResult.TransmutedItems(recipe.output.map { it.copy() })
		}

		return TransmutationResult.Pass
	}

	private fun getRecipe(input: ItemStack, world: World): TransmutingRecipe? {
		world.recipeManager.listAllOfType(HexicalRecipe.TRANSMUTING_RECIPE).forEach { recipe ->
			if (recipe.matches(SimpleInventory(input), world))
				return recipe
		}
		return null
	}
}

sealed class TransmutationResult {
	object AbsorbedMedia : TransmutationResult()
	object Pass : TransmutationResult()
	data class TransmutedItems(val output: List<ItemStack>) : TransmutationResult()
}
