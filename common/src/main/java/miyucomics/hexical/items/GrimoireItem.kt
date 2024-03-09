package miyucomics.hexical.items

import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.putList
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.server.world.ServerWorld


class GrimoireItem : Item(Settings().maxCount(1)) {
	companion object {
		fun writeToGrimoire (stack: ItemStack, key: HexPattern, information: List<Iota>) {
			val patterns = NbtList()
			for (iota in information)
				patterns.add(HexIotaTypes.serialize(iota));
			stack.orCreateNbt.putList(key.anglesSignature(), patterns)
		}

		fun getPatternInGrimoire (stack: ItemStack, key: HexPattern, world: ServerWorld): List<Iota>? {
			val patsTag = stack.orCreateNbt.getList(key.anglesSignature(), NbtElement.COMPOUND_TYPE.toInt())
			if (patsTag.isEmpty())
				return null
			val out = ArrayList<Iota>()
			for (patTag in patsTag)
				out.add(HexIotaTypes.deserialize(patTag as NbtCompound, world));
			return out;
		}
	}
}