package miyucomics.hexical.init

import miyucomics.hexical.feature.babelbug.BabelbugListener
import miyucomics.hexical.feature.charming.ServerCharmedUseReceiver
import miyucomics.hexical.feature.dyeing.DyeingEntityRegistry
import miyucomics.hexical.feature.dyeing.DyeingUtils
import miyucomics.hexical.feature.flora.ConjureFloraHook
import miyucomics.hexical.feature.lesser_sentinel.ServerLesserSentinelPusher
import miyucomics.hexical.feature.player_state.RespawnPersistHook
import miyucomics.hexical.feature.prestidigitation.PrestidigitationHandlers
import miyucomics.hexical.feature.redstone_pin.RedstonePinManager
import miyucomics.hexical.feature.rotate.BlockRotationHandlers
import miyucomics.hexical.feature.sentinel_bed.SentinelBedAmbitHook
import miyucomics.hexical.feature.sentinel_bed.SentinelBedPoi
import miyucomics.hexical.feature.shader.ServerShaderManager
import miyucomics.hexical.feature.telepathy.ServerPeripheralReceiver
import miyucomics.hexical.feature.transmuting.TransmutingHelper
import miyucomics.hexical.misc.InitHook

object HexicalHooksServer {
	private val hooks = mutableListOf<InitHook>()
	fun register(hook: InitHook) { hooks.add(hook) }

	fun init() {
		register(ServerCharmedUseReceiver)
		register(ServerLesserSentinelPusher)
		register(ServerPeripheralReceiver)
		register(ServerShaderManager)
		register(RespawnPersistHook)
		register(SentinelBedAmbitHook)
		register(PrestidigitationHandlers)
		register(TransmutingHelper)
		register(SentinelBedPoi)
		register(BlockRotationHandlers)
		register(DyeingUtils)
		register(DyeingEntityRegistry)
		register(ConjureFloraHook)
		register(RedstonePinManager)
		register(BabelbugListener)

		for (hook in hooks)
			hook.init()
	}
}