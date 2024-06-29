package miyucomics.hexical.registry

import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.Iota
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.utils.HexicalUtils
import miyucomics.hexical.client.PlayerAnimations
import miyucomics.hexical.items.ConjuredStaffItem
import miyucomics.hexical.items.getConjuredStaff
import miyucomics.hexical.state.EvokeState
import miyucomics.hexical.state.KeybindData
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier

object HexicalNetworking {
	val CAST_CONJURED_STAFF_PACKET: Identifier = HexicalMain.id("cast_conjured_staff")
	val PRESSED_KEY_PACKET: Identifier = HexicalMain.id("press_key")
	val RELEASED_KEY_PACKET: Identifier = HexicalMain.id("release_key")

	val REQUEST_START_EVOKING_PACKET: Identifier = HexicalMain.id("request_start_evoking")
	val REQUEST_END_EVOKING_PACKET: Identifier = HexicalMain.id("request_end_evoking")
	val CONFIRM_START_EVOKING_PACKET: Identifier = HexicalMain.id("confirm_start_evoking")
	private val CONFIRM_END_EVOKING_PACKET: Identifier = HexicalMain.id("confirm_end_evoking")

	@JvmStatic
	fun serverInit() {
		ServerPlayNetworking.registerGlobalReceiver(CAST_CONJURED_STAFF_PACKET) { server, player, _, buf, _ ->
			val hand = getConjuredStaff(player) ?: return@registerGlobalReceiver
			val constructedStack: MutableList<Iota> = ArrayList()
			val staffRank = buf.readInt()
			for (i in 0 until staffRank)
				constructedStack.add(BooleanIota(buf.readBoolean()))
			server.execute {
				ConjuredStaffItem.cast(player, hand, player.getStackInHand(hand), constructedStack)
			}
		}

		ServerPlayNetworking.registerGlobalReceiver(PRESSED_KEY_PACKET) { _, player, _, buf, _ ->
			if (!KeybindData.active.containsKey(player.uuid)) {
				KeybindData.active[player.uuid] = HashMap()
				KeybindData.duration[player.uuid] = HashMap()
			}
			val key = buf.readString()
			KeybindData.active[player.uuid]!![key] = true
			KeybindData.duration[player.uuid]!![key] = 0
		}
		ServerPlayNetworking.registerGlobalReceiver(RELEASED_KEY_PACKET) { _, player, _, buf, _ ->
			val key = buf.readString()
			KeybindData.active[player.uuid]!![key] = false
			KeybindData.duration[player.uuid]!![key] = 0
		}

		ServerPlayNetworking.registerGlobalReceiver(REQUEST_START_EVOKING_PACKET) { server, player, _, _, _ ->
			if (!HexicalUtils.isEnlightened(player))
				return@registerGlobalReceiver
			EvokeState.active[player.uuid] = true
			EvokeState.duration[player.uuid] = 0
			player.world.playSound(null, player.x, player.y, player.z, SoundEvents.ENTITY_EVOKER_PREPARE_WOLOLO, SoundCategory.PLAYERS, 1f, 1f)
			for (otherPlayer in server.playerManager.playerList) {
				val packet = PacketByteBufs.create()
				packet.writeUuid(player.uuid)
				ServerPlayNetworking.send(otherPlayer, CONFIRM_START_EVOKING_PACKET, packet)
			}
		}
		ServerPlayNetworking.registerGlobalReceiver(REQUEST_END_EVOKING_PACKET) { server, player, _, _, _ ->
			if (!HexicalUtils.isEnlightened(player))
				return@registerGlobalReceiver
			EvokeState.active[player.uuid] = false
			EvokeState.duration[player.uuid] = -1
			for (otherPlayer in server.playerManager.playerList) {
				val packet = PacketByteBufs.create()
				packet.writeUuid(player.uuid)
				ServerPlayNetworking.send(otherPlayer, CONFIRM_END_EVOKING_PACKET, packet)
			}
		}
	}

	@JvmStatic
	fun clientInit() {
		ClientPlayNetworking.registerGlobalReceiver(CONFIRM_START_EVOKING_PACKET) { client, _, packet, _ ->
			val uuid = packet.readUuid()
			val player = client.world!!.getPlayerByUuid(uuid)?: return@registerGlobalReceiver
			val container = (player as PlayerAnimations).hexicalModAnimations()
			val frame = PlayerAnimationRegistry.getAnimation(HexicalMain.id("cast_loop"))!!
			container.setAnimation(KeyframeAnimationPlayer(frame))
			EvokeState.active[uuid] = true
		}
		ClientPlayNetworking.registerGlobalReceiver(CONFIRM_END_EVOKING_PACKET) { client, _, packet, _ ->
			val uuid = packet.readUuid()
			val container = (client.world!!.getPlayerByUuid(uuid) as PlayerAnimations).hexicalModAnimations()
			val frame = PlayerAnimationRegistry.getAnimation(HexicalMain.id("cast_end"))!!
			container.setAnimation(KeyframeAnimationPlayer(frame))
			EvokeState.active[uuid] = false
		}
	}
}