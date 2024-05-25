package miyucomics.hexical.items

import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.getOrCreateCompound
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.putList
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

class GrimoireItem : Item(Settings().maxCount(1).group(HexicalItems.HEXICAL_GROUP)) {
	companion object {
		fun write(stack: ItemStack, key: HexPattern, information: List<Iota>) {
			val patterns = NbtList()
			for (iota in information)
				patterns.add(HexIotaTypes.serialize(iota))
			if (!stack.orCreateNbt.contains("patterns"))
				stack.orCreateNbt.putCompound("patterns", NbtCompound())
			stack.orCreateNbt.getCompound("patterns").putCompound(key.anglesSignature(), NbtCompound())
			stack.orCreateNbt.getCompound("patterns").getCompound(key.anglesSignature()).putList("expansion", patterns)
		}

		fun getPatternsInGrimoire(stack: ItemStack): List<PatternIota> {
			val result = ArrayList<PatternIota>()
			for (pattern in stack.orCreateNbt.getOrCreateCompound("patterns").keys)
				result.add(PatternIota(HexPattern.fromAngles(pattern, HexDir.WEST)))
			return result
		}

		fun getUses(stack: ItemStack, key: HexPattern): Int? {
			if (!stack.orCreateNbt.getCompound("patterns").getCompound(key.anglesSignature()).contains("uses"))
				return null
			return stack.orCreateNbt.getCompound("patterns").getCompound(key.anglesSignature()).getInt("uses")
		}

		fun restrict(stack: ItemStack, key: HexPattern, uses: Int) {
			if (!stack.orCreateNbt.contains("patterns"))
				return
			if (!stack.orCreateNbt.getCompound("patterns").contains(key.anglesSignature()))
				return
			stack.orCreateNbt.getCompound("patterns").getCompound(key.anglesSignature()).putInt("uses", uses)
		}

		fun erase(stack: ItemStack, key: HexPattern) {
			if (!stack.orCreateNbt.contains("patterns"))
				return
			stack.orCreateNbt.getCompound("patterns").remove(key.anglesSignature())
		}

		fun getPatternInGrimoire(stack: ItemStack, key: HexPattern, world: ServerWorld): List<Iota>? {
			val data = stack.orCreateNbt.getOrCreateCompound("patterns").getCompound(key.anglesSignature())
			val patsTag = data.getList("expansion", NbtElement.COMPOUND_TYPE.toInt())
			if (patsTag.isEmpty())
				return null
			val out = ArrayList<Iota>()
			for (patTag in patsTag)
				out.add(HexIotaTypes.deserialize(patTag as NbtCompound, world))
			if (data.contains("uses")) {
				data.putInt("uses", data.getInt("uses") - 1)
				if (data.getInt("uses") <= 0)
					erase(stack, key)
			}
			return out
		}
	}
}

fun grimoireLookup(player: ServerPlayerEntity, pattern: HexPattern, slots: List<ItemStack>): List<Iota>? {
	for (stack in slots) {
		if (stack.item != HexicalItems.GRIMOIRE_ITEM)
			continue
		val value = GrimoireItem.getPatternInGrimoire(stack, pattern, player.getWorld())
		if (value != null)
			return value
	}
	return null
}