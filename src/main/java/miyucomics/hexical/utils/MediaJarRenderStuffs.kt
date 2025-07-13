package miyucomics.hexical.utils

import miyucomics.hexical.inits.HexicalRenderLayers
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.RotationAxis
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.abs

object MediaJarRenderStuffs {
	fun renderFluid(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, filled: Float, offset: Vector3f = Vector3f(0f)) {
		if (filled == 0f)
			return
		val consumer = vertexConsumers.getBuffer(HexicalRenderLayers.mediaJarRenderLayer)
		matrices.push()
		matrices.translate(0.5f, 1f / 16f, 0.5f)
		addRectangularPrism(consumer, matrices, height = filled * 12f / 16f, offset)
		matrices.pop()
	}

	private val NEGATIVE_X_ROTATION: Quaternionf = RotationAxis.POSITIVE_X.rotationDegrees(-90f)
	private val DIR2ROT: Map<Direction, Quaternionf> = enumValues<Direction>().associateWith { it.opposite.rotationQuaternion.mul(NEGATIVE_X_ROTATION) }

	private fun addRectangularPrism(consumer: VertexConsumer, matrices: MatrixStack, height: Float, offset: Vector3f) {
		val halfWidth = 0.5f / 2f
		val halfHeight = height / 2f

		matrices.push()
		matrices.translate(0f, halfHeight, 0f)

		for (direction in Direction.values()) {
			var depth = halfWidth
			var y0 = -halfHeight
			var y1 = halfHeight

			if (direction.axis == Direction.Axis.Y) {
				depth = halfHeight
				y0 = -halfWidth
				y1 = halfWidth
			}

			matrices.push()
			matrices.multiply(DIR2ROT[direction])
			matrices.translate(0f, 0f, -depth)
			addQuad(consumer, matrices, -halfWidth, y0, halfWidth, y1, offset)
			matrices.pop()
		}

		matrices.pop()
	}

	private fun addQuad(consumer: VertexConsumer, matrices: MatrixStack, x0: Float, y0: Float, x1: Float, y1: Float, offset: Vector3f) {
		val quadWidth = (abs(x0 - x1) * 255).toInt()
		val quadHeight = (abs(y0 - y1) * 255).toInt()
		consumer.vertex(matrices.peek().positionMatrix, x0, y1, 0f).texture(0f, 1f).color(quadWidth, quadHeight, 0, 0).normal(offset.x, offset.y, offset.z).next()
		consumer.vertex(matrices.peek().positionMatrix, x1, y1, 0f).texture(1f, 1f).color(quadWidth, quadHeight, 0, 0).normal(offset.x, offset.y, offset.z).next()
		consumer.vertex(matrices.peek().positionMatrix, x1, y0, 0f).texture(1f, 0f).color(quadWidth, quadHeight, 0, 0).normal(offset.x, offset.y, offset.z).next()
		consumer.vertex(matrices.peek().positionMatrix, x0, y0, 0f).texture(0f, 0f).color(quadWidth, quadHeight, 0, 0).normal(offset.x, offset.y, offset.z).next()
	}
}