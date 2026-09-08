package miyucomics.hexical.feature.dyeing

import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

class DyeingItemPage : AbstractDyeingPage<ItemStack>() {
	override fun parseProvided(name: String): ItemStack = Registries.ITEM.get(Identifier(name)).defaultStack
	override fun renderCustom(graphics: DrawContext, thing: ItemStack, x: Int, y: Int, mouseX: Int, mouseY: Int, tickDelta: Float) {
		graphics.drawItem(thing, x - 8, y - 8)
	}
}