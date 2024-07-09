package miyucomics.hexical.registry

import ladysnake.satin.api.event.ShaderEffectRenderCallback
import ladysnake.satin.api.managed.ShaderEffectManager
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.state.EvokeState
import miyucomics.hexical.state.KeybindData
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.MinecraftClient

object HexicalEvents {
	private val NIGHT_VISION_SHADER = ShaderEffectManager.getInstance().manage(HexicalMain.id("shaders/post/night_vision.json"))

	@JvmStatic
	fun init() {
		ServerPlayConnectionEvents.JOIN.register { handler, _, server ->
			val player = handler.player.uuid
			if (EvokeState.active[player] == true) {
				for (otherPlayer in server.playerManager.playerList) {
					val packet = PacketByteBufs.create()
					packet.writeUuid(player)
					ServerPlayNetworking.send(otherPlayer, HexicalNetworking.START_EVOKE_CHANNEL, packet)
				}
			}
		}
		ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
			val player = handler.player.uuid
			EvokeState.active[player] = false
			EvokeState.duration[player] = -1
			if (KeybindData.active.containsKey(player)) {
				for (key in KeybindData.active[player]!!.keys) {
					KeybindData.active[player]!![key] = false
					KeybindData.duration[player]!![key] = 0
				}
			}
		}
		ServerTickEvents.END_SERVER_TICK.register {
			for (player in EvokeState.active.keys)
				if (EvokeState.active[player]!!)
					EvokeState.duration[player] = EvokeState.duration[player]!! + 1
			for (player in KeybindData.duration.keys) {
				val binds = KeybindData.active[player]!!
				for (key in binds.keys)
					if (KeybindData.active[player]!!.getOrDefault(key, false))
						KeybindData.duration[player]!![key] = KeybindData.duration[player]!![key]!! + 1
			}
		}
	}

	@JvmStatic
	fun clientInit() {
		ShaderEffectRenderCallback.EVENT.register(ShaderEffectRenderCallback { tickDelta: Float ->
			val player = MinecraftClient.getInstance().player ?: return@ShaderEffectRenderCallback
			if (player.hasStatusEffect(HexicalStatusEffects.MEDIA_VISION_STATUS_EFFECT))
				NIGHT_VISION_SHADER.render(tickDelta)
		})
	}
}