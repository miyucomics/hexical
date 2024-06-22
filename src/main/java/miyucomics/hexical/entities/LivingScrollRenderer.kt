package miyucomics.hexical.entities

import at.petrak.hexcasting.api.HexAPI.modLoc
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.client.CAP_THETA
import at.petrak.hexcasting.client.findDupIndices
import at.petrak.hexcasting.client.makeZappy
import at.petrak.hexcasting.client.rotate
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.decoration.AbstractDecorationEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.*
import kotlin.math.*

class LivingScrollRenderer(ctx: EntityRendererFactory.Context?) : EntityRenderer<LivingScrollEntity?>(ctx) {
	override fun render(scroll: LivingScrollEntity?, yaw: Float, deltaTick: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		RenderSystem.setShader { GameRenderer.getPositionTexShader() }
		matrices.push()
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((scroll as Entity).pitch))
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - (scroll as Entity).yaw))
		val worldLight = WorldRenderer.getLightmapCoordinates(scroll.world, scroll.blockPos)
		drawFrame(matrices, vertexConsumers, getTexture(scroll), scroll.getSize().toFloat(), worldLight)
		drawPattern(matrices, vertexConsumers, scroll.getRender(), scroll.getSize(), worldLight)
		matrices.pop()
	}

	override fun getTexture(scroll: LivingScrollEntity?) = when (scroll!!.getSize()) {
		1 -> PRISTINE_BG_SMOL
		2 -> PRISTINE_BG_MEDIUM
		3 -> PRISTINE_BG_LARGE
		else -> PRISTINE_BG_SMOL
	}

	companion object {
		private val PRISTINE_BG_SMOL: Identifier = modLoc("textures/block/scroll_paper.png")
		private val PRISTINE_BG_MEDIUM: Identifier = modLoc("textures/entity/scroll_medium.png")
		private val PRISTINE_BG_LARGE: Identifier = modLoc("textures/entity/scroll_large.png")
		private val WHITE: Identifier = modLoc("textures/entity/white.png")

		private fun vertex(mat: Matrix4f, normal: Matrix3f, light: Int, verts: VertexConsumer, x: Float, y: Float, z: Float, u: Float, v: Float, nx: Float, ny: Float, nz: Float) = verts.vertex(mat, x, y, z).color(-0x1).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, nx, ny, nz).next()
		private fun vertexColored(mat: Matrix4f, normal: Matrix3f, light: Int, verts: VertexConsumer, col: Int, pos: Vec2f) = verts.vertex(mat, -pos.x, pos.y, 0f).color(col).texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, 0f, 0f, 1f).next()

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
		private fun drawPattern(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, pattern: HexPattern, size: Int, light: Int) {
			matrices.push()
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180f))
			matrices.translate(0.0, 0.0, 0.75 / 16.0)
			val scale = when (size) {
				1 -> 0.4f
				2 -> 1f
				3 -> 1.5f
				else -> 1f
			}
			matrices.scale(scale, scale, 1f)
			val lines = pattern.toLines(0.25f, pattern.getCenter(0.25f).negate()).toMutableList()
			val verts2 = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(WHITE))
			val zappy = makeZappy(lines, findDupIndices(pattern.positions()), 10, 2.5f, 0.1f, 0.2f, 0f, 1f, hashCode().toDouble())
			val peek = matrices.peek()
			val mat2 = peek.positionMatrix
			val norm2 = peek.normalMatrix
			drawLineSequence(mat2, norm2, light, verts2, zappy, 0.05f, (0xc8_322b33).toInt())
			matrices.pop()
		}

		private fun drawLineSequence(mat: Matrix4f, normalMat: Matrix3f, light: Int, verts: VertexConsumer, points: List<Vec2f>, width: Float, color: Int) {
			if (points.size <= 1)
				return

			val joinAngles = FloatArray(points.size)
			val joinOffsets = FloatArray(points.size)
			for (i in 2 until points.size) {
				val p0 = points[i - 2]
				val p1 = points[i - 1]
				val p2 = points[i]
				val prev = p1.add(p0.negate())
				val next = p2.add(p1.negate())
				val angle = atan2(
					prev.x * next.y - prev.y * next.x,
					prev.x * next.x + prev.y * next.y
				)
				joinAngles[i - 1] = angle
				val clamp = min(prev.length(), next.length()) / (width * 0.5f)
				joinOffsets[i - 1] = MathHelper.clamp(sin(angle) / (1 + cos(angle)), -clamp, clamp)
			}

			for (i in 0 until points.size - 1) {
				val p1 = points[i]
				val p2 = points[i + 1]

				val tangent = p2.add(p1.negate()).normalize().multiply(width * 0.5f)
				val normal = Vec2f(-tangent.y, tangent.x)

				val jlow = joinOffsets[i]
				val jhigh = joinOffsets[i + 1]

				val p1Down = p1.add(tangent.multiply(max(0f, jlow))).add(normal)
				val p1Up = p1.add(tangent.multiply(max(0f, -jlow))).add(normal.negate())
				val p2Down = p2.add(tangent.multiply(max(0f, jhigh)).negate()).add(normal)
				val p2Up = p2.add(tangent.multiply(max(0f, -jhigh)).negate()).add(normal.negate())

				vertexColored(mat, normalMat, light, verts, color, p1)
				vertexColored(mat, normalMat, light, verts, color, p2)
				vertexColored(mat, normalMat, light, verts, color, p2Up)
				vertexColored(mat, normalMat, light, verts, color, p1Up)

				vertexColored(mat, normalMat, light, verts, color, p1)
				vertexColored(mat, normalMat, light, verts, color, p1Down)
				vertexColored(mat, normalMat, light, verts, color, p2Down)
				vertexColored(mat, normalMat, light, verts, color, p2)

				if (i > 0) {
					val sangle = joinAngles[i]
					val angle = abs(sangle.toDouble()).toFloat()
					val rnormal = normal.negate()
					val joinSteps = ceil(angle * 180 / (CAP_THETA * PI)).toInt()
					if (joinSteps < 1) continue

					if (sangle < 0) {
						var prevVert = Vec2f(p1.x - rnormal.x, p1.y - rnormal.y)
						for (j in 1..joinSteps) {
							val fan = rotate(rnormal, -sangle * (j.toFloat() / joinSteps))
							val fanShift = Vec2f(p1.x - fan.x, p1.y - fan.y)

							vertexColored(mat, normalMat, light, verts, color, p1)
							vertexColored(mat, normalMat, light, verts, color, p1)
							vertexColored(mat, normalMat, light, verts, color, fanShift)
							vertexColored(mat, normalMat, light, verts, color, prevVert)
							prevVert = fanShift
						}
					} else {
						val startFan = rotate(normal, -sangle)
						var prevVert = Vec2f(p1.x - startFan.x, p1.y - startFan.y)
						for (j in joinSteps - 1 downTo 0) {
							val fan = rotate(normal, -sangle * (j.toFloat() / joinSteps))
							val fanShift = Vec2f(p1.x - fan.x, p1.y - fan.y)

							vertexColored(mat, normalMat, light, verts, color, p1)
							vertexColored(mat, normalMat, light, verts, color, p1)
							vertexColored(mat, normalMat, light, verts, color, fanShift)
							vertexColored(mat, normalMat, light, verts, color, prevVert)
							prevVert = fanShift
						}
					}
				}
			}

			for (pair in arrayOf(arrayOf(points[0], points[1]), arrayOf(points[points.size - 1], points[points.size - 2]))) {
				val point: Vec2f = pair[0]
				val prev: Vec2f = pair[1]

				val tangent = point.add(prev.negate()).normalize().multiply(0.5f * width)
				val normal = Vec2f(-tangent.y, tangent.x)
				val joinSteps = ceil(180f / CAP_THETA).toInt()
				for (j in joinSteps downTo 1) {
					val fan0 = rotate(normal, -PI.toFloat() * (j.toFloat() / joinSteps))
					val fan1 = rotate(normal, -PI.toFloat() * ((j - 1).toFloat() / joinSteps))

					vertexColored(mat, normalMat, light, verts, color, point)
					vertexColored(mat, normalMat, light, verts, color, point)
					vertexColored(mat, normalMat, light, verts, color, point.add(fan1))
					vertexColored(mat, normalMat, light, verts, color, point.add(fan0))
				}
			}
		}
	}
}