package miyucomics.hexical.items

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.getOrCreateCompound
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.putList
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

class GrimoireItem : Item(Settings().maxCount(1)) {
	companion object {
		fun getPatternInGrimoire(stack: ItemStack, key: HexPattern, world: ServerWorld): List<Iota>? {
			val data = stack.orCreateNbt.getOrCreateCompound("patterns").getCompound(key.anglesSignature())
			val patsTag = data.getList("expansion", NbtElement.COMPOUND_TYPE.toInt())
			if (patsTag.isEmpty())
				return null
			val out = ArrayList<Iota>()
			for (patTag in patsTag)
				out.add(IotaType.deserialize(patTag as NbtCompound, world))
			return out
		}
	}
}

fun grimoireLookup(player: ServerPlayerEntity, pattern: HexPattern, stack: ItemStack): List<Iota>? {
	val value = GrimoireItem.getPatternInGrimoire(stack, pattern, player.serverWorld)
	if (value != null)
		return value
	return null
}