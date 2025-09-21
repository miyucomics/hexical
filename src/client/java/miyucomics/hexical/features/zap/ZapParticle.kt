package miyucomics.hexical.features.zap

import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.Vec3d

class ZapParticle(world: ClientWorld, x: Double, y: Double, z: Double, val offset: Vec3d) : Particle(world, x, y, z) {
	override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera?, tickDelta: Float) {

	}

	override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

	class Factory(val spriteProvider: SpriteProvider) : ParticleFactory<ZapParticleEffect> {
		override fun createParticle(effect: ZapParticleEffect, world: ClientWorld, x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double) = ZapParticle(world, x, y, z, effect.offset).apply {
			setColor(effect.color.x, effect.color.y, effect.color.z)
			setVelocity(0.0, 0.0, 0.0)
			maxAge = 10
		}
	}
}