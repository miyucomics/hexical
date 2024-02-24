package miyucomics.hexical.persistent_state

import miyucomics.hexical.Hexical
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.MinecraftServer
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.*
import java.util.function.Consumer

class PersistentStateHandler : PersistentState() {
	private var masterLamps: HashMap<UUID, MasterLampData> = HashMap<UUID, MasterLampData>()

	override fun writeNbt(nbt: NbtCompound): NbtCompound {
		val nbtMasterLamps = NbtCompound()
		masterLamps.forEach { (uuid, playerData) -> nbtMasterLamps.put(uuid.toString(), playerData.toNbt()) }
		nbt.put("master_lamp", nbtMasterLamps)
		return nbt
	}

	companion object {
		private fun createFromNbt(tag: NbtCompound): PersistentStateHandler {
			val state = PersistentStateHandler()
			val nbtMasterLamps = tag.getCompound("master_lamp")
			nbtMasterLamps.keys.forEach(Consumer { key: String -> state.masterLamps[UUID.fromString(key)] = MasterLampData.createFromNbt(nbtMasterLamps.getCompound(key)) })
			return state
		}

		private fun getServerState(server: MinecraftServer): PersistentStateHandler {
			val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager
			val state: PersistentStateHandler = persistentStateManager.getOrCreate(PersistentStateHandler::createFromNbt, ::PersistentStateHandler, Hexical.MOD_ID)
			state.markDirty()
			return state
		}

		fun getPlayerState(player: LivingEntity): MasterLampData {
			val serverState: PersistentStateHandler = getServerState(player.getWorld().server!!)
			val playerState: MasterLampData = serverState.masterLamps.computeIfAbsent(player.uuid) { MasterLampData() }
			return playerState
		}
	}
}