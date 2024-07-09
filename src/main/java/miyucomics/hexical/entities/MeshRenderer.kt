package miyucomics.hexical.entities

import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3f

class MeshRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<MeshEntity>(ctx) {
	override fun getTexture(entity: MeshEntity?): Identifier? = null
	override fun shouldRender(entity: MeshEntity?, frustum: Frustum?, x: Double, y: Double, z: Double) = true
	override fun render(entity: MeshEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		val oldShader = RenderSystem.getShader()
		RenderSystem.setShader(GameRenderer::getPositionColorShader)
		RenderSystem.enableDepthTest()
		matrices.push()

		val pos = MinecraftClient.getInstance().player!!.pos
		if (entity!!.yaw != 0.0f)
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-entity.yaw))
		if (entity.pitch != 0.0f)
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.pitch))
		if (entity.clientRoll != 0.0f)
			matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(entity.clientRoll))
		matrices.scale(entity!!.clientSize, entity.clientSize, entity.clientSize)
		matrices.translate(entity.x - pos.x, entity.y - pos.y, entity.z - pos.z)

		RenderSystem.disableCull()
		RenderUtils.sentinelLike(matrices, entity.clientVertices, 5f, entity.clientPigment)
		RenderSystem.enableCull()

		matrices.pop()
		RenderSystem.setShader { oldShader }
	}
}