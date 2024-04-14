package miyucomics.hexical.networking

import at.petrak.hexcasting.api.utils.getSafe
import at.petrak.hexcasting.common.network.IMessage
import miyucomics.hexical.Hexical
import miyucomics.hexical.entities.LivingScrollEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class SpawnLivingScrollPacket(val inner: EntitySpawnS2CPacket, val pos: BlockPos, val direction: Direction, val size: Int) : IMessage {
	override fun getFabricId() = ID

	override fun serialize(buf: PacketByteBuf) {
		inner.write(buf)
		buf.writeBlockPos(pos)
		buf.writeVarInt(direction.ordinal)
		buf.writeVarInt(size)
	}

	companion object {
		val ID: Identifier = Hexical.id("living_scroll")

		fun deserialize(buf: PacketByteBuf): SpawnLivingScrollPacket {
			val inner = EntitySpawnS2CPacket(buf)
			val pos = buf.readBlockPos()
			val dir = Direction.values().getSafe(buf.readVarInt(), Direction.DOWN)
			val blockSize = buf.readVarInt()
			return SpawnLivingScrollPacket(inner, pos, dir, blockSize)
		}

		fun handle(self: SpawnLivingScrollPacket) {
			MinecraftClient.getInstance().execute(Runnable {
				val player = MinecraftClient.getInstance().player ?: return@Runnable
				player.networkHandler.onEntitySpawn(self.inner)
					val entity = player.world.getEntityById(self.inner.id)
					if (entity is LivingScrollEntity)
						entity.readSpawnData(self.pos, self.direction, self.size)
			})
		}
	}
}