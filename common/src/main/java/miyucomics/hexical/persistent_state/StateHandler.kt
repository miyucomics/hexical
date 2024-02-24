package miyucomics.hexical.persistent_state

import miyucomics.hexical.Hexical
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.MinecraftServer
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.*
import java.util.function.Consumer

class StateHandler : PersistentState() {
	var masterLampData: HashMap<UUID, MasterLampData> = HashMap<UUID, MasterLampData>()

	override fun writeNbt(nbt: NbtCompound): NbtCompound {
		val playersNbt = NbtCompound()
		masterLampData.forEach { (uuid, playerData) -> playersNbt.put(uuid.toString(), playerData.toNbt()) }
		nbt.put("players", playersNbt)
		return nbt
	}

	companion object {
		private fun createFromNbt(tag: NbtCompound): StateHandler {
			val state = StateHandler()
			val playersNbt = tag.getCompound("players")
			playersNbt.keys.forEach(Consumer { key: String? -> state.masterLampData[UUID.fromString(key)] = MasterLampData.createFromNbt(playersNbt.getCompound(key)) })
			return state
		}

		private fun getServerState(server: MinecraftServer): StateHandler {
			val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager
			val state: StateHandler = persistentStateManager.getOrCreate(StateHandler::createFromNbt, { StateHandler() }, Hexical.MOD_ID)
			state.markDirty()
			return state
		}

		fun getPlayerState(player: LivingEntity): MasterLampData {
			val serverState: StateHandler = getServerState(player.getWorld().server!!)
			val playerState: MasterLampData = serverState.masterLampData.computeIfAbsent(player.uuid) { MasterLampData() }
			return playerState
		}
	}
}