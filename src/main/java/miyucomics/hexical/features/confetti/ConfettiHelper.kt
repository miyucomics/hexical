package miyucomics.hexical.features.confetti

import miyucomics.hexical.HexicalMain
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

object ConfettiHelper {
	val CONFETTI_CHANNEL: Identifier = HexicalMain.id("confetti")

	fun spawn(world: ServerWorld, pos: Vec3d, dir: Vec3d, speed: Double) {
		val packet = PacketByteBufs.create()
		packet.writeLong(HexicalMain.RANDOM.nextLong())
		packet.writeDouble(pos.x)
		packet.writeDouble(pos.y)
		packet.writeDouble(pos.z)
		packet.writeDouble(dir.x)
		packet.writeDouble(dir.y)
		packet.writeDouble(dir.z)
		packet.writeDouble(speed)
		world.players.forEach { player -> world.sendToPlayerIfNearby(player, false, pos.x, pos.y, pos.z, ServerPlayNetworking.createS2CPacket(CONFETTI_CHANNEL, packet)) }
	}
}