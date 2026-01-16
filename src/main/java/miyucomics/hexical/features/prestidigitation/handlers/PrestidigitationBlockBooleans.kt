package miyucomics.hexical.features.prestidigitation.handlers

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.prestidigitation.interfaces.PrestidigitationHandler
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.block.Block
import net.minecraft.registry.Registries
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import java.io.InputStreamReader

object PrestidigitationBlockBooleans {
	private val map: MutableMap<Block, BooleanProperty> = mutableMapOf()

	fun init(register: (PrestidigitationHandler) -> Unit) {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
			override fun getFabricId() = HexicalMain.id("prestidigitation_block_boolean")
			override fun reload(manager: ResourceManager) {
				map.clear()
				manager.findResources("prestidigitation") { it.path.endsWith("block_booleans.json") }.keys.forEach { path ->
					(JsonParser.parseReader(InputStreamReader(manager.getResource(path).get().inputStream, "UTF-8")) as JsonObject).entrySet().forEach {
						val id = Identifier(it.key)
						if (Registries.BLOCK.containsId(id))
							map[Registries.BLOCK.get(id)] = BooleanProperty.of(it.value.asString)
					}
				}
			}
		})

		register(object : PrestidigitationHandlerBlock() {
			override fun canAffectBlock(env: CastingEnvironment, pos: BlockPos) = map.containsKey(getBlock(env, pos))
			override fun affect(env: CastingEnvironment, pos: BlockPos) {
				val state = getBlockState(env, pos)
				setBlockState(env, pos, state.with(map[state.block], !state.get(map[state.block])))
			}
		})
	}
}