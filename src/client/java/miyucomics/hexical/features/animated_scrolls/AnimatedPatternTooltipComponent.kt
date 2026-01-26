package miyucomics.hexical.features.animated_scrolls

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.client.render.*
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.item.TooltipData
import net.minecraft.util.Identifier

class AnimatedPatternTooltipComponent(tooltip: AnimatedPatternTooltip) : TooltipComponent {
	private val color: Int = tooltip.color
	private val fadedColor = (color and 0x00FFFFFF) or (64 shl 24)
	private val background = if (tooltip.state == 1) ANCIENT_BG else PRISTINE_BG
	private val patternlike = HexPatternLike.of(tooltip.pattern)
	private val patternSettings = WorldlyPatternRenderHelpers.WORLDLY_SETTINGS_WOBBLY
	private val staticPoints = HexPatternPoints.getStaticPoints(patternlike, patternSettings, 0.0)
	private val nonzappyLines = patternlike.nonZappyPoints
	private val thickness = patternSettings.getInnerWidth(staticPoints.finalScale).toFloat()

	override fun drawItems(font: TextRenderer?, mouseX: Int, mouseY: Int, graphics: DrawContext) {
		val matrices = graphics.matrices
		val positionMatrix = matrices.peek().positionMatrix

		matrices.push()
		matrices.translate(mouseX.toFloat(), mouseY.toFloat(), 500f)
		RenderSystem.enableBlend()

		graphics.drawTexture(background, 0, 0, RENDER_SIZE, RENDER_SIZE, 0f, 0f, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE)

		matrices.translate(0f, 0f, 100f)
		matrices.scale(RENDER_SIZE.toFloat(), RENDER_SIZE.toFloat(), 1f)

		val oldShader = RenderSystem.getShader()
		val zappyPattern = makeZappy(nonzappyLines, findDupIndices(nonzappyLines), patternSettings.hops, patternSettings.variance, patternSettings.speed, patternSettings.flowIrregular, patternSettings.readabilityOffset, patternSettings.lastSegmentProp, 0.0)

		val points = staticPoints.scaleVecs(zappyPattern)
		drawLineSeq(positionMatrix, points, thickness * 1.5f, fadedColor, fadedColor, VCDrawHelper.getHelper(null, matrices, 0.001f))
		drawLineSeq(positionMatrix, points, thickness, color, color, VCDrawHelper.getHelper(null, matrices, 0.002f))
		RenderSystem.setShader { oldShader }

		matrices.pop()
	}

	override fun getWidth(renderer: TextRenderer) = RENDER_SIZE
	override fun getHeight() = RENDER_SIZE

	companion object {
		private val ANCIENT_BG: Identifier = HexAPI.modLoc("textures/gui/scroll_ancient.png")
		private val PRISTINE_BG: Identifier = HexAPI.modLoc("textures/gui/scroll.png")

		private const val RENDER_SIZE = 128
		private const val TEXTURE_SIZE = 48

		fun tryConvert(data: TooltipData): TooltipComponent? {
			if (data is AnimatedPatternTooltip)
				return AnimatedPatternTooltipComponent(data)
			return null
		}
	}
}