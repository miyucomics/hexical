package miyucomics.hexical.registry

import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.items.ConjuredStaffItem
import miyucomics.hexical.items.getConjuredStaff
import miyucomics.hexical.state.KeybindData
import miyucomics.hexical.state.TelepathyData
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier

object HexicalNetworking {
	val CAST_CONJURED_STAFF_PACKET: Identifier = HexicalMain.id("cast_conjured_staff")
	val START_TELEPATHY_PACKET: Identifier = HexicalMain.id("start_telepathy")
	val STOP_TELEPATHY_PACKET: Identifier = HexicalMain.id("stop_telepathy")
	val PRESSED_KEY_PACKET: Identifier = HexicalMain.id("press_key")
	val RELEASED_KEY_PACKET: Identifier = HexicalMain.id("release_key")

	@JvmStatic
	fun init() {
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
			if (!KeybindData.active.containsKey(player.uuid)) {
				KeybindData.active[player.uuid] = HashMap()
				KeybindData.duration[player.uuid] = HashMap()
			}
			val key = buf.readString()
			KeybindData.active[player.uuid]!![key] = false
			KeybindData.duration[player.uuid]!![key] = 0
		}
		ServerPlayNetworking.registerGlobalReceiver(START_TELEPATHY_PACKET) { _, player, _, _, _ ->
			TelepathyData.active[player.uuid] = true
			TelepathyData.timer[player.uuid] = 0
		}
		ServerPlayNetworking.registerGlobalReceiver(STOP_TELEPATHY_PACKET) { _, player, _, _, _ ->
			TelepathyData.active[player.uuid] = false
		}
		ServerPlayNetworking.registerGlobalReceiver(CAST_CONJURED_STAFF_PACKET) { _, player, _, buf, _ ->
			val hand = getConjuredStaff(player) ?: return@registerGlobalReceiver
			val world = player.world as ServerWorld
			val constructedStack: MutableList<Iota> = ArrayList()
			val staffRank = buf.readInt()
			for (i in 0 until staffRank)
				constructedStack.add(BooleanIota(buf.readBoolean()))
			(player.getStackInHand(hand).item as ConjuredStaffItem).cast(world, player, hand, player.getStackInHand(hand), constructedStack)
		}
	}
}