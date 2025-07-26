package miyucomics.hexical.inits

import miyucomics.hexical.features.charms.ServerCharmedUseReceiver
import miyucomics.hexical.features.dyes.DyeDataHook
import miyucomics.hexical.features.hopper.HopperEndpointRegistry
import miyucomics.hexical.features.lesser_sentinels.ServerLesserSentinelPusher
import miyucomics.hexical.features.media_log.ServerSpyingHooks
import miyucomics.hexical.features.peripherals.ServerPeripheralReceiver
import miyucomics.hexical.features.periwinkle.WooleyedEffectRegister
import miyucomics.hexical.features.player.RespawnPersistHook
import miyucomics.hexical.features.prestidigitation.PrestidigitationHandlersHook
import miyucomics.hexical.features.scarabs.ScarabHandler
import miyucomics.hexical.features.sentinel_beds.SentinelBedAmbitHook
import miyucomics.hexical.features.sentinel_beds.SentinelBedPoi
import miyucomics.hexical.features.shaders.ServerShaderManager
import miyucomics.hexical.features.transmuting.TransmutingHelper
import miyucomics.hexical.misc.InitHook

object HexicalHooksServer {
	private val hooks = mutableListOf<InitHook>()
	fun register(hook: InitHook) { hooks.add(hook) }

	fun init() {
		register(ServerCharmedUseReceiver)
		register(ServerLesserSentinelPusher)
		register(ServerPeripheralReceiver)
		register(ServerSpyingHooks)
		register(ServerShaderManager)
		register(RespawnPersistHook)
		register(SentinelBedAmbitHook)
		register(PrestidigitationHandlersHook)
		register(HopperEndpointRegistry)
		register(DyeDataHook)
		register(TransmutingHelper)
		register(ScarabHandler)
		register(WooleyedEffectRegister)
		register(SentinelBedPoi)

		for (hook in hooks)
			hook.init()
	}
}