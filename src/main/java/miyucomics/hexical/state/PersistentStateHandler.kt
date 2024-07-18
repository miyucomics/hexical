package miyucomics.hexical.state

import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.serializeToNBT
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.registry.HexicalNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.*

class PersistentStateHandler : PersistentState() {
	private var archLamps: HashMap<UUID, ArchLampData> = HashMap<UUID, ArchLampData>()
	private var evocation: HashMap<UUID, NbtCompound> = HashMap<UUID, NbtCompound>()
	private var shader: HashMap<UUID, Identifier> = HashMap<UUID, Identifier>()
	private var wristpockets: HashMap<UUID, ItemStack> = HashMap<UUID, ItemStack>()

	override fun writeNbt(nbt: NbtCompound): NbtCompound {
		val nbtArchLamps = NbtCompound()
		archLamps.forEach { (uuid, archData) -> nbtArchLamps.put(uuid.toString(), archData.toNbt()) }
		nbt.put("arch_lamp", nbtArchLamps)

		val nbtEvocation = NbtCompound()
		evocation.forEach { (uuid, compound) -> nbtEvocation.putCompound(uuid.toString(), compound) }
		nbt.put("evocation", nbtEvocation)

		val nbtShader = NbtCompound()
		shader.forEach { (uuid, shader) -> nbtShader.putString(uuid.toString(), shader.toString()) }
		nbt.put("shader", nbtShader)

		val nbtWristpockets = NbtCompound()
		wristpockets.forEach { (uuid, stack) -> nbtWristpockets.putCompound(uuid.toString(), stack.serializeToNBT()) }
		nbt.put("wristpocket", nbtWristpockets)

		return nbt
	}

	companion object {
		private fun createFromNbt(tag: NbtCompound): PersistentStateHandler {
			val state = PersistentStateHandler()

			val nbtArchLamps = tag.getCompound("arch_lamp")
			nbtArchLamps.keys.forEach { key: String -> state.archLamps[UUID.fromString(key)] = ArchLampData.createFromNbt(nbtArchLamps.getCompound(key)) }

			val nbtEvocation = tag.getCompound("evocation")
			nbtEvocation.keys.forEach { key: String -> state.evocation[UUID.fromString(key)] = nbtEvocation.getCompound(key) }

			val nbtShader = tag.getCompound("shader")
			nbtShader.keys.forEach { key: String -> state.shader[UUID.fromString(key)] = Identifier(nbtShader.getString(key)) }

			val nbtWristpockets = tag.getCompound("wristpocket")
			nbtWristpockets.keys.forEach { key: String -> state.wristpockets[UUID.fromString(key)] = ItemStack.fromNbt(nbtWristpockets.getCompound(key)) }

			return state
		}

		private fun getServerState(server: MinecraftServer): PersistentStateHandler {
			val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager
			val state = persistentStateManager.getOrCreate(PersistentStateHandler::createFromNbt, ::PersistentStateHandler, HexicalMain.MOD_ID)
			state.markDirty()
			return state
		}

		fun setEvocation(player: ServerPlayerEntity, hex: NbtCompound) {
			val serverState = getServerState(player.getWorld().server)
			serverState.evocation[player.uuid] = hex
		}

		fun getEvocation(player: ServerPlayerEntity): NbtCompound? {
			val serverState = getServerState(player.getWorld().server)
			return serverState.evocation[player.uuid]
		}

		fun getPlayerArchLampData(player: ServerPlayerEntity): ArchLampData {
			val serverState = getServerState(player.getWorld().server)
			return serverState.archLamps.computeIfAbsent(player.uuid) { ArchLampData() }
		}

		fun getShader(player: ServerPlayerEntity): Identifier? {
			val serverState = getServerState(player.getWorld().server)
			return serverState.shader[player.uuid]
		}

		fun setShader(player: ServerPlayerEntity, shader: Identifier?) {
			val serverState = getServerState(player.getWorld().server)
			if (shader == null) {
				serverState.shader.remove(player.uuid)
				val packet = PacketByteBufs.create()
				packet.writeString("clear")
				ServerPlayNetworking.send(player, HexicalNetworking.SYNC_SHADER_CHANNEL, packet)
			} else {
				serverState.shader[player.uuid] = shader
				val packet = PacketByteBufs.create()
				packet.writeString(shader.toString())
				ServerPlayNetworking.send(player, HexicalNetworking.SYNC_SHADER_CHANNEL, packet)
			}
		}

		fun stashWristpocket(player: ServerPlayerEntity, stack: ItemStack) {
			val serverState = getServerState(player.getWorld().server)
			serverState.wristpockets[player.uuid] = stack
		}

		fun wristpocketItem(player: ServerPlayerEntity): ItemStack {
			val serverState = getServerState(player.getWorld().server)
			return serverState.wristpockets.computeIfAbsent(player.uuid) { ItemStack.EMPTY }
		}
	}
}