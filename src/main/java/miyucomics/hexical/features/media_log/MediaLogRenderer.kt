package miyucomics.hexical.features.media_log

import at.petrak.hexcasting.client.render.*
import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.inits.InitHook
import miyucomics.hexical.misc.ClientStorage
import miyucomics.hexical.misc.RenderUtils
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.command.argument.ColorArgumentType.color
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.MathHelper
import kotlin.math.max
import kotlin.math.min

object MediaLogRenderer : InitHook() {
	const val FADE_IN_DURATION: Int = 40

	override fun init() {
		ClientTickEvents.END_CLIENT_TICK.register {
			if (ClientStorage.fadingInLog)
				ClientStorage.fadingInLogTweener = min(ClientStorage.ticks - ClientStorage.fadingInLogStart, FADE_IN_DURATION)
			else
				ClientStorage.fadingInLogTweener = max(ClientStorage.fadingInLogTweener - 5, 0)
		}

		HudRenderCallback.EVENT.register { context, tickDelta ->
			val progress = ClientStorage.fadingInLogTweener / FADE_IN_DURATION.toFloat()
			if (progress == 0f)
				return@register

			val backgroundColor = ColorHelper.Argb.getArgb((progress * 100).toInt(), 0, 0, 0)
			context.fillGradient(0, 0, context.scaledWindowWidth, context.scaledWindowHeight, backgroundColor, backgroundColor)

//			val progress = 1f

			context.matrices.push()
			context.matrices.translate(context.scaledWindowWidth / 2f, context.scaledWindowHeight / 2f, 0f)

			val mishapAlpha = MathHelper.clamp((progress / 0.2f), 0f, 1f)
			drawMishapText(context, mishapAlpha)

			if (ClientStorage.mediaLog.patterns.buffer().isNotEmpty()) {
				val patternProgress = MathHelper.clamp((progress - 0.2f) / 0.5f, 0f, 1f)
				val patternsVisible = (patternProgress * 16).toInt()
				val patternAlpha = (patternProgress * 16) % 1
				val oldShader = RenderSystem.getShader()

				for (i in 0 until patternsVisible)
					drawMediaLogPattern(context.matrices, i, 1f)
				if (patternsVisible < 15)
					drawMediaLogPattern(context.matrices, patternsVisible, patternAlpha)

				RenderSystem.setShader { oldShader }
			}

			val stackProgress = MathHelper.clamp((progress - 0.7f) / 0.3f, 0f, 1f)
			val stackVisible = (stackProgress * 8).toInt()
			val stackAlpha = (stackProgress * 8) % 1

			for (i in 0 until stackVisible)
				drawStackItem(context, i, 1f)
			if (stackVisible < 7)
				drawStackItem(context, stackVisible, stackAlpha)

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
}