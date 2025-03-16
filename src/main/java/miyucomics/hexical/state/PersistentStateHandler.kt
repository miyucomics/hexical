package miyucomics.hexical.state

import miyucomics.hexical.HexicalMain
import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.MinecraftServer
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.*

class PersistentStateHandler : PersistentState() {
	private var archLamps = HashMap<UUID, ArchLampData>()

	override fun writeNbt(nbt: NbtCompound): NbtCompound {
		val nbtArchLamps = NbtCompound()
		archLamps.forEach { (uuid, archData) -> nbtArchLamps.put(uuid.toString(), archData.toNbt()) }
		nbt.put("arch_lamp", nbtArchLamps)

		return nbt
	}

	companion object {
		private fun createFromNbt(tag: NbtCompound): PersistentStateHandler {
			val state = PersistentStateHandler()

			val nbtArchLamps = tag.getCompound("arch_lamp")
			nbtArchLamps.keys.forEach { key: String -> state.archLamps[UUID.fromString(key)] = ArchLampData.createFromNbt(nbtArchLamps.getCompound(key)) }

			return state
		}

		private fun getServerState(server: MinecraftServer): PersistentStateHandler {
			val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager
			val state = persistentStateManager.getOrCreate(PersistentStateHandler::createFromNbt, ::PersistentStateHandler, HexicalMain.MOD_ID)
			state.markDirty()
			return state
		}

		fun getArchLampData(entity: Entity) = getServerState(entity.server!!).archLamps.computeIfAbsent(entity.uuid) { ArchLampData() }
	}
}