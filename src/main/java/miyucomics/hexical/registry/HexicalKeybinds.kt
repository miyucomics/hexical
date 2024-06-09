package miyucomics.hexical.registry

import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.client.PlayerAnimations
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.MinecraftClient
import net.minecraft.client.input.KeyboardInput
import net.minecraft.client.option.KeyBinding
import org.lwjgl.glfw.GLFW

object HexicalKeybinds {
	private val EVOKE_KEYBIND = KeyBinding("key.hexical.evoke", GLFW.GLFW_KEY_R, "key.categories.hexical")
	private val TELEPATHY_KEYBIND = KeyBinding("key.hexical.telepathy", GLFW.GLFW_KEY_G, "key.categories.hexical")
	private val EXIT_SCRYING_KEYBIND = KeyBinding("key.hexical.stop_scrying", GLFW.GLFW_KEY_J, "key.categories.hexical")
	private var states = mutableMapOf<String, Boolean>()

	@JvmStatic
	fun init() {
		KeyBindingHelper.registerKeyBinding(EVOKE_KEYBIND)
		KeyBindingHelper.registerKeyBinding(TELEPATHY_KEYBIND)
		KeyBindingHelper.registerKeyBinding(EXIT_SCRYING_KEYBIND)
		ClientTickEvents.END_CLIENT_TICK.register { client: MinecraftClient ->
			if (client.player == null)
				return@register

			if (EXIT_SCRYING_KEYBIND.isPressed) {
				client.setCameraEntity(client.player);
				client.player!!.input = KeyboardInput(client.options);
			}

			if (states.keys.contains(EVOKE_KEYBIND.translationKey)) {
				val container = (client.player as PlayerAnimations).hexical_getModAnimation()
				if (states[EVOKE_KEYBIND.translationKey] == true && !EVOKE_KEYBIND.isPressed) {
					val frame = PlayerAnimationRegistry.getAnimation(HexicalMain.id("cast_end"))!!
					container.setAnimation(KeyframeAnimationPlayer(frame))
				} else if (states[EVOKE_KEYBIND.translationKey] == false && EVOKE_KEYBIND.isPressed) {
					val frame = PlayerAnimationRegistry.getAnimation(HexicalMain.id("cast_loop"))!!
					container.setAnimation(KeyframeAnimationPlayer(frame))
				}
			}
			states[EVOKE_KEYBIND.translationKey] = EVOKE_KEYBIND.isPressed

			for (key in listOf(client.options.forwardKey, client.options.leftKey, client.options.rightKey, client.options.backKey, TELEPATHY_KEYBIND)) {
				if (states.keys.contains(key.translationKey)) {
					if (states[key.translationKey] == true && !key.isPressed) {
						val buf = PacketByteBufs.create()
						buf.writeString(key.translationKey)
						ClientPlayNetworking.send(HexicalNetworking.RELEASED_KEY_PACKET, buf)
					} else if (states[key.translationKey] == false && key.isPressed) {
						val buf = PacketByteBufs.create()
						buf.writeString(key.translationKey)
						ClientPlayNetworking.send(HexicalNetworking.PRESSED_KEY_PACKET, buf)
					}
				}
				states[key.translationKey] = key.isPressed
			}
		}
	}
}