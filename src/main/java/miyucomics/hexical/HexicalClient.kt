package miyucomics.hexical

import miyucomics.hexical.inits.*
import net.fabricmc.api.ClientModInitializer

class HexicalClient : ClientModInitializer {
	override fun onInitializeClient() {
		HexicalBlocks.clientInit()
		HexicalEntities.clientInit()
		HexicalItems.clientInit()
		HexicalKeybinds.init()
		HexicalNetworking.clientInit()
	}
}