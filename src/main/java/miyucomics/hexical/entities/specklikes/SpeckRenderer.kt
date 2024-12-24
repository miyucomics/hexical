package miyucomics.hexical.entities.specklikes

import at.petrak.hexcasting.api.HexAPI.modLoc
import com.mojang.blaze3d.systems.RenderSystem
import dev.kosmx.playerAnim.core.util.Vec3f
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

class SpeckRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<SpeckEntity>(ctx) {
	override fun getTexture(entity: SpeckEntity?): Identifier? = null
	override fun shouldRender(entity: SpeckEntity?, frustum: Frustum?, x: Double, y: Double, z: Double) = true
	override fun render(entity: SpeckEntity?, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		matrices.push()
		if (!entity!!.clientIsText)
			matrices.translate(0.0, 0.25, 0.0)
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-entity.yaw))
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.pitch))
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(entity.clientRoll))
		matrices.scale(entity.clientSize, entity.clientSize, entity.clientSize)

		if (entity.clientIsText) {
			RenderSystem.disableCull()
			val height = (-textRenderer.getWidth(entity.clientText) / 2).toFloat()
			matrices.scale(0.025f, -0.025f, 0.025f)
			textRenderer.draw(entity.clientText, height, -textRenderer.fontHeight.toFloat() / 2f, entity.clientPigment.colorProvider.getColor(0f, entity.pos))
			RenderSystem.enableCull()
		} else {
			val top = matrices.peek()
			val buffer = vertexConsumers.getBuffer(renderLayer)
			RenderUtils.drawLines(top.positionMatrix, top.normalMatrix, LightmapTextureManager.MAX_LIGHT_COORDINATE, entity.clientThickness * 0.05f / entity.clientSize, buffer, entity.clientVerts) { pos -> entity.clientPigment.colorProvider.getColor(0f, Vec3d(pos.x.toDouble(), pos.y.toDouble(), 0.0).multiply(2.0).add(entity.pos)) }
		}

		matrices.pop()
	}

	companion object {
		private val renderLayer = RenderLayer.getEntityCutoutNoCull(modLoc("textures/entity/white.png"))
	}
}