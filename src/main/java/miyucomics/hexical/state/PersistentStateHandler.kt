package miyucomics.hexical.state

import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.HexicalMain
import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.*

class PersistentStateHandler : PersistentState() {
	private var archLamps = HashMap<UUID, ArchLampData>()
	private var ledgers = HashMap<UUID, LedgerData>()

	override fun writeNbt(nbt: NbtCompound): NbtCompound {
		val nbtArchLamps = NbtCompound()
		archLamps.forEach { (uuid, archData) -> nbtArchLamps.put(uuid.toString(), archData.toNbt()) }
		nbt.put("arch_lamp", nbtArchLamps)

		val nbtLedgers = NbtCompound()
		ledgers.forEach { (uuid, ledger) -> nbtLedgers.putCompound(uuid.toString(), ledger.toNbt()) }
		nbt.put("ledgers", nbtLedgers)

		return nbt
	}

	companion object {
		private fun createFromNbt(tag: NbtCompound): PersistentStateHandler {
			val state = PersistentStateHandler()

			val nbtArchLamps = tag.getCompound("arch_lamp")
			nbtArchLamps.keys.forEach { key: String -> state.archLamps[UUID.fromString(key)] = ArchLampData.createFromNbt(nbtArchLamps.getCompound(key)) }

			val nbtLedgers = tag.getCompound("ledgers")
			nbtLedgers.keys.forEach { key: String -> state.ledgers[UUID.fromString(key)] = LedgerData.createFromNbt(nbtLedgers.getCompound(key)) }

			return state
		}

		private fun getServerState(server: MinecraftServer): PersistentStateHandler {
			val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager
			val state = persistentStateManager.getOrCreate(PersistentStateHandler::createFromNbt, ::PersistentStateHandler, HexicalMain.MOD_ID)
			state.markDirty()
			return state
		}

		fun getArchLampData(entity: Entity) = getServerState(entity.server!!).archLamps.computeIfAbsent(entity.uuid) { ArchLampData() }

		@JvmStatic
		fun getLedger(player: ServerPlayerEntity) = getServerState(player.server!!).ledgers.computeIfAbsent(player.uuid) { LedgerData() }
		fun clearLedger(player: ServerPlayerEntity) {
			getServerState(player.server!!).ledgers[player.uuid] = LedgerData()
		}
	}
}