package miyucomics.hexical.screens

import at.petrak.hexcasting.api.casting.math.HexPattern
import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.render.*
import net.minecraft.text.Text
import net.minecraft.util.math.Vec2f

class PatternWidget(pattern: HexPattern?, private val x: Int, private val y: Int) : ClickableWidget(x, y, 10, 10, Text.empty()) {
	private var points: List<Vec2f>? = listOf()
	init {
		setPattern(pattern)
	}

	fun setPattern(pattern: HexPattern?) {
		if (pattern == null) {
			this.points = null
			return
		}
		this.points = RenderUtils.getNormalizedStrokes(pattern).map { it.multiply(15f) }
	}

	override fun renderButton(drawContext: DrawContext, mouseX: Int, mouseY: Int, f: Float) {
		if (points != null) {
			val matrices = drawContext.matrices.peek()
			val buffer = Tessellator.getInstance().buffer
			RenderSystem.setShader(GameRenderer::getPositionColorProgram)
			buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
			fun makeVertex(pos: Vec2f) {
				buffer
					.vertex(matrices.positionMatrix, pos.x + x, pos.y + y, 0f)
					.color((0xff_ffffff).toInt())
					.next()
			}
			RenderUtils.quadifyLines(::makeVertex, 0.5f, points!!)
			BufferRenderer.drawWithGlobalProgram(buffer.end())
		}
	}

	override fun appendClickableNarrations(narrationMessageBuilder: NarrationMessageBuilder) {}
}