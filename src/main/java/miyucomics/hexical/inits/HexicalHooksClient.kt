package miyucomics.hexical.inits

import miyucomics.hexical.features.evocation.ClientEvocationReceiver
import miyucomics.hexical.features.lesser_sentinel.ClientLesserSentinelReceiver
import miyucomics.hexical.features.lesser_sentinel.LesserSentinelRenderer
import miyucomics.hexical.features.lesser_sentinel.ServerLesserSentinelPusher
import miyucomics.hexical.features.peripherals.ClientPeripheralPusher
import miyucomics.hexical.features.peripherals.ServerPeripheralReceiver

object HexicalHooksClient {
	private val hooks = mutableListOf<Hook>()
	fun registerHook(hook: Hook) { hooks.add(hook) }

	fun init() {
		registerHook(ClientPeripheralPusher)
		registerHook(ClientEvocationReceiver)
		registerHook(ClientLesserSentinelReceiver)
		registerHook(LesserSentinelRenderer)
		for (hook in hooks)
			hook.registerCallbacks()
	}
}

object HexicalHooksServer {
	private val hooks = mutableListOf<Hook>()
	fun registerHook(hook: Hook) { hooks.add(hook) }

	fun init() {
		registerHook(ServerPeripheralReceiver)
		registerHook(ServerLesserSentinelPusher)
		for (hook in hooks)
			hook.registerCallbacks()
	}
}

abstract class Hook {
	abstract fun registerCallbacks()
}