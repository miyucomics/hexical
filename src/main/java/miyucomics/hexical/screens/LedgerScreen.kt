package miyucomics.hexical.screens

import miyucomics.hexical.client.ClientStorage
import miyucomics.hexical.registry.HexicalNetworking
import miyucomics.hexical.state.LedgerData
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class LedgerScreen : Screen(Text.literal("Ledger")) {
	private val patternWidgets: MutableList<DisplayWidget> = mutableListOf()
	override fun shouldPause() = false

	override fun init() {
		val padding = 20
		val heightPermitted = height - 2 * padding
		val widthPermitted = width - 2 * padding
		val panelWidth = widthPermitted / 2 - padding

		addDrawable(StackWidget(padding, padding, panelWidth, heightPermitted))
		addDrawable(MishapWidget(width / 2 + padding, padding * 2 + heightPermitted / 2, panelWidth, heightPermitted / 2 - padding))

		patternWidgets.clear()
		val ledgerData = ClientStorage.ledger.ledger.buffer()
		for (i in 0..31) {
			val x = (i % 8).toFloat() / 7.0 * (panelWidth - DisplayWidget.PATTERN_SIZE)
			val y = (i / 8).toFloat() / 3.0 * (heightPermitted / 2 - DisplayWidget.PATTERN_SIZE * 2)
			val widget = DisplayWidget(
				ledgerData.getOrNull(i),
				width / 2 + padding + x.toInt(),
				padding + y.toInt()
			)
			patternWidgets.add(widget)
			addDrawable(widget)
		}
	}

	override fun tick() {
		val ledgerData = ClientStorage.ledger.ledger.buffer()
		for (i in 0..31)
			patternWidgets[i].setPattern(ledgerData.getOrNull(i))
	}

	override fun render(drawContext: DrawContext, i: Int, j: Int, f: Float) {
		drawContext.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680)
		drawContext.drawBorder(width / 2, 0, 1, height, (0xffffffff).toInt())
		drawContext.drawBorder(width / 2, height / 2, width / 2, 1, (0xffffffff).toInt())
		super.render(drawContext, i, j, f)
	}

	override fun mouseClicked(mouseX: Double, mouseY: Double, type: Int): Boolean {
		patternWidgets.forEach { widget ->
			if (mouseX >= widget.x && mouseY >= widget.y && mouseX < widget.x + widget.width && mouseY < widget.y + widget.height) {
				ClientPlayNetworking.send(HexicalNetworking.LEDGER_CHANNEL, PacketByteBufs.empty())
				ClientStorage.ledger = LedgerData()
				return true
			}
		}
		return false
	}
}