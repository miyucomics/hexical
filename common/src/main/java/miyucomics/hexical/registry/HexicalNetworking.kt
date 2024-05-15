package miyucomics.hexical.registry

import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.Iota
import dev.architectury.networking.NetworkManager
import miyucomics.hexical.Hexical
import miyucomics.hexical.items.ConjuredStaffItem
import miyucomics.hexical.items.getConjuredStaff
import miyucomics.hexical.state.KeybindData
import miyucomics.hexical.state.TelepathyData
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier

object HexicalNetworking {
	val CAST_CONJURED_STAFF_PACKET: Identifier = Hexical.id("cast_conjured_staff")
	val START_TELEPATHY_PACKET: Identifier = Hexical.id("start_telepathy")
	val STOP_TELEPATHY_PACKET: Identifier = Hexical.id("stop_telepathy")
	val PRESSED_KEY_PACKET: Identifier = Hexical.id("press_key")
	val RELEASED_KEY_PACKET: Identifier = Hexical.id("release_key")

	@JvmStatic
	fun init() {
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, PRESSED_KEY_PACKET) { buf, context ->
			if (!KeybindData.active.containsKey(context.player.uuid)) {
				KeybindData.active[context.player.uuid] = HashMap()
				KeybindData.duration[context.player.uuid] = HashMap()
			}
			val key = buf.readString()
			KeybindData.active[context.player.uuid]!![key] = true
			KeybindData.duration[context.player.uuid]!![key] = 0
		}
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, RELEASED_KEY_PACKET) { buf, context ->
			if (!KeybindData.active.containsKey(context.player.uuid)) {
				KeybindData.active[context.player.uuid] = HashMap()
				KeybindData.duration[context.player.uuid] = HashMap()
			}
			val key = buf.readString()
			KeybindData.active[context.player.uuid]!![key] = false
			KeybindData.duration[context.player.uuid]!![key] = 0
		}
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, START_TELEPATHY_PACKET) { _, context ->
			TelepathyData.active[context.player.uuid] = true
			TelepathyData.timer[context.player.uuid] = 0
		}
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, STOP_TELEPATHY_PACKET) { _, context ->
			TelepathyData.active[context.player.uuid] = false
		}
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, CAST_CONJURED_STAFF_PACKET) { buf, context ->
			val player = context.player as ServerPlayerEntity
			val hand = getConjuredStaff(player) ?: return@registerReceiver
			val world = player.world as ServerWorld
			val constructedStack: MutableList<Iota> = ArrayList()
			val staffRank = buf.readInt()
			for (i in 0 until staffRank)
				constructedStack.add(BooleanIota(buf.readBoolean()))
			context.queue {
				(player.getStackInHand(hand).item as ConjuredStaffItem).cast(world, player, hand, player.getStackInHand(hand), constructedStack)
			}
		}
	}
}