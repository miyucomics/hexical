package miyucomics.hexical.particles

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.Vec3f

@Environment(value = EnvType.CLIENT)
class HexxyParticle
internal constructor(clientWorld: ClientWorld, x: Double, y: Double, z: Double) : Particle(clientWorld, x, y, z) {
	init {
		this.gravityStrength = 0.0f
		this.maxAge = 30
	}

	override fun getType(): ParticleTextureSheet = ParticleTextureSheet.CUSTOM

	override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
		val progress = (age.toFloat() + tickDelta) / maxAge.toFloat()
		val matricies = MatrixStack()
		matricies.multiply(camera.rotation)
		matricies.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(150.0f * progress - 60.0f))
		matricies.scale(-1.0f, -1.0f, 1.0f)
		matricies.translate(0.0, -1.101, 1.5)
	}

	@Environment(value = EnvType.CLIENT)
	class Factory : ParticleFactory<DefaultParticleType?> {
		override fun createParticle(defaultParticleType: DefaultParticleType?, clientWorld: ClientWorld, d: Double, e: Double, f: Double, g: Double, h: Double, i: Double): Particle {
			return HexxyParticle(clientWorld, d, e, f)
		}
	}
}