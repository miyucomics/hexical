package miyucomics.hexical.features.evocation

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.player.fields.evocationActive
import miyucomics.hexical.features.player.fields.evocationDuration
import miyucomics.hexical.inits.HexicalSounds
import miyucomics.hexical.misc.CastingUtils
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier

object ServerEvocationManager {
	val START_EVOKE_CHANNEL: Identifier = HexicalMain.id("start_evoking")
	val END_EVOKING_CHANNEL: Identifier = HexicalMain.id("end_evoking")

	fun startEvocation(player: ServerPlayerEntity, server: MinecraftServer) {
		if (!CastingUtils.isEnlightened(player))
			return
		player.evocationActive = true
		player.evocationDuration = HexicalMain.EVOKE_DURATION
		player.world.playSound(null, player.x, player.y, player.z, HexicalSounds.EVOKING_MURMUR, SoundCategory.PLAYERS, 1f, 1f)
		for (receiver in server.playerManager.playerList)
			ServerPlayNetworking.send(receiver, START_EVOKE_CHANNEL, PacketByteBufs.create().also { it.writeUuid(player.uuid) })
	}

	fun endEvocation(player: ServerPlayerEntity, server: MinecraftServer) {
		if (!CastingUtils.isEnlightened(player))
			return
		player.evocationActive = false
		for (receiver in server.playerManager.playerList)
			ServerPlayNetworking.send(receiver, END_EVOKING_CHANNEL, PacketByteBufs.create().also { it.writeUuid(player.uuid) })
	}
}