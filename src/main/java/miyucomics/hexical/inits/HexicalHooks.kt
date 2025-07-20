package miyucomics.hexical.inits

import miyucomics.hexical.features.autographs.AutographTooltip
import miyucomics.hexical.features.charms.CharmedItemTooltip
import miyucomics.hexical.features.charms.ServerCharmedUseReceiver
import miyucomics.hexical.features.confetti.ClientConfettiReceiver
import miyucomics.hexical.features.cracked_items.CrackedItemTooltip
import miyucomics.hexical.features.curios.curios.CompassCurioModelHook
import miyucomics.hexical.features.curios.curios.HandbellCurioItemModel
import miyucomics.hexical.features.dyes.DyeDataHook
import miyucomics.hexical.features.evocation.ClientEvocationReceiver
import miyucomics.hexical.features.hopper.HopperEndpointRegistry
import miyucomics.hexical.features.lamps.ArchLampModelProvider
import miyucomics.hexical.features.lesser_sentinels.ClientLesserSentinelReceiver
import miyucomics.hexical.features.lesser_sentinels.LesserSentinelRenderer
import miyucomics.hexical.features.lesser_sentinels.ServerLesserSentinelPusher
import miyucomics.hexical.features.media_jar.MediaJarRenderHooks
import miyucomics.hexical.features.media_jar.MediaJarShader
import miyucomics.hexical.features.media_log.ClientMediaLogReceiver
import miyucomics.hexical.features.media_log.MediaLogRenderer
import miyucomics.hexical.features.media_log.ServerSpyingHooks
import miyucomics.hexical.features.peripherals.ClientPeripheralPusher
import miyucomics.hexical.features.peripherals.ServerPeripheralReceiver
import miyucomics.hexical.features.periwinkle.WooleyedEffectRegister
import miyucomics.hexical.features.player.PlayerAnimatorHook
import miyucomics.hexical.features.player.RespawnPersistHook
import miyucomics.hexical.features.prestidigitation.PrestidigitationHandlersHook
import miyucomics.hexical.features.scarabs.ScarabHandler
import miyucomics.hexical.features.scarabs.ScarabWingRenderer
import miyucomics.hexical.features.sentinel_beds.SentinelBedAmbitHook
import miyucomics.hexical.features.shaders.ClientShaderReceiver
import miyucomics.hexical.features.shaders.ServerShaderManager
import miyucomics.hexical.features.transmuting.TransmutingHelper

object HexicalHooksClient {
	private val hooks = mutableListOf<InitHook>()
	fun register(hook: InitHook) { hooks.add(hook) }

	fun init() {
		register(ClientConfettiReceiver)
		register(ClientEvocationReceiver)
		register(ClientLesserSentinelReceiver)
		register(LesserSentinelRenderer)
		register(ClientPeripheralPusher)
		register(ClientMediaLogReceiver)
		register(MediaLogRenderer)
		register(AutographTooltip)
		register(CharmedItemTooltip)
		register(CrackedItemTooltip)
		register(ClientShaderReceiver)
		register(CompassCurioModelHook)
		register(ScarabWingRenderer)
		register(MediaJarRenderHooks)
		register(MediaJarShader)
		register(ArchLampModelProvider)
		register(HandbellCurioItemModel)
		register(PlayerAnimatorHook)

		for (hook in hooks)
			hook.init()
	}
}

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

		for (hook in hooks)
			hook.init()
	}
}

abstract class InitHook {
	abstract fun init()
}