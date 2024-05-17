package miyucomics.hexical.state

import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.serializeToNBT
import miyucomics.hexical.Hexical
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtDouble
import net.minecraft.nbt.NbtTypes
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.*
import java.util.function.Consumer

class PersistentStateHandler : PersistentState() {
	private var archLamps: HashMap<UUID, ArchLampData> = HashMap<UUID, ArchLampData>()
	private var boundLibraries: HashMap<UUID, BlockPos?> = HashMap<UUID, BlockPos?>()
	private var wristpockets: HashMap<UUID, ItemStack> = HashMap<UUID, ItemStack>()

	override fun writeNbt(nbt: NbtCompound): NbtCompound {
		val nbtArchLamps = NbtCompound()
		archLamps.forEach { (uuid, playerData) -> nbtArchLamps.put(uuid.toString(), playerData.toNbt()) }
		nbt.put("arch_lamp", nbtArchLamps)
		val nbtBoundLibraries = NbtCompound()
		boundLibraries.forEach { (uuid, position) ->
			if (position == null)
				return@forEach
			val serializedPosition = NbtCompound()
			serializedPosition.putInt("x", position.x)
			serializedPosition.putInt("y", position.y)
			serializedPosition.putInt("z", position.z)
			nbtBoundLibraries.putCompound(uuid.toString(), serializedPosition)
		}
		nbt.put("bound_libraries", nbtBoundLibraries)
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
			val nbtBoundLibraries = tag.getCompound("bound_libraries")
			nbtBoundLibraries.keys.forEach { key: String ->
				val position = nbtBoundLibraries.getCompound(key)
				state.boundLibraries[UUID.fromString(key)] = BlockPos(position.getInt("x"), position.getInt("y"), position.getInt("z"))
			}
			val nbtWristpockets = tag.getCompound("wristpockets")
			nbtWristpockets.keys.forEach { key: String -> state.wristpockets[UUID.fromString(key)] = ItemStack.fromNbt(nbtWristpockets.getCompound(key)) }
			return state
		}

		private fun getServerState(server: MinecraftServer): PersistentStateHandler {
			val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager
			val state: PersistentStateHandler = persistentStateManager.getOrCreate(PersistentStateHandler::createFromNbt, ::PersistentStateHandler, Hexical.MOD_ID)
			state.markDirty()
			return state
		}

		fun getPlayerArchLampData(player: PlayerEntity): ArchLampData {
			val serverState: PersistentStateHandler = getServerState(player.getWorld().server!!)
			return serverState.archLamps.computeIfAbsent(player.uuid) { ArchLampData() }
		}

		fun setPlayerBoundLibrary(player: PlayerEntity, position: BlockPos?) {
			val serverState: PersistentStateHandler = getServerState(player.getWorld().server!!)
			serverState.boundLibraries[player.uuid] = position
		}

		fun getPlayerBoundLibrary(player: PlayerEntity): BlockPos? {
			val serverState: PersistentStateHandler = getServerState(player.getWorld().server!!)
			return serverState.boundLibraries.computeIfAbsent(player.uuid) { null }
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