package miyucomics.hexical.features.specklikes

import miyucomics.hexical.features.specklikes.speck.SpeckEntity
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.RotationAxis

class SpeckRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<SpeckEntity>(ctx) {
	override fun getTexture(entity: SpeckEntity): Identifier? = null
	override fun shouldRender(entity: SpeckEntity, frustum: Frustum, x: Double, y: Double, z: Double) = true
	override fun render(entity: SpeckEntity, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		matrices.push()

		matrices.translate(0.0, 0.375, 0.0)
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entity.yaw))
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.pitch))
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entity.clientRoll))
		matrices.scale(entity.clientSize * 0.02f, -entity.clientSize * 0.02f, entity.clientSize * 0.02f)

		val text = entity.dataTracker.get(SpeckEntity.textDataTracker)
		textRenderer.draw(text, -textRenderer.getWidth(text) / 2f, 0f, 0xff_ffffff.toInt(), true, matrices.peek().positionMatrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE)

		matrices.pop()
	}
}