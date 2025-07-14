package miyucomics.hexical.features.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import miyucomics.hexical.HexicalMain
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import java.io.InputStreamReader

object PrestidigitationBlockTransformations {
	private val map: MutableMap<Block, BlockState> = mutableMapOf()

	fun init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
			override fun getFabricId() = HexicalMain.id("prestidigitation_block_transformations")
			override fun reload(manager: ResourceManager) {
				map.clear()
				manager.findResources("prestidigitation") { it.path.endsWith("block_transformations.json") }.keys.forEach { path ->
					(JsonParser.parseReader(InputStreamReader(manager.getResource(path).get().inputStream, "UTF-8")) as JsonObject).entrySet().forEach {
						map[Registries.BLOCK.get(Identifier(it.key))] = Registries.BLOCK.get(Identifier(it.value.asString)).defaultState
					}
				}
			}
		})

		Registry.register(PrestidigitationData.PRESTIDIGITATION_HANDLER, HexicalMain.id("transform_block"), object : PrestidigitationHandler {
			override fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean {
				val state = env.world.getBlockState(position)
				if (state.block !in map)
					return false
				env.world.setBlockState(position, map[state.block])
				return true
			}
		})
	}
}