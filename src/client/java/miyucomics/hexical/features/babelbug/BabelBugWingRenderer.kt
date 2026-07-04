package miyucomics.hexical.features.babelbug

import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.misc.InitHook
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.util.Identifier

object BabelBugWingRenderer : InitHook() {
	override fun init() {
		ModelPredicateProviderRegistry.register(HexicalItems.BABELBUG_ITEM, Identifier("active")) { stack, _, _, _ ->
			if (stack.nbt?.getBoolean("active") == true)
				1.0f
			else
				0.0f
		}
	}
}