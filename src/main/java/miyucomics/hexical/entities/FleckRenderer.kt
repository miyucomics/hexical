package miyucomics.hexical.entities

import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.*

class FleckRenderer(ctx: EntityRendererFactory.Context?) : EntityRenderer<FleckEntity?>(ctx) {
	override fun getTexture(entity: FleckEntity?): Identifier? = null
	override fun shouldRender(entity: FleckEntity?, frustum: Frustum?, x: Double, y: Double, z: Double) = true
	override fun render(entity: FleckEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack?, vertexConsumers: VertexConsumerProvider?, light: Int) {
		val oldShader = RenderSystem.getShader()
		RenderSystem.setShader(GameRenderer::getPositionColorShader)
		RenderSystem.enableDepthTest()
		matrices!!.push()

		if (entity!!.yaw != 0.0f)
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-entity.yaw))
		if (entity.pitch != 0.0f)
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.pitch))
		if (entity.getRoll() != 0.0f)
			matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(entity.getRoll()))
		matrices.scale(entity.getSize(), entity.getSize(), entity.getSize())

		RenderSystem.disableCull()
		RenderUtils.drawFigure(matrices.peek().positionMatrix, entity.getShape(), entity.getThickness() * 0.05f, entity.getPigment())
		RenderSystem.enableCull()

		matrices.pop()
		RenderSystem.setShader { oldShader }
	}
}