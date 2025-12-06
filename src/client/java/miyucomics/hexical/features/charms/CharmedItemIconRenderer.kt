package miyucomics.hexical.features.charms

import com.samsthenerd.inline.api.client.extrahooks.ItemOverlayRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack
import net.minecraft.util.math.ColorHelper

object CharmedItemIconRenderer : ItemOverlayRenderer {
    override fun isActive(stack: ItemStack) = CharmUtilities.isStackCharmed(stack) && CharmUtilities.getIcon(stack) != null
    override fun render(stack: ItemStack, graphics: DrawContext) {
        val color = CharmUtilities.getIcon(stack)!!
        graphics.fill(2, 2, 5, 5, ColorHelper.Argb.lerp(0.8f, (0xff_000000).toInt(), color))
        graphics.fill(1, 1, 4, 4, color)
    }
}