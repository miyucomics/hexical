package miyucomics.hexical.features.peripherals

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.evocation.ServerEvocationManager
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Identifier

object ServerPeripheralReceiver : InitHook() {
	val PRESSED_KEY_CHANNEL: Identifier = HexicalMain.id("press_key")
	val RELEASED_KEY_CHANNEL: Identifier = HexicalMain.id("release_key")
	val SCROLL_CHANNEL: Identifier = HexicalMain.id("scroll")

	override fun init() {
		ServerPlayNetworking.registerGlobalReceiver(PRESSED_KEY_CHANNEL) { server, player, _, buf, _ ->
			val key = buf.readString()
			player.serverKeybindActive()[key] = true
			player.serverKeybindDuration()[key] = 0
			if (key == "key.hexical.telepathy")
				player.serverScroll = 0
			if (key == "key.hexical.evoke")
				ServerEvocationManager.startEvocation(player, server)
		}

		ServerPlayNetworking.registerGlobalReceiver(RELEASED_KEY_CHANNEL) { server, player, _, buf, _ ->
			val key = buf.readString()
			player.serverKeybindActive()[key] = false
			player.serverKeybindDuration()[key] = 0
			if (key == "key.hexical.evoke")
				ServerEvocationManager.endEvocation(player, server)
		}

		ServerPlayNetworking.registerGlobalReceiver(SCROLL_CHANNEL) { _, player, _, buf, _ ->
			player.serverScroll = player.serverScroll + buf.readInt()
		}
	}
}