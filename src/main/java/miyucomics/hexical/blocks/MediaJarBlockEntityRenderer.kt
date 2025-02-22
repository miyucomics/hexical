package miyucomics.hexical.blocks

import miyucomics.hexical.HexicalClient
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.RotationAxis
import org.joml.Quaternionf

class MediaJarBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<MediaJarBlockEntity> {
	override fun render(jarData: MediaJarBlockEntity?, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
		if (jarData == null)
			return
		val filled = jarData.getMedia().toFloat() / MediaJarBlock.MAX_CAPACITY.toFloat()

		val consumer = vertexConsumers.getBuffer(HexicalClient.MEDIA_JAR_RENDER_LAYER)
		val height = filled * 12f / 16f

		matrices.push()
		matrices.translate(0.5f, 1f / 16f, 0.5f)
		addRectangularPrism(consumer, matrices, width = 0.5f, height = height)
		matrices.pop()
	}

	companion object {
		private val NEGATIVE_X_ROTATION: Quaternionf = RotationAxis.POSITIVE_X.rotationDegrees(-90f)
		private val DIR2ROT: Map<Direction, Quaternionf> = enumValues<Direction>().associateWith { it.opposite.rotationQuaternion.mul(NEGATIVE_X_ROTATION) }

		private fun addRectangularPrism(consumer: VertexConsumer, matrices: MatrixStack, width: Float, height: Float) {
			val halfWidth = width / 2f
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
				addQuad(consumer, matrices, -halfWidth, y0, halfWidth, y1)
				matrices.pop()
			}

			matrices.pop()
		}

		private fun addQuad(consumer: VertexConsumer, matrices: MatrixStack, x0: Float, y0: Float, x1: Float, y1: Float) {
			consumer.vertex(matrices.peek().positionMatrix, x0, y1, 0f).next()
			consumer.vertex(matrices.peek().positionMatrix, x1, y1, 0f).next()
			consumer.vertex(matrices.peek().positionMatrix, x1, y0, 0f).next()
			consumer.vertex(matrices.peek().positionMatrix, x0, y0, 0f).next()
		}
	}
}