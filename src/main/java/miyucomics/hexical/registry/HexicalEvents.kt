package miyucomics.hexical.registry

import ladysnake.satin.api.event.ShaderEffectRenderCallback
import ladysnake.satin.api.managed.ManagedShaderEffect
import miyucomics.hexical.state.EvokeState
import miyucomics.hexical.state.KeybindData
import miyucomics.hexical.state.PersistentStateHandler
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object HexicalEvents {
	var SHADER: ManagedShaderEffect? = null

	@JvmStatic
	fun init() {
		ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
			val player = handler.player
			if (PersistentStateHandler.getShader(player) != null) {
				val packet = PacketByteBufs.create()
				packet.writeString(PersistentStateHandler.getShader(player)!!.toString())
				ServerPlayNetworking.send(player, HexicalNetworking.SYNC_SHADER_CHANNEL, packet)
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
		ClientPlayConnectionEvents.DISCONNECT.register { _, _ -> SHADER = null }
		ShaderEffectRenderCallback.EVENT.register(ShaderEffectRenderCallback { tickDelta: Float -> SHADER?.render(tickDelta) })
	}
}