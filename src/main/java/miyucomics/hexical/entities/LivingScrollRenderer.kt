package miyucomics.hexical.entities

import at.petrak.hexcasting.api.HexAPI.modLoc
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.client.CAP_THETA
import at.petrak.hexcasting.client.findDupIndices
import at.petrak.hexcasting.client.makeZappy
import at.petrak.hexcasting.client.rotate
import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.utils.RenderUtils.CIRCLE_RESOLUTION
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
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
		private fun vertexColored(mat: Matrix4f, normal: Matrix3f, light: Int, verts: VertexConsumer, pos: Vec2f) = verts.vertex(mat, -pos.x, pos.y, 0f).color((0xc8_322b33).toInt()).texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, 0f, 0f, 1f).next()

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
				1 -> 0.5f
				2 -> 1f
				3 -> 1.5f
				else -> 1f
			}
			matrices.scale(scale, scale, 1f)
			val lines = pattern.toLines(0.25f, pattern.getCenter(0.25f).negate()).toMutableList()
			val peek = matrices.peek()
			val mat = peek.positionMatrix
			val norm = peek.normalMatrix
			val verts = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(WHITE))
			val zappy = makeZappy(lines, findDupIndices(pattern.positions()), 10, 2.5f, 0.1f, 0.2f, 0f, 1f, hashCode().toDouble())
			drawLineSequence(mat, norm, light, verts, zappy)
			matrices.pop()
		}

		private fun drawLineSequence(mat: Matrix4f, normalMat: Matrix3f, light: Int, verts: VertexConsumer, points: List<Vec2f>) {
			val pointCount = points.size
			if (pointCount <= 1)
				return

			val joinAngles = FloatArray(points.size)
			for (i in 2 until points.size) {
				val currentPoint = points[i - 1]
				val offsetFromLast = currentPoint.add(points[i - 2].negate())
				val offsetToNext = points[i].add(currentPoint.negate())
				joinAngles[i - 1] = atan2(offsetFromLast.x * offsetToNext.y - offsetFromLast.y * offsetToNext.x, offsetFromLast.x * offsetToNext.x + offsetFromLast.y * offsetToNext.y)
			}

			for (i in 0 until pointCount - 1) {
				val currentPoint = points[i]
				val nextPoint = points[i + 1]

				val sideLength = nextPoint.add(currentPoint.negate()).normalize().multiply(0.025f)
				val normal = Vec2f(-sideLength.y, sideLength.x)

				val currentDown = currentPoint.add(normal)
				val currentUp = currentPoint.add(normal.negate())
				val nextDown = nextPoint.add(normal)
				val nextUp = nextPoint.add(normal.negate())

				vertexColored(mat, normalMat, light, verts, currentUp)
				vertexColored(mat, normalMat, light, verts, currentDown)
				vertexColored(mat, normalMat, light, verts, nextDown)
				vertexColored(mat, normalMat, light, verts, nextUp)

				if (i > 0) {
					val angle = joinAngles[i]
					val joinSteps = ceil(abs(angle) / (2 * MathHelper.PI) * CIRCLE_RESOLUTION).toInt()
					if (joinSteps < 1)
						continue

					if (angle < 0) {
						var previous = currentPoint.add(normal)
						for (j in 1..joinSteps) {
							val fan = rotate(normal.negate(), -angle * (j.toFloat() / joinSteps))
							val fanShift = currentPoint.add(fan.negate())

							vertexColored(mat, normalMat, light, verts, currentPoint)
							vertexColored(mat, normalMat, light, verts, currentPoint)
							vertexColored(mat, normalMat, light, verts, fanShift)
							vertexColored(mat, normalMat, light, verts, previous)
							previous = fanShift
						}
					} else {
						var previous = currentPoint.add(rotate(normal, -angle).negate())
						for (j in joinSteps - 1 downTo 0) {
							val fan = rotate(normal, -angle * (j.toFloat() / joinSteps))
							val fanShift = currentPoint.add(fan.negate())

							vertexColored(mat, normalMat, light, verts, currentPoint)
							vertexColored(mat, normalMat, light, verts, currentPoint)
							vertexColored(mat, normalMat, light, verts, fanShift)
							vertexColored(mat, normalMat, light, verts, previous)
							previous = fanShift
						}
					}
				}
			}

			for (pair in arrayOf(arrayOf(points[0], points[1]), arrayOf(points[pointCount - 1], points[pointCount - 2]))) {
				val point = pair[0]
				val prev = pair[1]

				val sideLength = point.add(prev.negate()).normalize().multiply(0.025f)
				val normal = Vec2f(-sideLength.y, sideLength.x)
				val joinSteps = 20 / 2
				for (j in joinSteps downTo 1) {
					val fan0 = rotate(normal, -PI.toFloat() * (j.toFloat() / joinSteps))
					val fan1 = rotate(normal, -PI.toFloat() * ((j - 1).toFloat() / joinSteps))

					vertexColored(mat, normalMat, light, verts, point)
					vertexColored(mat, normalMat, light, verts, point)
					vertexColored(mat, normalMat, light, verts, point.add(fan1))
					vertexColored(mat, normalMat, light, verts, point.add(fan0))
				}
			}
		}
	}
}