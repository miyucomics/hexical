package miyucomics.hexical.state

import miyucomics.hexical.Hexical
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.MinecraftServer
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.*
import java.util.function.Consumer

class PersistentStateHandler : PersistentState() {
	private var archLamps: HashMap<UUID, ArchLampData> = HashMap<UUID, ArchLampData>()

	override fun writeNbt(nbt: NbtCompound): NbtCompound {
		val nbtArchLamps = NbtCompound()
		archLamps.forEach { (uuid, playerData) -> nbtArchLamps.put(uuid.toString(), playerData.toNbt()) }
		nbt.put("arch_lamp", nbtArchLamps)
		return nbt
	}

	companion object {
		private fun createFromNbt(tag: NbtCompound): PersistentStateHandler {
			val state = PersistentStateHandler()
			val nbtArchLamps = tag.getCompound("arch_lamp")
			nbtArchLamps.keys.forEach(Consumer { key: String -> state.archLamps[UUID.fromString(key)] = ArchLampData.createFromNbt(nbtArchLamps.getCompound(key)) })
			return state
		}

		private fun getServerState(server: MinecraftServer): PersistentStateHandler {
			val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager
			val state: PersistentStateHandler = persistentStateManager.getOrCreate(PersistentStateHandler::createFromNbt, ::PersistentStateHandler, Hexical.MOD_ID)
			state.markDirty()
			return state
		}

		fun getPlayerState(player: LivingEntity): ArchLampData {
			val serverState: PersistentStateHandler = getServerState(player.getWorld().server!!)
			val playerState: ArchLampData = serverState.archLamps.computeIfAbsent(player.uuid) { ArchLampData() }
			return playerState
		}
	}
}