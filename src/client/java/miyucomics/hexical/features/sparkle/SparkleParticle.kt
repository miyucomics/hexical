package miyucomics.hexical.features.sparkle

import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteBillboardParticle
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.world.ClientWorld

class SparkleParticle(world: ClientWorld?, x: Double, y: Double, z: Double, velocityX: Double, velocityY: Double, velocityZ: Double, provider: SpriteProvider) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
	private val spriteProvider: SpriteProvider

	init {
		this.maxAge = 20
		this.velocityX = 0.0
		this.velocityY = 0.0
		this.velocityZ = 0.0
		this.spriteProvider = provider
		this.setSprite(provider)
		this.setSpriteForAge(provider)
		this.scale(2.5f)
	}

	override fun tick() {
		super.tick()
		this.setSpriteForAge(this.spriteProvider)
	}

	public override fun getBrightness(tint: Float): Int {
		val i = super.getBrightness(tint)
		val k = i shr 16 and 0xFF
		return 240 or (k shl 16)
	}

	override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

	data class Factory(val spriteProvider: SpriteProvider) : ParticleFactory<SparkleParticleEffect> {
		override fun createParticle(effect: SparkleParticleEffect, world: ClientWorld, d: Double, e: Double, f: Double, g: Double, h: Double, i: Double) = SparkleParticle(world, d, e, f, g, h, i, this.spriteProvider).apply {
			setColor(effect.color.x, effect.color.y, effect.color.z)
			setMaxAge(effect.lifespan)
		}
	}
}