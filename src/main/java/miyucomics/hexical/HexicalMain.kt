package miyucomics.hexical

import miyucomics.hexical.inits.*
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import net.minecraft.util.math.random.Random

class HexicalMain : ModInitializer {
	override fun onInitialize() {
		HexicalActions.init()
		HexicalAdvancements.init()
		HexicalBlocks.init()
		HexicalEntities.init()
		HexicalIota.init()
		HexicalItems.init()
		HexicalParticles.init()
		HexicalSounds.init()
		HexicalHooksServer.init()
	}

	companion object {
		const val MOD_ID: String = "hexical"
		@JvmField val RANDOM: Random = Random.create()
		@JvmStatic fun id(string: String) = Identifier(MOD_ID, string)
	}
}