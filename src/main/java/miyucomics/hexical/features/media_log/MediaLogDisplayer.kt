package miyucomics.hexical.features.media_log

import at.petrak.hexcasting.client.render.*
import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.misc.ClientStorage
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.MathHelper
import kotlin.math.max
import kotlin.math.min

object MediaLogDisplayer {
	const val FADE_IN_DURATION: Int = 100

	fun registerClientCallbacks() {
		ClientTickEvents.END_CLIENT_TICK.register {
			if (ClientStorage.fadingInLog)
				ClientStorage.fadingInLogTweener = min(ClientStorage.ticks - ClientStorage.fadingInLogStart, FADE_IN_DURATION)
			else
				ClientStorage.fadingInLogTweener = max(ClientStorage.fadingInLogTweener - 1, 0)
		}

		HudRenderCallback.EVENT.register { context, tickDelta ->
			val progress = ClientStorage.fadingInLogTweener / FADE_IN_DURATION.toFloat()

			val mishapAlpha = MathHelper.clamp((progress / 0.2f), 0f, 1f)
			drawMishapText(mishapAlpha)

			val patternProgress = MathHelper.clamp((progress - 0.2f) / 0.5f, 0f, 1f)
			val patternsVisible = (patternProgress * 16).toInt()
			val patternAlpha = (patternProgress * 16) % 1

			for (i in 0 until patternsVisible)
				drawMediaLogPattern(context.matrices, i, 1f)
			if (patternsVisible < 16)
				drawMediaLogPattern(context.matrices, patternsVisible, patternAlpha)

			val stackProgress = MathHelper.clamp((progress - 0.7f) / 0.3f, 0f, 1f)
			val stackVisible = (stackProgress * 8).toInt()
			val stackAlpha = (stackProgress * 8) % 1

			for (i in 0 until stackVisible)
				drawStackItem(i, 1f)
			if (stackVisible < 8)
				drawStackItem(stackVisible, stackAlpha)
		}
	}

	fun drawMishapText(alpha: Float) {
		val mishapText = ClientStorage.mediaLog.mishap
	}

	fun drawMediaLogPattern(matrices: MatrixStack, index: Int, alpha: Float) {
		val patternlike = HexPatternLike.of(ClientStorage.mediaLog.patterns.buffer()[index])
		val patternSettings = WorldlyPatternRenderHelpers.WORLDLY_SETTINGS_WOBBLY
		val oldShader = RenderSystem.getShader()

		val color = ColorHelper.Argb.getArgb((alpha * 255).toInt(), 255, 255, 255)
		val staticPoints = HexPatternPoints.getStaticPoints(patternlike, patternSettings, 0.0)
		val nonzappyLines = patternlike.nonZappyPoints
		val zappyPattern = makeZappy(nonzappyLines, findDupIndices(nonzappyLines), patternSettings.hops, patternSettings.variance, patternSettings.speed, patternSettings.flowIrregular, patternSettings.readabilityOffset, patternSettings.lastSegmentProp, 0.0)
		drawLineSeq(matrices.peek().getPositionMatrix(), staticPoints.scaleVecs(zappyPattern), patternSettings.getInnerWidth(staticPoints.finalScale).toFloat(), color, color, VCDrawHelper.getHelper(null, matrices, 0.001f))

		RenderSystem.setShader { oldShader }
	}

	fun drawStackItem(index: Int, alpha: Float) {
		val iotas = ClientStorage.mediaLog.stack
	}
}