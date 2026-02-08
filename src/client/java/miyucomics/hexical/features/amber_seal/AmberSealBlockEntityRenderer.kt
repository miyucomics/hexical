package miyucomics.hexical.features.amber_seal

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
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
		renderAmberSealInnerContents(renderManager, renderDispatcher, seal.encasedState, seal.encasedEntity, tickDelta, matrices, consumers, light, overlay)
	}

	companion object {
		fun renderAmberSealInnerContents(manager: BlockRenderManager, dispatcher: BlockEntityRenderDispatcher, state: BlockState, blockEntity: BlockEntity?, tickDelta: Float, matrices: MatrixStack, consumers: VertexConsumerProvider, light: Int, overlay: Int) {
			matrices.push()
			matrices.translate(.0625, .0625, .0625)
			matrices.scale(0.875f, 0.875f, 0.875f)
			manager.renderBlockAsEntity(state, matrices, consumers, light, overlay)
			if (blockEntity != null)
				dispatcher.render(blockEntity, tickDelta, matrices, consumers)
			matrices.pop()
		}
	}
}