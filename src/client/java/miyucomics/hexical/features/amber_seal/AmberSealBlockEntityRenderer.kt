package miyucomics.hexical.features.amber_seal

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack

class AmberSealBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<AmberSealBlockEntity> {
	private val renderManager: BlockRenderManager = ctx.renderManager
	private val renderDispatcher: BlockEntityRenderDispatcher = ctx.renderDispatcher

	override fun render(seal: AmberSealBlockEntity, tickDelta: Float, matrices: MatrixStack, consumers: VertexConsumerProvider, light: Int, overlay: Int) {
		if (seal.encasedState == null)
			return

		matrices.push()
		matrices.translate(0.0625, 0.0625, 0.0625)
		matrices.scale(0.875f, 0.875f, 0.875f)
		renderManager.renderBlockAsEntity(seal.encasedState, matrices, consumers, light, overlay)
		renderDispatcher.render(seal.encasedEntity, tickDelta, matrices, consumers)
		matrices.pop()
	}
}