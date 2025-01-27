package miyucomics.hexical.screens

import miyucomics.hexical.client.ClientStorage
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class LedgerScreen : Screen(Text.literal("Ledger")) {
	private val widgets: MutableList<SinglePatternWidget> = mutableListOf()

	override fun shouldPause() = false

	override fun init() {
		val heightPermitted = (height * 0.5).toInt()
		val heightPadding = (height - heightPermitted) / 2
		val widthPermitted = width - 2 * heightPadding
		val widthPadding = (width - widthPermitted) / 2

		widgets.clear()
		val ledgerData = ClientStorage.ledger.ledger.buffer()
		for (i in 0..31) {
			val x = (i % 8).toFloat() / 7.0 * widthPermitted
			val y = (i / 8).toFloat() / 3.0 * heightPermitted
			val widget = SinglePatternWidget(ledgerData.getOrNull(i), widthPadding + x.toInt(), heightPadding + y.toInt())
			widgets.add(widget)
			addDrawable(widget)
		}
	}

	override fun tick() {
		val ledgerData = ClientStorage.ledger.ledger.buffer()
		for (i in 0..31)
			widgets[i].setPattern(ledgerData.getOrNull(i))
	}
}