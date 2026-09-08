package miyucomics.hexical.feature.zap

import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteBillboardParticle
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import org.joml.Quaternionf
import org.joml.Vector3d
import org.joml.Vector3f
import kotlin.math.abs

class ZapParticle(world: ClientWorld, x: Double, y: Double, z: Double, val offset: Vector3f, color: Vector3f, val spriteProvider: SpriteProvider) : SpriteBillboardParticle(world, x, y, z) {
	private val roll: Float = this.random.nextFloat() * (Math.PI.toFloat() * 2f)

	init {
		this.maxAge = 10
		this.velocityX = 0.0
		this.velocityY = 0.0
		this.velocityZ = 0.0
		this.setColor(color.x, color.y, color.z)
		this.setSprite(spriteProvider)
		this.setSpriteForAge(spriteProvider)
		this.collidesWithWorld = false
	}

	override fun tick() {
		super.tick()
		this.setSpriteForAge(spriteProvider)
	}

	override fun buildGeometry(consumer: VertexConsumer, camera: Camera, deltaTick: Float) {
		val cameraPos = camera.pos
		val renderX = MathHelper.lerp(deltaTick.toDouble(), this.prevPosX, this.x) - cameraPos.x
		val renderY = MathHelper.lerp(deltaTick.toDouble(), this.prevPosY, this.y) - cameraPos.y
		val renderZ = MathHelper.lerp(deltaTick.toDouble(), this.prevPosZ, this.z) - cameraPos.z
		val start = Vector3d(renderX, renderY, renderZ)
		val end = Vector3d(renderX + offset.x, renderY + offset.y, renderZ + offset.z)

		if (offset.lengthSquared() < 0.01)
			return

		val widthVec = Vector3f()
		if (abs(offset.x) < 0.9f) {
			widthVec.set(1f, 0f, 0f).cross(offset)
		} else {
			widthVec.set(0f, 1f, 0f).cross(offset)
		}
		widthVec.normalize()

		val roll = Quaternionf().rotationAxis(roll, offset.x, offset.y, offset.z)
		widthVec.rotate(roll)

		val a = Vector3f(start.x.toFloat() - widthVec.x, start.y.toFloat() - widthVec.y, start.z.toFloat() - widthVec.z)
		val b = Vector3f(start.x.toFloat() + widthVec.x, start.y.toFloat() + widthVec.y, start.z.toFloat() + widthVec.z)
		val c = Vector3f(end.x.toFloat() + widthVec.x, end.y.toFloat() + widthVec.y, end.z.toFloat() + widthVec.z)
		val d = Vector3f(end.x.toFloat() - widthVec.x, end.y.toFloat() - widthVec.y, end.z.toFloat() - widthVec.z)

		val light = this.getBrightness(deltaTick)
		fun vertex(vertex: Vector3f, u: Float, v: Float) =
			consumer.vertex(vertex.x().toDouble(), vertex.y().toDouble(), vertex.z().toDouble()).texture(u, v).color(this.red, this.green, this.blue, this.alpha).light(light).next()

		vertex(a, this.minU, this.maxV)
		vertex(b, this.minU, this.minV)
		vertex(c, this.maxU, this.minV)
		vertex(d, this.maxU, this.maxV)

		vertex(d, this.maxU, this.maxV)
		vertex(c, this.maxU, this.minV)
		vertex(b, this.minU, this.minV)
		vertex(a, this.minU, this.maxV)
	}

	public override fun getBrightness(tint: Float): Int {
		val i = super.getBrightness(tint)
		val k = i shr 16 and 0xFF
		return 240 or (k shl 16)
	}

	override fun getType(): ParticleTextureSheet = ZapShaderHook.ZAP_SHEET

	data class Factory(val spriteProvider: SpriteProvider) : ParticleFactory<ZapParticleEffect> {
		override fun createParticle(effect: ZapParticleEffect, world: ClientWorld, x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double) = ZapParticle(world, x, y, z, effect.offset, effect.color, this.spriteProvider)
	}
}