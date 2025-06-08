package miyucomics.hexical.items

import miyucomics.hexical.blocks.MediaJarBlock
import miyucomics.hexical.registry.HexicalBlocks
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.RotationAxis
import org.joml.Quaternionf
import org.joml.Vector3f

class MediaJarItemRenderer : BuiltinItemRendererRegistry.DynamicItemRenderer {
	override fun render(stack: ItemStack, mode: ModelTransformationMode, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
		MinecraftClient.getInstance().blockRenderManager.renderBlockAsEntity(HexicalBlocks.MEDIA_JAR_BLOCK.defaultState, matrices, vertexConsumers, light, overlay)

		val tag = stack.nbt?.getCompound("BlockEntityTag")
		val media = tag?.getLong("media") ?: 0
		val filled = media.toFloat() / MediaJarBlock.MAX_CAPACITY.toFloat()

		val consumer = vertexConsumers.getBuffer(RenderLayer.getSolid())
		matrices.push()
		matrices.translate(0.5f, 1f / 16f, 0.5f)
		addRectangularPrism(consumer, matrices, height = filled * 12f / 16f)
		matrices.pop()
	}

	companion object {
		private val NEGATIVE_X_ROTATION: Quaternionf = RotationAxis.POSITIVE_X.rotationDegrees(-90f)
		private val DIR2ROT: Map<Direction, Quaternionf> = enumValues<Direction>().associateWith { it.opposite.rotationQuaternion.mul(NEGATIVE_X_ROTATION) }

		private fun addRectangularPrism(consumer: VertexConsumer, matrices: MatrixStack, height: Float) {
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
				addQuad(consumer, matrices, -halfWidth, y0, halfWidth, y1, direction.unitVector)
				matrices.pop()
			}

			matrices.pop()
		}

		private fun addQuad(consumer: VertexConsumer, matrices: MatrixStack, x0: Float, y0: Float, x1: Float, y1: Float, direction: Vector3f) {
			consumer.vertex(matrices.peek().positionMatrix, x0, y1, 0f).color(255, 255, 255, 255).texture(0f, 1f).light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE).normal(direction.x, direction.y, direction.z).next()
			consumer.vertex(matrices.peek().positionMatrix, x1, y1, 0f).color(255, 255, 255, 255).texture(1f, 1f).light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE).normal(direction.x, direction.y, direction.z).next()
			consumer.vertex(matrices.peek().positionMatrix, x1, y0, 0f).color(255, 255, 255, 255).texture(1f, 0f).light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE).normal(direction.x, direction.y, direction.z).next()
			consumer.vertex(matrices.peek().positionMatrix, x0, y0, 0f).color(255, 255, 255, 255).texture(0f, 0f).light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE).normal(direction.x, direction.y, direction.z).next()
		}
	}
}