package miyucomics.hexical.entities

import at.petrak.hexcasting.client.*
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec2f

class SpeckEntityRenderer(ctx: EntityRendererFactory.Context?) : EntityRenderer<SpeckEntity?>(ctx) {
	override fun render(entity: SpeckEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack?, vertexConsumers: VertexConsumerProvider?, light: Int) {
		val oldShader = RenderSystem.getShader()
		RenderSystem.setShader(GameRenderer::getPositionColorShader)
		RenderSystem.enableDepthTest()
		matrices!!.push()
		val size = 0.25f
		val pattern = entity!!.getPattern()
		val lines = pattern.toLines(size, pattern.getCenter(size).negate()).toMutableList()
		for (i in lines.indices)
			lines[i] = Vec2f(lines[i].x, -lines[i].y)
		val zappy = makeZappy(lines, findDupIndices(pattern.positions()), 10, 1.5f, 0.1f, 0.2f, 0f, 1f, hashCode().toDouble())
		val outer = -0x9b3701

		val mat = matrices.peek()
		drawLineSeq(mat.positionMatrix, zappy, 0.05f, 0.01f, outer, outer)
		matrices.pop()
		RenderSystem.setShader { oldShader }
	}

	override fun getTexture(entity: SpeckEntity?): Identifier? { return null }
}