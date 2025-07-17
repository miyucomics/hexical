package miyucomics.hexical.features.transmuting

import at.petrak.hexcasting.api.utils.isMediaItem
import at.petrak.hexcasting.common.lib.HexItems
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.media_jar.MediaJarBlock
import miyucomics.hexical.features.media_jar.MediaJarItem
import miyucomics.hexical.features.transmuting.TransmutingRecipe.Type
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.inits.InitHook
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.World
import kotlin.math.min

object TransmutingHelper : InitHook() {
	lateinit var TRANSMUTING_RECIPE: RecipeType<TransmutingRecipe>

	override fun init() {
		Registry.register(Registries.RECIPE_SERIALIZER, HexicalMain.id("transmuting"), TransmutingSerializer.INSTANCE)
		TRANSMUTING_RECIPE = Registry.register(Registries.RECIPE_TYPE, HexicalMain.id("transmuting"), Type.INSTANCE)
	}

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
		world.recipeManager.listAllOfType(TRANSMUTING_RECIPE).forEach { recipe ->
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
