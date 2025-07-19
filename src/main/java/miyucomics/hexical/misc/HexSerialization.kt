package miyucomics.hexical.misc

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.putList
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.server.world.ServerWorld

object HexSerialization {
	fun serializeHex(hex: List<Iota>) = NbtList().also { hex.forEach { iota -> it.add(IotaType.serialize(iota)) } }
	fun deserializeHex(list: NbtList, world: ServerWorld) = list.map { IotaType.deserialize(it.asCompound, world) }

	fun backwardsCompatibleReadHex(holder: NbtCompound, key: String, world: ServerWorld): List<Iota> {
		val element = holder.get(key)
		if (element is NbtCompound) {
			val elementData = (IotaType.deserialize(element, world) as ListIota).list.toList()
			holder.remove(key)
			holder.putList(key, serializeHex(elementData))
			return elementData
		}
		return deserializeHex(element as NbtList, world)
	}
}