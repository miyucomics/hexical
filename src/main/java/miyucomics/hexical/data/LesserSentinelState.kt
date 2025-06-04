package miyucomics.hexical.data

import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.asDouble
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.client.ClientStorage
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.command.argument.EntityArgumentType.player
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtDouble
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class LesserSentinelState {
	var instances: HashMap<RegistryKey<World>, LesserSentinelsInstance> = hashMapOf()

	fun toNbt(): NbtList {
		val list = NbtList()
		instances.values.forEach { list.add(it.toNbt()) }
		return list
	}

	fun getCurrentInstance(player: ServerPlayerEntity) = instances.getOrPut(player.serverWorld.registryKey) { LesserSentinelsInstance(mutableListOf(), player.serverWorld.registryKey) }

	fun syncToClient(player: ServerPlayerEntity) {
		val buf = PacketByteBufs.create()
		val instance = getCurrentInstance(player)
		buf.writeInt(instance.lesserSentinels.size)
		instance.lesserSentinels.forEach { pos ->
			buf.writeDouble(pos.x)
			buf.writeDouble(pos.y)
			buf.writeDouble(pos.z)
		}
		ServerPlayNetworking.send(player, LESSER_SENTINEL_CHANNEL, buf)
	}

	companion object {
		private val LESSER_SENTINEL_CHANNEL = HexicalMain.id("lesser_sentinel")

		@JvmStatic
		fun createFromNbt(list: NbtList): LesserSentinelState {
			val state = LesserSentinelState()
			list.forEach {
				val instance = LesserSentinelsInstance.createFromNbt(it.asCompound)
				state.instances[instance.dimension] = instance
			}
			return state
		}

		fun registerServerReciever() {
			ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register { player, origin, destination ->
				(player as PlayerEntityMinterface).getLesserSentinels().syncToClient(player)
			}
		}

		fun registerClientReciever() {
			ClientPlayNetworking.registerGlobalReceiver(LESSER_SENTINEL_CHANNEL) { client, _, packet, _ ->
				val count = packet.readInt()
				val list = mutableListOf<Vec3d>()

				repeat(count) {
					val x = packet.readDouble()
					val y = packet.readDouble()
					val z = packet.readDouble()
					list.add(Vec3d(x, y, z))
				}

				client.execute {
					ClientStorage.lesserSentinels.clear()
					ClientStorage.lesserSentinels.addAll(list)
				}
			}
		}
	}
}

data class LesserSentinelsInstance(var lesserSentinels: MutableList<Vec3d>, val dimension: RegistryKey<World>) {
	fun toNbt(): NbtCompound {
		val compound = NbtCompound()

		val location = NbtList()
		lesserSentinels.forEach { pos ->
			location.add(NbtDouble.of(pos.x))
			location.add(NbtDouble.of(pos.y))
			location.add(NbtDouble.of(pos.z))
		}

		compound.putString("dimension", dimension.value.toString())
		compound.putList("positional", location)
		return compound
	}

	companion object {
		fun createFromNbt(compound: NbtCompound): LesserSentinelsInstance {
			val lesserSentinels = mutableListOf<Vec3d>()
			compound.getList("positional", NbtElement.COMPOUND_TYPE.toInt()).windowed(3, 3, false) { lesserSentinels.add(Vec3d(it[0].asDouble, it[1].asDouble, it[2].asDouble)) }
			return LesserSentinelsInstance(lesserSentinels, RegistryKey.of(RegistryKeys.WORLD, Identifier(compound.getString("dimension"))))
		}
	}
}