package miyucomics.hexical.entities

import at.petrak.hexcasting.client.drawLineSeq
import at.petrak.hexcasting.client.findDupIndices
import at.petrak.hexcasting.client.makeZappy
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Quaternion
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import kotlin.math.asin
import kotlin.math.atan2


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
		val outer = entity.getPigment().getColor(entity.age.toFloat(), entity.pos)

		val rotation = Vec3f.NEGATIVE_Y.getRadialQuaternion(entity.yaw)
		rotation.hamiltonProduct(Vec3f.POSITIVE_X.getRadialQuaternion((entity.pitch - Math.PI / 2).toFloat()))
		matrices.multiply(rotation)
		drawLineSeq(matrices.peek().positionMatrix, zappy, 0.05f, 0.01f, outer, outer)
		matrices.pop()
		RenderSystem.setShader { oldShader }
	}

	fun getQuaternion(direction: Vec3d, forward: Boolean): Quaternion {
		var copiedVector = direction
		copiedVector = copiedVector.normalize()
		val pitch = asin(-copiedVector.y).toFloat()
		val yaw = (-atan2(copiedVector.x, copiedVector.z)).toFloat()
		val rotation = Vec3f.NEGATIVE_Y.getRadialQuaternion(yaw)
		if (forward) {
			rotation.hamiltonProduct(Vec3f.POSITIVE_X.getRadialQuaternion((pitch + Math.PI / 2).toFloat()))
		} else {
			rotation.hamiltonProduct(Vec3f.POSITIVE_X.getRadialQuaternion((pitch - Math.PI / 2).toFloat()))
		}
		return rotation
	}

	override fun getTexture(entity: SpeckEntity?): Identifier? { return null }
}