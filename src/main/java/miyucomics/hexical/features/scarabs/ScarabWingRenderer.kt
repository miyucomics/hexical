package miyucomics.hexical.features.scarabs

import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.misc.InitHook
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.util.Identifier

object ScarabWingRenderer : InitHook() {
	override fun init() {
		ModelPredicateProviderRegistry.register(HexicalItems.SCARAB_BEETLE_ITEM, Identifier("active")) { stack, _, _, _ ->
			if (stack.nbt?.getBoolean("active") == true)
				1.0f
			else
				0.0f
		}
	}
}