package miyucomics.hexical.data

import at.petrak.hexcasting.api.utils.asDouble
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.client.ClientStorage
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.NbtDouble
import net.minecraft.nbt.NbtList
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f

class LesserSentinelState {
	var lesserSentinels: MutableList<Vec3d> = mutableListOf()

	fun toNbt(): NbtList {
		val list = NbtList()
		lesserSentinels.forEach { pos ->
			list.add(NbtDouble.of(pos.x))
			list.add(NbtDouble.of(pos.y))
			list.add(NbtDouble.of(pos.z))
		}
		return list
	}

	fun syncToClient(player: ServerPlayerEntity) {
		val buf = PacketByteBufs.create()
		buf.writeInt(lesserSentinels.size)
		lesserSentinels.forEach { pos ->
			buf.writeDouble(pos.x)
			buf.writeDouble(pos.y)
			buf.writeDouble(pos.z)
		}
		ServerPlayNetworking.send(player, LESSER_SENTINEL_CHANNEL, buf)
	}

	companion object {
		private val LESSER_SENTINEL_CHANNEL = HexicalMain.id("lesser_sentinel")

		@JvmStatic
		fun createFromNbt(tag: NbtList): LesserSentinelState {
			val state = LesserSentinelState()
			tag.windowed(3, 3, false) { state.lesserSentinels.add(Vec3d(it[0].asDouble, it[1].asDouble, it[2].asDouble)) }
			return state
		}

		fun registerServerReciever() {
			ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
				val player = handler.player
				(player as PlayerEntityMinterface).getLesserSentinels().syncToClient(player)
			}
		}

		fun registerClientReciever() {
			ClientPlayNetworking.registerGlobalReceiver(LESSER_SENTINEL_CHANNEL) { _, _, packet, _ ->
				val count = packet.readInt()
				ClientStorage.lesserSentinels.clear()
				for (i in 0 until count) {
					val x = packet.readDouble()
					val y = packet.readDouble()
					val z = packet.readDouble()
					ClientStorage.lesserSentinels.add(Vec3d(x, y, z))
				}
				println(ClientStorage.lesserSentinels)
			}
		}
	}
}