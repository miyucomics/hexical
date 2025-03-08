package miyucomics.hexical.blocks

import miyucomics.hexical.registry.HexicalItems
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.RotationAxis

class PillowBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<PillowBlockEntity> {
	private val swordStack: ItemStack = ItemStack(HexicalItems.HAND_LAMP_ITEM)

	override fun render(entity: PillowBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
		matrices.push()
		matrices.translate(0.5, 1.0, 0.5)
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.currentYaw))
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.currentPitch))
		matrices.scale(1.5f, 1.5f, 1.5f)

		val lightAbove = WorldRenderer.getLightmapCoordinates(entity.world, entity.pos.up())
		MinecraftClient.getInstance().itemRenderer.renderItem(swordStack, ModelTransformationMode.GROUND, lightAbove, overlay, matrices, vertexConsumers, entity.world, 0)
		matrices.pop()
	}
}