package miyucomics.hexical.features.player.fields

import at.petrak.hexcasting.fabric.event.MouseScrollCallback
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.player.PlayerField
import miyucomics.hexical.features.player.getHexicalPlayerManager
import miyucomics.hexical.inits.HexicalKeybinds
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier

class KeybindField : PlayerField {
	var active: HashMap<String, Boolean> = HashMap()
	var duration: HashMap<String, Int> = HashMap()
	var scroll: Int = 0

	override fun tick(player: PlayerEntity) {
		this.active.keys.filter { this.active[it] == true }.forEach { key ->
			this.duration[key] = this.duration[key]!! + 1
		}
	}

	companion object {
		val PRESSED_KEY_CHANNEL: Identifier = HexicalMain.id("press_key")
		val RELEASED_KEY_CHANNEL: Identifier = HexicalMain.id("release_key")
		val SCROLL_CHANNEL: Identifier = HexicalMain.id("scroll")

		fun registerServerCallbacks() {
			ServerPlayNetworking.registerGlobalReceiver(PRESSED_KEY_CHANNEL) { _, player, _, buf, _ ->
				val key = buf.readString()
				player.serverKeybindActive()[key] = true
				player.serverKeybindDuration()[key] = 0
				if (key == "key.hexical.telepathy")
					player.serverScroll = 0
			}

			ServerPlayNetworking.registerGlobalReceiver(RELEASED_KEY_CHANNEL) { _, player, _, buf, _ ->
				val key = buf.readString()
				player.serverKeybindActive()[key] = false
				player.serverKeybindDuration()[key] = 0
			}

			ServerPlayNetworking.registerGlobalReceiver(SCROLL_CHANNEL) { _, player, _, buf, _ ->
				player.serverScroll = player.serverScroll + buf.readInt()
			}
		}

		fun registerClientCallbacks() {
			MouseScrollCallback.EVENT.register { delta ->
				if (HexicalKeybinds.TELEPATHY_KEYBIND.isPressed) {
					val buf = PacketByteBufs.create()
					buf.writeInt(delta.toInt())
					ClientPlayNetworking.send(SCROLL_CHANNEL, buf)
					return@register true
				}
				return@register false
			}
		}
	}
}

fun PlayerEntity.serverKeybindActive() = this.getHexicalPlayerManager().get(KeybindField::class).active
fun PlayerEntity.serverKeybindDuration() = this.getHexicalPlayerManager().get(KeybindField::class).duration
var PlayerEntity.serverScroll: Int
	get() = this.getHexicalPlayerManager().get(KeybindField::class).scroll
	set(scroll) { this.getHexicalPlayerManager().get(KeybindField::class).scroll = scroll }