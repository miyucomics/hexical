package miyucomics.hexical.blocks

import miyucomics.hexical.utils.MediaJarRenderStuffs
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack

class MediaJarBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<MediaJarBlockEntity> {
	override fun render(jar: MediaJarBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
		MediaJarRenderStuffs.renderFluid(matrices, vertexConsumers, jar.getMedia().toFloat() / MediaJarBlock.MAX_CAPACITY.toFloat())
	}
}