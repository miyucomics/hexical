package miyucomics.hexical.features.particles

import com.mojang.brigadier.StringReader
import miyucomics.hexical.inits.HexicalParticles
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.AbstractDustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import org.joml.Vector3f
import java.util.*


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

	override fun getType(): ParticleTextureSheet {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
	}

	@JvmRecord
	data class Factory(val spriteProvider: SpriteProvider) : ParticleFactory<SparkleParticleEffect> {
		override fun createParticle(effect: SparkleParticleEffect, world: ClientWorld, d: Double, e: Double, f: Double, g: Double, h: Double, i: Double): Particle {
			val sparkleParticle = SparkleParticle(world, d, e, f, g, h, i, this.spriteProvider)
			sparkleParticle.setColor(effect.color.x, effect.color.y, effect.color.z)
			sparkleParticle.setMaxAge(effect.lifespan)
			return sparkleParticle
		}
	}
}

class SparkleParticleEffect(val color: Vector3f, val lifespan: Int) : ParticleEffect {
	object Factory : ParticleEffect.Factory<SparkleParticleEffect> {
		override fun read(type: ParticleType<SparkleParticleEffect>, buf: PacketByteBuf): SparkleParticleEffect {
			return SparkleParticleEffect(AbstractDustParticleEffect.readColor(buf), buf.readInt())
		}

		override fun read(particleType: ParticleType<SparkleParticleEffect>, stringReader: StringReader): SparkleParticleEffect {
			val color = AbstractDustParticleEffect.readColor(stringReader)
			stringReader.expect(' ')
			val lifespan = stringReader.readInt()
			return SparkleParticleEffect(color, lifespan)
		}
	}

	override fun getType() = HexicalParticles.SPARKLE_PARTICLE
	override fun asString() = String.format(Locale.ROOT, "sparkle_particle %.2f %.2f %.2f", color.x(), color.y(), color.z())

	override fun write(packet: PacketByteBuf) {
		packet.writeFloat(color.x())
		packet.writeFloat(color.y())
		packet.writeFloat(color.z())
		packet.writeInt(lifespan)
	}
}