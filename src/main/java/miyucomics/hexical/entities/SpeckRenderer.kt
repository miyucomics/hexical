package miyucomics.hexical.entities

import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.*

class SpeckRenderer(ctx: EntityRendererFactory.Context?) : EntityRenderer<SpeckEntity?>(ctx) {
	override fun getTexture(entity: SpeckEntity?): Identifier? = null
	override fun shouldRender(entity: SpeckEntity?, frustum: Frustum?, x: Double, y: Double, z: Double) = true
	override fun render(entity: SpeckEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack?, vertexConsumers: VertexConsumerProvider?, light: Int) {
		val oldShader = RenderSystem.getShader()
		RenderSystem.setShader(GameRenderer::getPositionColorShader)
		RenderSystem.enableDepthTest()
		matrices!!.push()

		if (entity!!.yaw != 0.0f)
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-entity.yaw))
		if (entity.pitch != 0.0f)
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.pitch))
		if (!entity.getIsPattern())
			matrices.translate(0.0, 0.25, 0.0)
		if (entity.getRoll() != 0.0f)
			matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(entity.getRoll()))
		matrices.scale(entity.getSize(), entity.getSize(), entity.getSize())

		RenderSystem.disableCull()
		if (entity.getIsPattern()) {
			val pattern = entity.getPattern()
			val lines = pattern.toLines(0.25f, pattern.getCenter(0.25f).negate()).toMutableList()
			for (i in lines.indices)
				lines[i] = Vec2f(lines[i].x, -lines[i].y)
			RenderUtils.drawFigure(matrices.peek().positionMatrix, lines, entity.getThickness() * 0.05f, entity.getPigment())
		} else {
			matrices.scale(0.1f / 3f, -0.1f / 3f, -0.1f / 3f)
			val text = entity.getText()
			val height = (-textRenderer.getWidth(text) / 2).toFloat()
			textRenderer.draw(matrices, text, height, -textRenderer.fontHeight.toFloat() / 2f, entity.getPigment().getColor(0f, entity.pos))
		}
		RenderSystem.enableCull()

		matrices.pop()
		RenderSystem.setShader { oldShader }
	}
}