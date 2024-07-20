package miyucomics.hexical.entities

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3f

class MeshRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<MeshEntity>(ctx) {
	override fun getTexture(entity: MeshEntity?): Identifier? = null
	override fun shouldRender(entity: MeshEntity?, frustum: Frustum?, x: Double, y: Double, z: Double) = true
	override fun render(entity: MeshEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		val vertices = entity!!.clientVertices
		if (vertices.size < 2)
			return
		RenderSystem.enableBlend()
		RenderSystem.lineWidth(10f)
		RenderSystem.disableCull()
		RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA)
		RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader)
		val tessellator = Tessellator.getInstance()
		val buf = tessellator.buffer
		buf.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES)
		for (i in 1..<vertices.size)
			renderLine(matrices, buf, vertices[i - 1], vertices[i], 1f, 1f, 1f, 1f)
		tessellator.draw()
		RenderSystem.enableCull()
	}

	private fun renderLine(matrices: MatrixStack, buffer: BufferBuilder, start: Vec3f, end: Vec3f, r: Float, g: Float, b: Float, a: Float) {
		val position = matrices.peek().positionMatrix
		val norm = matrices.peek().normalMatrix
		val normal = end.copy()
		normal.subtract(start)
		normal.normalize()
		buffer.vertex(position, start.x, start.y, start.z).color(r, g, b, a).normal(norm, normal.x, normal.y, normal.z).next()
		buffer.vertex(position, end.x, end.y, end.z).color(r, g, b, a).normal(norm, -normal.x, -normal.y, -normal.z).next()
	}
}