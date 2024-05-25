package miyucomics.hexical.data

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
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
}