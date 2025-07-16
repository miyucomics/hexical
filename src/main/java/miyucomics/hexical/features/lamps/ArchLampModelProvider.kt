package miyucomics.hexical.features.lamps

import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.inits.InitHook
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.util.Identifier

object ArchLampModelProvider : InitHook() {
	override fun init() {
		ModelPredicateProviderRegistry.register(HexicalItems.ARCH_LAMP_ITEM, Identifier("active")) { stack, _, _, _ ->
			if (stack.nbt?.getBoolean("active") == true) 1.0f else 0.0f
		}
	}
}