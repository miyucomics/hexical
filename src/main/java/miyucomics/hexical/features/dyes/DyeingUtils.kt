package miyucomics.hexical.features.dyes

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.dyes.block.DyeingBlockRecipe
import miyucomics.hexical.features.dyes.block.DyeingBlockSerializer
import miyucomics.hexical.features.dyes.item.DyeingItemRecipe
import miyucomics.hexical.features.dyes.item.DyeingItemSerializer
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import java.io.InputStream
import java.io.InputStreamReader

object DyeingUtils : InitHook() {
	val DYEING_BLOCK_RECIPE: RecipeType<DyeingBlockRecipe> = Registry.register(Registries.RECIPE_TYPE, HexicalMain.id("dye_block"), DyeingBlockRecipe.Type.INSTANCE)
	val DYEING_ITEM_RECIPE: RecipeType<DyeingItemRecipe> = Registry.register(Registries.RECIPE_TYPE, HexicalMain.id("dye_item"), DyeingItemRecipe.Type.INSTANCE)

	val flatBlockColorLookup = mutableMapOf<Block, DyeOption>()
	val flatItemColorLookup = mutableMapOf<Item, DyeOption>()

	fun getDye(block: Block) = flatBlockColorLookup[block]
	fun getDye(item: Item) = flatItemColorLookup[item]
	fun getRecipe(world: ServerWorld, state: BlockState, dye: DyeOption): DyeingBlockRecipe? = world.recipeManager.listAllOfType(DYEING_BLOCK_RECIPE).firstOrNull { it.canDye(state, dye) }
	fun getRecipe(world: ServerWorld, stack: ItemStack, dye: DyeOption): DyeingItemRecipe? = world.recipeManager.listAllOfType(DYEING_ITEM_RECIPE).firstOrNull { it.canDye(stack, dye) }

	override fun init() {
		Registry.register(Registries.RECIPE_SERIALIZER, HexicalMain.id("dye_block"), DyeingBlockSerializer.INSTANCE)
		Registry.register(Registries.RECIPE_SERIALIZER, HexicalMain.id("dye_item"), DyeingItemSerializer.INSTANCE)

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
			override fun getFabricId() = HexicalMain.id("dyes")
			override fun reload(manager: ResourceManager) {
				flatBlockColorLookup.clear()
				flatItemColorLookup.clear()
				manager.findResources("dyes/block") { path -> path.path.endsWith(".json") }.keys.forEach { load(manager.getResource(it).get().inputStream, Registries.BLOCK, flatBlockColorLookup) }
				manager.findResources("dyes/item") { path -> path.path.endsWith(".json") }.keys.forEach { load(manager.getResource(it).get().inputStream, Registries.ITEM, flatItemColorLookup) }
			}
		})
	}

	private fun <T : Any> load(stream: InputStream, registry: Registry<T>, map: MutableMap<T, DyeOption>) {
		(JsonParser.parseReader(InputStreamReader(stream, "UTF-8")) as JsonObject).asMap().forEach { (id, dye) ->
			map[registry.get(Identifier(id))!!] = enumValues<DyeOption>()[dye.asInt]
		}
	}
}