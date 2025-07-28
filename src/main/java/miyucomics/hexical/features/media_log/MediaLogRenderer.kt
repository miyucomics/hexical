package miyucomics.hexical.features.media_log

import at.petrak.hexcasting.client.render.*
import miyucomics.hexical.misc.ClientStorage
import miyucomics.hexical.misc.ClientStorage.ticks
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.MathHelper
import kotlin.math.max
import kotlin.math.min

object MediaLogRenderer : InitHook() {
	const val FADE_IN_DURATION: Int = 40
	var fadingInLog = false
	var fadingInLogStart = 0
	var fadingInLogTweener = 0

	override fun init() {
		ClientTickEvents.END_CLIENT_TICK.register {
			fadingInLogTweener = if (fadingInLog) min(ticks - fadingInLogStart, FADE_IN_DURATION) else max(fadingInLogTweener - 5, 0)
		}

		HudRenderCallback.EVENT.register { context, tickDelta ->
			if (fadingInLogTweener == 0) return@register
			val progress = (fadingInLogTweener + tickDelta) / FADE_IN_DURATION.toFloat()

			val backgroundColor = ColorHelper.Argb.getArgb((progress * 100).toInt(), 0, 0, 0)
			context.fillGradient(0, 0, context.scaledWindowWidth, context.scaledWindowHeight, backgroundColor, backgroundColor)

			context.matrices.push()
			context.matrices.translate(context.scaledWindowWidth / 2f, context.scaledWindowHeight / 2f, 0f)

			for (phase in phases) {
				val localProgress = (progress - phase.start) / phase.duration
				if (localProgress > 0f)
					phase.render(context, MathHelper.clamp(localProgress, 0f, 1f))
			}

			context.matrices.pop()
		}
	}

	fun drawMishapText(context: DrawContext, alpha: Float) {
		val mishapText = ClientStorage.mediaLog.mishap
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, mishapText, 0, -context.scaledWindowHeight / 2 + 10, ColorHelper.Argb.getArgb((alpha * 255).toInt(), 255, 255, 255))
	}

	fun drawMediaLogPattern(matrices: MatrixStack, index: Int, alpha: Float) {
		matrices.push()
		matrices.translate((index - 8).toFloat() * 30f - 12.5f, -12.5f, 0f)
		matrices.scale(25f, 25f, 25f)

		val color = ColorHelper.Argb.getArgb((alpha * 255).toInt(), 255, 255, 255)
		val patternlike = HexPatternLike.of(ClientStorage.mediaLog.patterns.buffer()[index])
		val patternSettings = WorldlyPatternRenderHelpers.WORLDLY_SETTINGS_WOBBLY
		val staticPoints = HexPatternPoints.getStaticPoints(patternlike, patternSettings, 0.0)
		val nonzappyLines = patternlike.nonZappyPoints
		val zappyPattern = makeZappy(nonzappyLines, findDupIndices(nonzappyLines), patternSettings.hops, patternSettings.variance, patternSettings.speed, patternSettings.flowIrregular, patternSettings.readabilityOffset, patternSettings.lastSegmentProp, 0.0)
		drawLineSeq(matrices.peek().getPositionMatrix(), staticPoints.scaleVecs(zappyPattern), 0.05f, color, color, VCDrawHelper.getHelper(null, matrices, 0.001f))

		matrices.pop()
	}

	fun drawStackItem(context: DrawContext, index: Int, alpha: Float) {
		if (index >= ClientStorage.mediaLog.stack.buffer().size || alpha == 0f)
			return
		context.matrices.push()
		val iotas = ClientStorage.mediaLog.stack.buffer()
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, iotas[index], 17, 16 * (4 - index), ColorHelper.Argb.getArgb((alpha * 255).toInt(), 255, 255, 255))
		context.matrices.pop()
	}

	private val phases = listOf(
		Phase(0.0f, 0.2f) { ctx, t ->
			drawMishapText(ctx, t)
		},
		Phase(0.2f, 0.5f) { ctx, t ->
			val progress = MathHelper.clamp(t, 0f, 1f)
			val visible = (progress * 16).toInt()
			val alpha = (progress * 16) % 1
			for (i in 0 until visible)
				drawMediaLogPattern(ctx.matrices, i, 1f)
			if (visible < 15)
				drawMediaLogPattern(ctx.matrices, visible, alpha)
		},
		Phase(0.7f, 0.3f) { ctx, t ->
			val progress = MathHelper.clamp(t, 0f, 1f)
			val visible = (progress * 8).toInt()
			val alpha = (progress * 8) % 1
			for (i in 0 until visible)
				drawStackItem(ctx, i, 1f)
			if (visible < 7)
				drawStackItem(ctx, visible, alpha)
		}
	)

	private data class Phase(
		val start: Float,
		val duration: Float,
		val render: (DrawContext, Float) -> Unit
	)
}

