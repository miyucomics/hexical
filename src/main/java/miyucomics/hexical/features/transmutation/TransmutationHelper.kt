package miyucomics.hexical.features.transmutation

import at.petrak.hexcasting.api.utils.isMediaItem
import at.petrak.hexcasting.common.lib.HexItems
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.features.media_jar.MediaJarBlock
import miyucomics.hexical.features.media_jar.MediaJarItem
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.inits.HexicalRecipe
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import kotlin.math.min

object TransmutationHelper {
	fun transmuteItem(world: World, stack: ItemStack, media: Long, insertMedia: (Long) -> Long, withdrawMedia: (Long) -> Boolean): TransmutationResult {
		if (stack.isOf(HexItems.BATTERY)) {
			val mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(stack)!!
			val given = min(mediaHolder.maxMedia - mediaHolder.media, media)
			mediaHolder.insertMedia(given, false)
			withdrawMedia(given)
			return TransmutationResult.RefilledHolder
		}

		if (stack.isOf(HexicalBlocks.MEDIA_JAR_ITEM) && stack.hasNbt() && stack.nbt!!.contains("BlockEntityTag")) {
			val jarData = stack.nbt!!.getCompound("BlockEntityTag")
			val given = min(MediaJarBlock.MAX_CAPACITY - MediaJarItem.getMedia(jarData), media)
			MediaJarItem.insertMedia(jarData, given)
			withdrawMedia(given)
			return TransmutationResult.RefilledHolder
		}

		if (isMediaItem(stack) && media < MediaJarBlock.MAX_CAPACITY) {
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
	object RefilledHolder : TransmutationResult()
	data class TransmutedItems(val output: List<ItemStack>) : TransmutationResult()
}
