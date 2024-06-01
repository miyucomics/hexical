package miyucomics.hexical.data

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.HashMap

object DyeData {
	val blocks = HashMap<String, DyeColor>()
	val families = HashMap<String, EnumMap<DyeColor, String>>()

	fun isDyeable(block: Block): Boolean {
		return blocks.containsKey(Registry.BLOCK.getId(block).toString())
	}

	fun getDye(block: Block): DyeColor? {
		return blocks.getOrDefault(Registry.BLOCK.getId(block).toString(), null)
	}

	fun getNewBlock(block: Block, dye: DyeColor): BlockState {
		families.forEach { (_, family) ->
			if (family.containsValue(Registry.BLOCK.getId(block).toString()))
				return Registry.BLOCK.get(Identifier(family[dye])).defaultState
		}
		return block.defaultState
	}

	fun loadData(stream: InputStream, name: String) {
		val json = JsonParser.parseReader(InputStreamReader(stream, "UTF-8")) as JsonObject
		json.keySet().forEach { familyKey ->
			val family = json.get(familyKey) as JsonObject
			family.keySet().forEach { block ->
				val dye = DyeColor.byName(family.get(block).asString, DyeColor.WHITE)!!
				blocks[block] = dye
				if (!families.containsKey(familyKey))
					families[familyKey] = EnumMap(DyeColor::class.java)
				families[familyKey]!![dye] = block
			}
		}
	}
}