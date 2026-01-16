package miyucomics.hexical.features.prestidigitation.handlers

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.prestidigitation.interfaces.PrestidigitationHandler
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.Registries
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import java.io.InputStreamReader

object PrestidigitationBlockTransformations {
	private val map: MutableMap<Block, BlockState> = mutableMapOf()

	fun init(register: (PrestidigitationHandler) -> Unit) {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
			override fun getFabricId() = HexicalMain.id("prestidigitation_block_transformations")
			override fun reload(manager: ResourceManager) {
				map.clear()
				manager.findResources("prestidigitation") { it.path.endsWith("block_transformations.json") }.keys.forEach { path ->
					(JsonParser.parseReader(InputStreamReader(manager.getResource(path).get().inputStream, "UTF-8")) as JsonObject).entrySet().forEach {
						val from = Identifier(it.key)
						val to = Identifier(it.value.asString)
						if (Registries.BLOCK.containsId(from) || Registries.BLOCK.containsId(to))
							map[Registries.BLOCK.get(from)] = Registries.BLOCK.get(to).defaultState
					}
				}
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = map.containsKey(getBlock(env, pos))
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				setBlockState(env, pos, map[getBlock(env, pos)]!!)
			}
		})
	}
}