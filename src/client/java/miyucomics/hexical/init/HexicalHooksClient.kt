package miyucomics.hexical.init

import miyucomics.hexical.feature.autograph.AutographTooltip
import miyucomics.hexical.feature.babelbug.BabelBugWingRenderer
import miyucomics.hexical.feature.charming.CharmedItemTooltip
import miyucomics.hexical.feature.confetti.ClientConfettiReceiver
import miyucomics.hexical.feature.curio.CompassCurioModelHook
import miyucomics.hexical.feature.curio.FluteCurioItemModel
import miyucomics.hexical.feature.curio.HandbellCurioItemModel
import miyucomics.hexical.feature.evocation.ClientEvocationReceiver
import miyucomics.hexical.feature.genie_lamp.ArchLampModelProvider
import miyucomics.hexical.feature.jailbreak.JailbrokenItemTooltip
import miyucomics.hexical.feature.lesser_sentinel.ClientLesserSentinelReceiver
import miyucomics.hexical.feature.lesser_sentinel.LesserSentinelRenderer
import miyucomics.hexical.feature.media_log.ClientMediaLogReceiver
import miyucomics.hexical.feature.media_log.MediaLogRenderer
import miyucomics.hexical.feature.player_animation.PlayerAnimatorHook
import miyucomics.hexical.feature.shader.ClientShaderReceiver
import miyucomics.hexical.feature.telepathy.ClientPeripheralPusher
import miyucomics.hexical.feature.toasting.ToastReceiver
import miyucomics.hexical.feature.zap.ZapShaderHook
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
		register(BabelBugWingRenderer)
		register(ArchLampModelProvider)
		register(CompassCurioModelHook)
		register(FluteCurioItemModel)
		register(HandbellCurioItemModel)
		register(PlayerAnimatorHook)
		register(ToastReceiver)
		register(ZapShaderHook)

		for (hook in hooks)
			hook.init()
	}
}