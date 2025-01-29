package miyucomics.hexical.screens

import miyucomics.hexical.client.ClientStorage
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.widget.EmptyWidget
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MishapWidget(private val x: Int, private val y: Int, width: Int, height: Int) : EmptyWidget(x, y, width, height), Drawable {
	override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, f: Float) {
		val renderer = MinecraftClient.getInstance().textRenderer
		drawContext.drawTextWithShadow(renderer, Text.translatable("hexical.ledger.mishap").formatted(Formatting.BOLD), x, y, 0xffffff)
		var i = 1
		renderer.wrapLines(ClientStorage.ledger.mishap, width).forEach {
			drawContext.drawTextWithShadow(renderer, it, x, y + i * 16, 0xffffff)
			i++
		}
	}
}