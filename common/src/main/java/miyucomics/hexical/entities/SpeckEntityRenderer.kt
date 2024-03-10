package miyucomics.hexical.entities

import at.petrak.hexcasting.client.*
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class SpeckEntityRenderer(ctx: EntityRendererFactory.Context?) : EntityRenderer<SpeckEntity?>(ctx) {
	override fun render(entity: SpeckEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack?, vertexConsumers: VertexConsumerProvider?, light: Int) {
		val oldShader = RenderSystem.getShader()
		RenderSystem.setShader(GameRenderer::getPositionColorShader)
		RenderSystem.enableDepthTest()
		matrices!!.push()
		matrices.scale(-1f, -1f, 1f)

		val mat = matrices.peek()
		val pattern = entity!!.getPattern()
		val lines = pattern.toLines(1f, pattern.getCenter(1f).negate())
		val zappy = makeZappy(lines, findDupIndices(pattern.positions()), 10, 1.5f, 0.1f, 0.2f, 0f, 1f, hashCode().toDouble())
		val outer = -0x9b3701
		drawLineSeq(mat.positionMatrix, zappy, 0.1f, 0.01f, outer, outer)

		matrices.pop()
		RenderSystem.setShader { oldShader }
	}

	override fun getTexture(entity: SpeckEntity?): Identifier? { return null }
}