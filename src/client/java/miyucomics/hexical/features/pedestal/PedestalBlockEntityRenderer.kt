package miyucomics.hexical.features.pedestal

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec3d
import kotlin.math.sin

class PedestalBlockEntityRenderer : BlockEntityRenderer<PedestalBlockEntity> {
	override fun render(pedestal: PedestalBlockEntity, tickDelta: Float, matrices: MatrixStack, vertices: VertexConsumerProvider, light: Int, overlay: Int) {
		if (pedestal.heldStack.isEmpty)
			return

		val time = pedestal.world!!.time + tickDelta
		val offset = Vec3d.of(pedestal.normalVector).multiply(0.75)
		matrices.translate(offset.x + 0.5, offset.y + 0.35, offset.z + 0.5)

		val bobbing = Vec3d.of(pedestal.normalVector).multiply(sin(time / 10).toDouble() / 10)
		matrices.translate(bobbing.x, bobbing.y, bobbing.z)

		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 4))

		val lightAbove = WorldRenderer.getLightmapCoordinates(pedestal.world, pedestal.pos.up())
		MinecraftClient.getInstance().itemRenderer.renderItem(pedestal.heldStack, ModelTransformationMode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertices, pedestal.world, 0)
	}
}