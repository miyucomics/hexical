package miyucomics.hexical.inits

import miyucomics.hexical.features.autographs.AutographTooltip
import miyucomics.hexical.features.charms.CharmedItemTooltip
import miyucomics.hexical.features.confetti.ClientConfettiReceiver
import miyucomics.hexical.features.curios.CompassCurioModelHook
import miyucomics.hexical.features.curios.FluteCurioItemModel
import miyucomics.hexical.features.curios.HandbellCurioItemModel
import miyucomics.hexical.features.dyes.DyeDataHook
import miyucomics.hexical.features.evocation.ClientEvocationReceiver
import miyucomics.hexical.features.jailbreak.JailbrokenItemTooltip
import miyucomics.hexical.features.lamps.ArchLampModelProvider
import miyucomics.hexical.features.lesser_sentinels.ClientLesserSentinelReceiver
import miyucomics.hexical.features.lesser_sentinels.LesserSentinelRenderer
import miyucomics.hexical.features.mage_blocks.MageBlockModelLoadingHook
import miyucomics.hexical.features.mage_blocks.MageBlockScryingOverlay
import miyucomics.hexical.features.media_jar.MediaJarRenderHooks
import miyucomics.hexical.features.media_jar.MediaJarShader
import miyucomics.hexical.features.media_log.ClientMediaLogReceiver
import miyucomics.hexical.features.media_log.MediaLogRenderer
import miyucomics.hexical.features.pedestal.PedestalRenderHooks
import miyucomics.hexical.features.player.PlayerAnimatorHook
import miyucomics.hexical.features.scarabs.ScarabWingRenderer
import miyucomics.hexical.features.shaders.ClientShaderReceiver
import miyucomics.hexical.features.telepathy.ClientPeripheralPusher
import miyucomics.hexical.misc.InitHook

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
		register(JailbrokenItemTooltip)
		register(ClientShaderReceiver)
		register(ScarabWingRenderer)
		register(MediaJarRenderHooks)
		register(MediaJarShader)
		register(ArchLampModelProvider)
		register(CompassCurioModelHook)
		register(FluteCurioItemModel)
		register(HandbellCurioItemModel)
		register(PlayerAnimatorHook)
		register(MageBlockScryingOverlay)
		register(PedestalRenderHooks)
		register(MageBlockModelLoadingHook)
		register(DyeDataHook)

		for (hook in hooks)
			hook.init()
	}
}