package miyucomics.hexical.blocks

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.registry.Registry

@Environment(EnvType.CLIENT)
class MageBlockRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<MageBlockEntity> {
	override fun render(blockEntity: MageBlockEntity?, tickDelta: Float, matrices: MatrixStack?, vertexConsumers: VertexConsumerProvider?, light: Int, overlay: Int) {
		if (blockEntity!!.properties["chameleon"] == true)
			MinecraftClient.getInstance().blockRenderManager.renderBlockAsEntity(Registry.BLOCK.get(blockEntity.appearance).defaultState, matrices, vertexConsumers, light, overlay)
	}
}