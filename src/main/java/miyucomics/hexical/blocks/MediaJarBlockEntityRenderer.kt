package miyucomics.hexical.blocks

import miyucomics.hexical.HexicalClient
import miyucomics.hexical.utils.RenderUtils.addRectangularPrism
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

		val consumer = vertexConsumers.getBuffer(HexicalClient.MEDIA_JAR_RENDER_LAYER)
		val height = filled * 12f / 16f

		matrices.push()
		matrices.translate(0.5f, 1f / 16f, 0.5f)
		addRectangularPrism(consumer, matrices, minU = 0f, minV = 0f, maxU = 1f, maxV = 1f, width = 0.5f, height = height, light = light, color = 0xFFFFFFFF.toInt(), alpha = 1f)
		matrices.pop()
	}
}