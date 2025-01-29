package miyucomics.hexical.screens

import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.TAU
import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.widget.EmptyWidget
import net.minecraft.client.render.*
import net.minecraft.util.math.Vec2f
import kotlin.math.cos
import kotlin.math.sin

class DisplayWidget(pattern: HexPattern?, private val x: Int, private val y: Int) : EmptyWidget(x, y, PATTERN_SIZE, PATTERN_SIZE), Drawable {
	private var points: List<Vec2f>? = listOf()

	companion object {
		const val PATTERN_SIZE = 14
	}

	init {
		setPattern(pattern)
	}

	fun setPattern(pattern: HexPattern?) {
		if (pattern == null) {
			this.points = null
			return
		}
		this.points = RenderUtils.getNormalizedStrokes(pattern).map { Vec2f(it.x, -it.y).multiply(PATTERN_SIZE.toFloat()) }
	}

	override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, f: Float) {
		val matrices = drawContext.matrices.peek()
		val buffer = Tessellator.getInstance().buffer
		RenderSystem.setShader(GameRenderer::getPositionColorProgram)

		if (points != null) {
			buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
			fun makeVertex(pos: Vec2f) = buffer.vertex(matrices.positionMatrix, pos.x + x + PATTERN_SIZE / 2, pos.y + y + PATTERN_SIZE / 2, 0f).color(1f, 1f, 1f, 1f).next()
			RenderUtils.quadifyLines(::makeVertex, 0.5f, points!!)
		} else {
			buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR)
			for (i in 0..6) {
				val theta = -i.toFloat() / 6f * TAU.toFloat()
				buffer.vertex(matrices.positionMatrix, cos(theta) + x + PATTERN_SIZE / 2, sin(theta) + y + PATTERN_SIZE / 2, 0f).color(0.42f, 0.44f, 0.53f, 1f).next()
			}
		}

		BufferRenderer.drawWithGlobalProgram(buffer.end())
	}
}