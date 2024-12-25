package miyucomics.hexical.entities

import at.petrak.hexcasting.api.HexAPI.modLoc
import at.petrak.hexcasting.client.makeZappy
import at.petrak.hexcasting.client.render.makeZappy
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Matrix3f
import net.minecraft.util.math.Matrix4f
import net.minecraft.util.math.Vec3f

class LivingScrollRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<LivingScrollEntity>(ctx) {
	override fun render(scroll: LivingScrollEntity?, yaw: Float, deltaTick: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		matrices.push()
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(scroll!!.pitch))
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - scroll.yaw))
		val worldLight = WorldRenderer.getLightmapCoordinates(scroll.world, scroll.blockPos)
		if (!scroll.clientVanished)
			drawFrame(matrices, vertexConsumers, getTexture(scroll), scroll.clientSize.toFloat(), worldLight)
		drawPattern(matrices, vertexConsumers, scroll, worldLight)
		matrices.pop()
	}

	override fun getTexture(scroll: LivingScrollEntity?) = when (scroll!!.clientSize) {
		1 -> if (scroll.clientAged) ANCIENT_SMALL else PRISTINE_SMALL
		2 -> if (scroll.clientAged) ANCIENT_MEDIUM else PRISTINE_MEDIUM
		3 -> if (scroll.clientAged) ANCIENT_LARGE else PRISTINE_LARGE
		else -> ANCIENT_SMALL
	}

	companion object {
		private val PRISTINE_SMALL: Identifier = modLoc("textures/block/scroll_paper.png")
		private val PRISTINE_MEDIUM: Identifier = modLoc("textures/entity/scroll_medium.png")
		private val PRISTINE_LARGE: Identifier = modLoc("textures/entity/scroll_large.png")
		private val ANCIENT_SMALL: Identifier = modLoc("textures/block/ancient_scroll_paper.png")
		private val ANCIENT_MEDIUM: Identifier = modLoc("textures/entity/scroll_ancient_medium.png")
		private val ANCIENT_LARGE: Identifier = modLoc("textures/entity/scroll_ancient_large.png")
		private val WHITE: Identifier = modLoc("textures/entity/white.png")

		private fun vertex(mat: Matrix4f, normal: Matrix3f, light: Int, verts: VertexConsumer, x: Float, y: Float, z: Float, u: Float, v: Float, nx: Float, ny: Float, nz: Float) = verts.vertex(mat, x, y, z).color(-0x1).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, nx, ny, nz).next()

		private fun drawFrame(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, texture: Identifier, size: Float, light: Int) {
			matrices.push()
			matrices.translate((-size / 2f).toDouble(), (-size / 2f).toDouble(), (1f / 32f).toDouble())

			val dz = -1f / 16f
			val margin = 1f / 48f
			val last = matrices.peek()
			val mat = last.positionMatrix
			val norm = last.normalMatrix

			val verts = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture))
			vertex(mat, norm, light, verts, 0f, 0f, dz, 0f, 0f, 0f, 0f, -1f)
			vertex(mat, norm, light, verts, 0f, size, dz, 0f, 1f, 0f, 0f, -1f)
			vertex(mat, norm, light, verts, size, size, dz, 1f, 1f, 0f, 0f, -1f)
			vertex(mat, norm, light, verts, size, 0f, dz, 1f, 0f, 0f, 0f, -1f)

			vertex(mat, norm, light, verts, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f)
			vertex(mat, norm, light, verts, size, 0f, 0f, 1f, 0f, 0f, 0f, 1f)
			vertex(mat, norm, light, verts, size, size, 0f, 1f, 1f, 0f, 0f, 1f)
			vertex(mat, norm, light, verts, 0f, size, 0f, 0f, 1f, 0f, 0f, 1f)

			vertex(mat, norm, light, verts, 0f, 0f, 0f, 0f, 0f, 0f, -1f, 0f)
			vertex(mat, norm, light, verts, 0f, 0f, dz, 0f, margin, 0f, -1f, 0f)
			vertex(mat, norm, light, verts, size, 0f, dz, 1f, margin, 0f, -1f, 0f)
			vertex(mat, norm, light, verts, size, 0f, 0f, 1f, 0f, 0f, -1f, 0f)

			vertex(mat, norm, light, verts, 0f, 0f, 0f, 0f, 0f, -1f, 0f, 0f)
			vertex(mat, norm, light, verts, 0f, size, 0f, 0f, 1f, -1f, 0f, 0f)
			vertex(mat, norm, light, verts, 0f, size, dz, margin, 1f, -1f, 0f, 0f)
			vertex(mat, norm, light, verts, 0f, 0f, dz, margin, 0f, -1f, 0f, 0f)

			vertex(mat, norm, light, verts, size, 0f, dz, 1f - margin, 0f, 1f, 0f, 0f)
			vertex(mat, norm, light, verts, size, size, dz, 1f - margin, 1f, 1f, 0f, 0f)
			vertex(mat, norm, light, verts, size, size, 0f, 1f, 1f, 1f, 0f, 0f)
			vertex(mat, norm, light, verts, size, 0f, 0f, 1f, 0f, 1f, 0f, 0f)

			vertex(mat, norm, light, verts, 0f, size, dz, 0f, 1f - margin, 0f, 1f, 0f)
			vertex(mat, norm, light, verts, 0f, size, 0f, 0f, 1f, 0f, 1f, 0f)
			vertex(mat, norm, light, verts, size, size, 0f, 1f, 1f, 0f, 1f, 0f)
			vertex(mat, norm, light, verts, size, size, dz, 1f, 1f - margin, 0f, 1f, 0f)

			matrices.pop()
		}

		private fun drawPattern(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, scroll: LivingScrollEntity, light: Int) {
			if (!scroll.clientVanished)
				matrices.translate(0.0, 0.0, -0.75 / 16.0)
			val scale = when (scroll.clientSize) {
				1 -> 0.5f
				2 -> 1.25f
				3 -> 2f
				else -> 1f
			}
			matrices.scale(scale, scale, 1f)
			val peek = matrices.peek()
			val buffer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(WHITE))
			val zappy = makeZappy(scroll.cachedVerts, null, 10, 1f, 0.1f, 0f, 0.1f, 0.9f, 0.0)
			RenderUtils.drawLines(peek.positionMatrix, peek.normalMatrix, if (scroll.clientGlow) LightmapTextureManager.MAX_LIGHT_COORDINATE else light, 0.025f / scroll.clientSize, buffer, zappy) { _ -> scroll.clientColor }
		}
	}
}