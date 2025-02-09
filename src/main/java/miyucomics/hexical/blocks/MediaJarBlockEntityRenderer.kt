package miyucomics.hexical.blocks

import miyucomics.hexical.utils.RenderUtils.addRect
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack

class MediaJarBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<MediaJarBlockEntity> {
	override fun render(jarData: MediaJarBlockEntity?, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
		if (jarData == null)
			return
		val filled = jarData.getMedia().toFloat() / MediaJarBlock.MAX_CAPACITY.toFloat()

		val consumer = vertexConsumers.getBuffer(RenderLayer.getEndPortal())
		val color = 0xFFFFFFFF.toInt()
		val width = 0.5f
		val height = filled * 12f / 16f

		matrices.push()
		matrices.translate(0.5f, 1f / 16f, 0.5f)
		addRect(consumer, matrices, minU = 0f, minV = 0f, maxU = 1f, maxV = 1f, width = width, height = height, light = light, color = color, alpha = 1f)
		matrices.pop()
	}
}