package miyucomics.hexical.utils

import at.petrak.hexcasting.xplat.IXplatAbstractions
import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.client.ClientStorage
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.*
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import kotlin.math.cos
import kotlin.math.sin

object HexagonRendering {
	fun renderHexagon(context: WorldRenderContext, pos: Vec3d) {
		val matrices = context.matrixStack()
		val camera = context.camera()
		val camPos = camera.pos

		matrices.push()
		matrices.translate(pos.x - camPos.x, pos.y - camPos.y, pos.z - camPos.z)

		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.yaw))
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.pitch))

		val tessellator = Tessellator.getInstance()
		val bufferBuilder = tessellator.buffer

		RenderSystem.disableDepthTest()
		RenderSystem.enableBlend()
		RenderSystem.defaultBlendFunc()
		RenderSystem.disableCull()
		RenderSystem.setShader(GameRenderer::getPositionColorProgram)

		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)

		val points = mutableListOf<Vec2f>()
		for (i in 0..6) {
			val angle = (i % 6) * (Math.PI / 3)
			points.add(Vec2f(cos(angle).toFloat(), sin(angle).toFloat()).multiply(0.25f))
		}

		val pigment = IXplatAbstractions.INSTANCE.getPigment(MinecraftClient.getInstance().player!!).colorProvider
		fun makeVertex(offset: Vec2f) = bufferBuilder.vertex(matrices.peek().positionMatrix, offset.x, offset.y, 0f)
			.color(pigment.getColor(ClientStorage.ticks.toFloat(), pos.add(offset.x.toDouble() * 2, offset.y.toDouble() * 2, 0.0)))
			.next()
		RenderUtils.quadifyLines(::makeVertex, 0.05f, points)

		tessellator.draw()

		RenderSystem.enableCull()
		RenderSystem.disableBlend()
		RenderSystem.enableDepthTest()

		matrices.pop()
	}
}