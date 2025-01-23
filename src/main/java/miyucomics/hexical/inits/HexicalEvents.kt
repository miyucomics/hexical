package miyucomics.hexical.inits

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import miyucomics.hexical.casting.components.LedgerComponent
import miyucomics.hexical.casting.components.SentinelBedComponent
import miyucomics.hexical.client.ClientStorage
import miyucomics.hexical.client.ShaderRenderer
import miyucomics.hexical.state.EvokeState
import miyucomics.hexical.state.KeybindData
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.nbt.NbtCompound

object HexicalEvents {
	@JvmStatic
	fun init() {
		CastingEnvironment.addCreateEventListener { env: CastingEnvironment, _: NbtCompound ->
			env.addExtension(SentinelBedComponent(env))
			if (env is PlayerBasedCastEnv)
				env.addExtension(LedgerComponent(env))
		}

		ServerPlayerEvents.AFTER_RESPAWN.register { _, _, alive ->
			if (!alive)
				ShaderRenderer.setEffect(null)
		}

		ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
			val player = handler.player.uuid
			EvokeState.active[player] = false
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
					EvokeState.duration[player] = EvokeState.duration[player]!! - 1
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
		ClientPlayConnectionEvents.DISCONNECT.register { _, _ -> ShaderRenderer.setEffect(null) }
		ClientTickEvents.END_CLIENT_TICK.register { ClientStorage.time += 1 }
	}
}