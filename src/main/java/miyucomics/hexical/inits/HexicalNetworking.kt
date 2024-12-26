package miyucomics.hexical.inits

import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.client.PlayerAnimations
import miyucomics.hexical.items.TchotchkeItem
import miyucomics.hexical.items.getConjuredStaff
import miyucomics.hexical.state.EvokeState
import miyucomics.hexical.state.KeybindData
import miyucomics.hexical.utils.CastingUtils
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier

object HexicalNetworking {
	@JvmField
	val tchotchke_CHANNEL: Identifier = HexicalMain.id("tchotchke")
	val PRESSED_KEY_CHANNEL: Identifier = HexicalMain.id("press_key")
	val RELEASED_KEY_CHANNEL: Identifier = HexicalMain.id("release_key")

	val START_EVOKE_CHANNEL: Identifier = HexicalMain.id("start_evoking")
	val END_EVOKING_CHANNEL: Identifier = HexicalMain.id("end_evoking")

	@JvmStatic
	fun serverInit() {
		ServerPlayNetworking.registerGlobalReceiver(tchotchke_CHANNEL) { server, player, _, buf, _ ->
			val hand = getConjuredStaff(player) ?: return@registerGlobalReceiver
			val constructedStack: MutableList<Iota> = ArrayList()
			val staffRank = buf.readInt()
			for (i in 0 until staffRank)
				constructedStack.add(BooleanIota(buf.readBoolean()))
			server.execute {
				TchotchkeItem.cast(player, hand, player.getStackInHand(hand), constructedStack)
			}
		}

		ServerPlayNetworking.registerGlobalReceiver(PRESSED_KEY_CHANNEL) { _, player, _, buf, _ ->
			if (!KeybindData.active.containsKey(player.uuid)) {
				KeybindData.active[player.uuid] = HashMap()
				KeybindData.duration[player.uuid] = HashMap()
			}
			val key = buf.readString()
			KeybindData.active[player.uuid]!![key] = true
			KeybindData.duration[player.uuid]!![key] = 0
		}
		ServerPlayNetworking.registerGlobalReceiver(RELEASED_KEY_CHANNEL) { _, player, _, buf, _ ->
			val key = buf.readString()
			KeybindData.active[player.uuid]!![key] = false
			KeybindData.duration[player.uuid]!![key] = 0
		}

		ServerPlayNetworking.registerGlobalReceiver(START_EVOKE_CHANNEL) { server, player, _, _, _ ->
			if (!CastingUtils.isEnlightened(player))
				return@registerGlobalReceiver
			EvokeState.active[player.uuid] = true
			EvokeState.duration[player.uuid] = HexicalMain.EVOKE_DURATION
			player.world.playSound(null, player.x, player.y, player.z, HexicalSounds.EVOKING_MURMUR, SoundCategory.PLAYERS, 1f, 1f)
			for (otherPlayer in server.playerManager.playerList) {
				val packet = PacketByteBufs.create()
				packet.writeUuid(player.uuid)
				ServerPlayNetworking.send(otherPlayer, START_EVOKE_CHANNEL, packet)
			}
		}
		ServerPlayNetworking.registerGlobalReceiver(END_EVOKING_CHANNEL) { server, player, _, _, _ ->
			if (!CastingUtils.isEnlightened(player))
				return@registerGlobalReceiver
			EvokeState.active[player.uuid] = false
			for (otherPlayer in server.playerManager.playerList) {
				val packet = PacketByteBufs.create()
				packet.writeUuid(player.uuid)
				ServerPlayNetworking.send(otherPlayer, END_EVOKING_CHANNEL, packet)
			}
		}
	}

	@JvmStatic
	fun clientInit() {
		ClientPlayNetworking.registerGlobalReceiver(START_EVOKE_CHANNEL) { client, _, packet, _ ->
			val uuid = packet.readUuid()
			val player = client.world!!.getPlayerByUuid(uuid) ?: return@registerGlobalReceiver
			val container = (player as PlayerAnimations).hexicalModAnimations()
			container.setAnimation(KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(HexicalMain.id("cast_loop"))!!))
			EvokeState.active[uuid] = true
		}
		ClientPlayNetworking.registerGlobalReceiver(END_EVOKING_CHANNEL) { client, _, packet, _ ->
			val uuid = packet.readUuid()
			val player = client.world!!.getPlayerByUuid(uuid) ?: return@registerGlobalReceiver
			val container = (player as PlayerAnimations).hexicalModAnimations()
			container.setAnimation(KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(HexicalMain.id("cast_end"))!!))
			EvokeState.active[uuid] = false
		}
	}
}