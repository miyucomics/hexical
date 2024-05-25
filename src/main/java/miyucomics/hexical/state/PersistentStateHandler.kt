package miyucomics.hexical.state

import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.serializeToNBT
import miyucomics.hexical.HexicalMain
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.MinecraftServer
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.*

class PersistentStateHandler : PersistentState() {
	private var archLamps: HashMap<UUID, ArchLampData> = HashMap<UUID, ArchLampData>()
	private var wristpockets: HashMap<UUID, ItemStack> = HashMap<UUID, ItemStack>()

	override fun writeNbt(nbt: NbtCompound): NbtCompound {
		val nbtArchLamps = NbtCompound()
		archLamps.forEach { (uuid, playerData) -> nbtArchLamps.put(uuid.toString(), playerData.toNbt()) }
		nbt.put("arch_lamp", nbtArchLamps)
		val nbtWristpockets = NbtCompound()
		wristpockets.forEach { (uuid, stack) -> nbtWristpockets.putCompound(uuid.toString(), stack.serializeToNBT()) }
		nbt.put("wristpockets", nbtWristpockets)
		return nbt
	}

	companion object {
		private fun createFromNbt(tag: NbtCompound): PersistentStateHandler {
			val state = PersistentStateHandler()
			val nbtArchLamps = tag.getCompound("arch_lamp")
			nbtArchLamps.keys.forEach { key: String -> state.archLamps[UUID.fromString(key)] = ArchLampData.createFromNbt(nbtArchLamps.getCompound(key)) }
			val nbtWristpockets = tag.getCompound("wristpockets")
			nbtWristpockets.keys.forEach { key: String -> state.wristpockets[UUID.fromString(key)] = ItemStack.fromNbt(nbtWristpockets.getCompound(key)) }
			return state
		}

		private fun getServerState(server: MinecraftServer): PersistentStateHandler {
			val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager
			val state: PersistentStateHandler = persistentStateManager.getOrCreate(PersistentStateHandler::createFromNbt, ::PersistentStateHandler, HexicalMain.MOD_ID)
			state.markDirty()
			return state
		}

		fun getPlayerArchLampData(player: PlayerEntity): ArchLampData {
			val serverState: PersistentStateHandler = getServerState(player.getWorld().server!!)
			return serverState.archLamps.computeIfAbsent(player.uuid) { ArchLampData() }
		}

		fun stashWristpocket(player: PlayerEntity, stack: ItemStack) {
			val serverState: PersistentStateHandler = getServerState(player.getWorld().server!!)
			serverState.wristpockets[player.uuid] = stack
		}

		fun wristpocketItem(player: PlayerEntity): ItemStack {
			val serverState: PersistentStateHandler = getServerState(player.getWorld().server!!)
			return serverState.wristpockets.computeIfAbsent(player.uuid) { ItemStack.EMPTY }
		}
	}
}