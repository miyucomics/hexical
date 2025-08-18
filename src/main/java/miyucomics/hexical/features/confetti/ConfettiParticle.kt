package miyucomics.hexical.features.confetti

import miyucomics.hexical.HexicalMain
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler
import net.minecraft.util.math.random.CheckedRandom
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.max

class ConfettiParticle(world: ClientWorld, x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double, sprite: SpriteProvider) : SpriteBillboardParticle(world, x, y, z) {
	private val particleId: Double
	private var pitch = 0f
	private var yaw = 0f
	private var prevPitch = 0f
	private var prevYaw = 0f
	private var deltaPitch = 0f
	private var deltaYaw = 0f
	private var deltaRoll = 0f

	init {
		this.setSprite(sprite)
		this.particleId = random.nextDouble()
		this.velocityX = dx
		this.velocityY = dy
		this.velocityZ = dz

		this.setBoundingBoxSpacing(0.001f, 0.001f)
		this.gravityStrength = 0.2f
		this.velocityMultiplier = 0.9f
		this.maxAge = random.nextInt() * 400 + 300
		this.scale *= 1.25f
	}

	override fun buildGeometry(consumer: VertexConsumer, camera: Camera, deltaTick: Float) {
		val rotation = Quaternionf()
			.rotateZ(MathHelper.lerp(deltaTick, this.prevAngle, this.angle))
			.rotateY(MathHelper.lerp(deltaTick, this.prevYaw, this.yaw))
			.rotateX(MathHelper.lerp(deltaTick, this.prevPitch, this.pitch))

		val vertices = arrayOf(Vector3f(-this.scale, -this.scale, 0f), Vector3f(-this.scale, this.scale, 0f), Vector3f(this.scale, this.scale, 0f), Vector3f(this.scale, -this.scale, 0f)).map { vertex ->
			vertex.rotate(rotation).add(
				(MathHelper.lerp(deltaTick, this.prevPosX.toFloat(), this.x.toFloat()) - camera.pos.x.toFloat()),
				(MathHelper.lerp(deltaTick, this.prevPosY.toFloat(), this.y.toFloat()) - camera.pos.y.toFloat()),
				(MathHelper.lerp(deltaTick, this.prevPosZ.toFloat(), this.z.toFloat()) - camera.pos.z.toFloat())
			)
		}

		val light = this.getBrightness(deltaTick)
		fun vertex(vertex: Vector3f, u: Float, v: Float) =
			consumer.vertex(vertex.x().toDouble(), vertex.y().toDouble(), vertex.z().toDouble()).texture(u, v).color(this.red, this.green, this.blue, this.alpha).light(light).next()

		vertex(vertices[0], this.maxU, this.maxV)
		vertex(vertices[1], this.maxU, this.minV)
		vertex(vertices[2], this.minU, this.minV)
		vertex(vertices[3], this.minU, this.maxV)

		vertex(vertices[3], this.minU, this.maxV)
		vertex(vertices[2], this.minU, this.minV)
		vertex(vertices[1], this.maxU, this.minV)
		vertex(vertices[0], this.maxU, this.maxV)
	}

	override fun tick() {
		this.velocityX += X_NOISE.sample(particleId, age.toDouble(), false) / 100
		this.velocityZ += Z_NOISE.sample(particleId, age.toDouble(), false) / 100

		this.prevYaw = this.yaw
		this.prevPitch = this.pitch
		this.prevAngle = this.angle

		if (onGround || (this.x == this.prevPosX && this.z == this.prevPosZ && this.y == this.prevPosY && this.age != 0)) {
			this.age = max(age.toDouble(), (this.maxAge - 20).toDouble()).toInt()
		} else {
			this.deltaYaw += (YAW_NOISE.sample(particleId, age.toDouble(), false)).toFloat() / 10f
			this.deltaRoll += (ROLL_NOISE.sample(particleId, age.toDouble(), false)).toFloat() / 10f
			this.deltaPitch += (PITCH_NOISE.sample(particleId, age.toDouble(), false)).toFloat() / 10f
			this.yaw += this.deltaYaw
			this.pitch += this.deltaPitch
			this.angle += this.deltaRoll
		}

		this.deltaYaw *= 0.98f
		this.deltaRoll *= 0.98f
		this.deltaPitch *= 0.98f

		super.tick()
	}

	class Factory(private val sprite: SpriteProvider) : ParticleFactory<DefaultParticleType> {
		override fun createParticle(typeIn: DefaultParticleType?, world: ClientWorld, x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double): Particle {
			val particle = ConfettiParticle(world, x, y, z, dx, dy, dz, sprite)
			particle.yaw = HexicalMain.RANDOM.nextFloat() * MathHelper.TAU
			particle.pitch = HexicalMain.RANDOM.nextFloat() * MathHelper.TAU
			particle.angle = HexicalMain.RANDOM.nextFloat() * MathHelper.TAU
			return particle
		}
	}

	override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
	companion object {
		private val X_NOISE: OctaveSimplexNoiseSampler = noise(58637214)
		private val Z_NOISE: OctaveSimplexNoiseSampler = noise(823917)
		private val YAW_NOISE: OctaveSimplexNoiseSampler = noise(28943157)
		private val ROLL_NOISE: OctaveSimplexNoiseSampler = noise(80085)
		private val PITCH_NOISE: OctaveSimplexNoiseSampler = noise(49715286)
		private fun noise(seed: Int) = OctaveSimplexNoiseSampler(CheckedRandom(seed.toLong()), listOf(-7, -2, -1, 0, 1, 2))
	}
}