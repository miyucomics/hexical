package miyucomics.hexical.screens

import miyucomics.hexical.client.ClientStorage
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.MultilineTextWidget
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.text.Text

class LedgerScreen : Screen(Text.literal("Ledger")) {
	private var mishap: TextWidget? = null
	private var stack: MultilineTextWidget? = null
	private var patterns: MultilineTextWidget? = null

	override fun shouldPause() = false

	override fun init() {
		val padding = height - (height * 0.95).toInt()
		val columnWidth = (width / 2) - padding * 2

		mishap = TextWidget(width / 2, 0, 100, 100, ClientStorage.ledger.mishap, client!!.textRenderer)
		stack = MultilineTextWidget(padding, padding, ClientStorage.ledger.stack.buffer().fold(Text.empty()) { acc, text -> acc.append(text).append("\n") }, client!!.textRenderer)
		patterns = MultilineTextWidget(width - columnWidth - padding, padding, ClientStorage.ledger.ledger.buffer().fold(Text.empty()) { acc, text -> acc.append(text.toString()) }, client!!.textRenderer)

		addDrawableChild(stack)
		addDrawableChild(mishap)
		addDrawableChild(patterns)
	}

	override fun tick() {
		mishap!!.message = ClientStorage.ledger.mishap
		stack!!.message = ClientStorage.ledger.stack.buffer().fold(Text.empty()) { acc, text -> acc.append(text).append("\n") }
		patterns!!.message = ClientStorage.ledger.ledger.buffer().fold(Text.empty()) { acc, text -> acc.append(text.toString()) }
	}

	override fun render(drawContext: DrawContext?, i: Int, j: Int, f: Float) {
		super.render(drawContext, i, j, f)
	}
}