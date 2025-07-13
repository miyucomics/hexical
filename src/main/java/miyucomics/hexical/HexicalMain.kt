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
		HexicalData.init()
		HexicalEntities.init()
		HexicalEvents.init()
		HexicalIota.init()
		HexicalItems.init()
		HexicalParticles.init()
		HexicalPotions.init()
		HexicalRecipe.init()
		HexicalSounds.init()
		HexicalNetworking.serverInit()
	}

	companion object {
		const val MOD_ID: String = "hexical"

		@JvmField
		val RANDOM: Random = Random.create()
		const val EVOKE_DURATION: Int = 20

		@JvmStatic
		fun id(string: String) = Identifier(MOD_ID, string)
	}
}