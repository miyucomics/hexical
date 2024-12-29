package miyucomics.hexical.items

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.getOrCreateCompound
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld

class GrimoireItem : Item(Settings().maxCount(1)) {
	companion object {
		@JvmStatic
		fun getPatternInGrimoire(stack: ItemStack, key: HexPattern, world: ServerWorld): List<Iota>? {
			val data = stack.orCreateNbt.getOrCreateCompound("expansions")
			if (!data.contains(key.anglesSignature()))
				return null
			val deserialized = IotaType.deserialize(data.getCompound(key.anglesSignature()), world)
			if (deserialized is ListIota)
				return deserialized.list.toList()
			return null
		}
	}
}