package miyucomics.hexical

import miyucomics.hexical.data.PrestidigitationData
import miyucomics.hexical.inits.*
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import java.util.*

class HexicalMain : ModInitializer {
	override fun onInitialize() {
		HexicalAdvancements.init()
		HexicalBlocks.init()
		HexicalData.init()
		HexicalEntities.init()
		HexicalEvents.init()
		HexicalIota.init()
		HexicalItems.init()
		HexicalSounds.init()
		HexicalPatterns.init()
		HexicalNetworking.serverInit()
		PrestidigitationData.init()
	}

	companion object {
		const val MOD_ID: String = "hexical"
		@JvmField
		val RANDOM: Random = Random()
		const val EVOKE_DURATION: Int = 20

		fun id(string: String?): Identifier {
			return Identifier(MOD_ID, string)
		}
	}
}