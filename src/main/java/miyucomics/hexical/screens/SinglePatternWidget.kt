package miyucomics.hexical.screens

import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.TAU
import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.EmptyWidget
import net.minecraft.client.render.*
import net.minecraft.util.math.Vec2f
import kotlin.math.cos
import kotlin.math.sin

class SinglePatternWidget(pattern: HexPattern?, private val x: Int, private val y: Int) : EmptyWidget(x, y, 10, 10), Drawable {
	private var points: List<Vec2f>? = listOf()
	init {
		setPattern(pattern)
	}

	fun setPattern(pattern: HexPattern?) {
		if (pattern == null) {
			this.points = null
			return
		}
		this.points = RenderUtils.getNormalizedStrokes(pattern).map { Vec2f(it.x, -it.y).multiply(15f) }
	}

	override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, f: Float) {
		val matrices = drawContext.matrices.peek()
		val buffer = Tessellator.getInstance().buffer
		RenderSystem.setShader(GameRenderer::getPositionColorProgram)

		if (points != null) {
			buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
			fun makeVertex(pos: Vec2f) = buffer.vertex(matrices.positionMatrix, pos.x + x, pos.y + y, 0f).color(1f, 1f, 1f, 1f).next()
			RenderUtils.quadifyLines(::makeVertex, 0.5f, points!!)
		} else {
			buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR)
			for (i in 0..6) {
				val theta = -i.toFloat() / 6f * TAU.toFloat()
				buffer.vertex(matrices.positionMatrix, cos(theta) * 2 + x, sin(theta) * 2 + y, 0f).color(1f, 1f, 1f, 1f).next()
			}
		}

		BufferRenderer.drawWithGlobalProgram(buffer.end())
	}
}