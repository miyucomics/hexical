package miyucomics.hexical.features.peripherals

import at.petrak.hexcasting.fabric.event.MouseScrollCallback
import miyucomics.hexical.inits.HexicalKeybinds
import miyucomics.hexical.inits.InitHook
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.MinecraftClient

object ClientPeripheralPusher : InitHook() {
	private var previousState = mutableMapOf<String, Boolean>()

	override fun init() {
		ClientTickEvents.END_CLIENT_TICK.register { client: MinecraftClient ->
			if (client.player == null)
				return@register

			for (key in listOf(client.options.forwardKey, client.options.leftKey, client.options.rightKey, client.options.backKey, client.options.jumpKey, client.options.sneakKey, client.options.useKey, client.options.attackKey, HexicalKeybinds.TELEPATHY_KEYBIND, HexicalKeybinds.EVOKE_KEYBIND)) {
				if (previousState.keys.contains(key.translationKey)) {
					if (previousState[key.translationKey] == true && !key.isPressed) {
						ClientPlayNetworking.send(ServerPeripheralReceiver.RELEASED_KEY_CHANNEL, PacketByteBufs.create().also { it.writeString(key.translationKey) })
					} else if (previousState[key.translationKey] == false && key.isPressed) {
						ClientPlayNetworking.send(ServerPeripheralReceiver.PRESSED_KEY_CHANNEL, PacketByteBufs.create().also { it.writeString(key.translationKey) })
					}
				}

				previousState[key.translationKey] = key.isPressed
			}
		}

		MouseScrollCallback.EVENT.register { delta ->
			if (HexicalKeybinds.TELEPATHY_KEYBIND.isPressed) {
				val buf = PacketByteBufs.create()
				buf.writeInt(delta.toInt())
				ClientPlayNetworking.send(ServerPeripheralReceiver.SCROLL_CHANNEL, buf)
				return@register true
			}
			return@register false
		}
	}
}