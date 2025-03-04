package miyucomics.hexical.casting.patterns.grimoire

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.utils.getOrCreateCompound
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

class OpGrimoireIndex : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val itemInfo = env.getHeldItemToOperateOn { stack -> stack.isOf(HexicalItems.GRIMOIRE_ITEM) }
		if (itemInfo == null)
			throw MishapBadOffhandItem.of(null, "grimoire")

		val stack = itemInfo.stack
		populateGrimoireMetadata(stack)
		val metadata = stack.orCreateNbt.getCompound("metadata")

		val result = mutableListOf<PatternIota>()
		for (pattern in metadata.keys)
			result.add(PatternIota(HexPattern.fromAngles(pattern, HexDir.values()[metadata.getCompound(pattern).getInt("direction")])))
		return listOf(ListIota(result.toList()))
	}

	companion object {
		fun populateGrimoireMetadata(grimoire: ItemStack) {
			if (grimoire.orCreateNbt.contains("metadata"))
				return
			val metadata = NbtCompound()
			for (key in grimoire.orCreateNbt.getOrCreateCompound("expansions").keys) {
				val data = NbtCompound()
				data.putInt("direction", HexDir.EAST.ordinal)
				metadata.putCompound(key, data)
			}
			grimoire.orCreateNbt.putCompound("metadata", metadata)
		}
	}
}