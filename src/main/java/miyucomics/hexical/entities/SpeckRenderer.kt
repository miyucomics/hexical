package miyucomics.hexical.entities

import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3f

class SpeckRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<SpeckEntity>(ctx) {
	override fun getTexture(entity: SpeckEntity?): Identifier? = null
	override fun shouldRender(entity: SpeckEntity?, frustum: Frustum?, x: Double, y: Double, z: Double) = true
	override fun render(entity: SpeckEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		val oldShader = RenderSystem.getShader()
		RenderSystem.setShader(GameRenderer::getPositionColorShader)
		RenderSystem.enableDepthTest()
		matrices.push()

		if (!entity!!.clientIsText)
			matrices.translate(0.0, 0.25, 0.0)
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-entity.yaw))
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.pitch))
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(entity.clientRoll))
		matrices.scale(entity.clientSize, entity.clientSize, entity.clientSize)

		RenderSystem.disableCull()
		if (entity.clientIsText) {
			val height = (-textRenderer.getWidth(entity.clientText) / 2).toFloat()
			matrices.scale(0.025f, -0.025f, 0.025f)
			textRenderer.draw(matrices, entity.clientText, height, -textRenderer.fontHeight.toFloat() / 2f, entity.clientPigment.getColor(0f, entity.pos))
		} else {
			RenderUtils.drawFigure(matrices.peek().positionMatrix, entity.clientVerts, entity.clientThickness * 0.05f / entity.clientSize, entity.clientPigment, entity.pos)
		}
		RenderSystem.enableCull()

		matrices.pop()
		RenderSystem.setShader { oldShader }
	}
}