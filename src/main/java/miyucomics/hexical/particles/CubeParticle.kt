package miyucomics.hexical.particles

import com.mojang.brigadier.StringReader
import miyucomics.hexical.registry.HexicalParticles
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.world.ClientWorld
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.AbstractDustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import java.util.*
import kotlin.math.max

class CubeParticle(world: ClientWorld?, x: Double, y: Double, z: Double) : SpriteBillboardParticle(world, x, y, z) {
	override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
		val cam = camera.pos
		val centerX = (x - cam.x).toFloat()
		val centerY = (y - cam.y).toFloat()
		val centerZ = (z - cam.z).toFloat()
		val alpha = max(0f, (1f - (age + tickDelta) / maxAge.toFloat()))
		for (face in faces)
			renderFace(vertexConsumer, face, centerX, centerY, centerZ, alpha)
	}

	private fun renderFace(consumer: VertexConsumer, indices: IntArray, x: Float, y: Float, z: Float, alpha: Float) {
		val uvs = arrayOf(
			floatArrayOf(this.maxU, this.maxV),
			floatArrayOf(this.maxU, this.minV),
			floatArrayOf(this.minU, this.minV),
			floatArrayOf(this.minU, this.maxV)
		)

		for (i in indices.indices) {
			val pos = positions[indices[i]]
			val uv = uvs[i]
			consumer.vertex(x + pos.x, y + pos.y, z + pos.z).texture(uv[0], uv[1]).color(red, green, blue, alpha).light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
		}
	}

	override fun getType(): ParticleTextureSheet {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
	}

	class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<CubeParticleEffect> {
		override fun createParticle(effect: CubeParticleEffect, clientWorld: ClientWorld, d: Double, e: Double, f: Double, g: Double, h: Double, i: Double): Particle {
			val particle = CubeParticle(clientWorld, d, e, f)
			particle.setSprite(this.spriteProvider)
			particle.setColor(effect.color.x, effect.color.y, effect.color.z)
			particle.setMaxAge(effect.lifespan)
			return particle
		}
	}

	companion object {
		private const val SCALE = 0.501

		private val positions = arrayOf(
			Vec3d( SCALE,  SCALE,  SCALE),
			Vec3d( SCALE, -SCALE,  SCALE),
			Vec3d(-SCALE, -SCALE,  SCALE),
			Vec3d(-SCALE,  SCALE,  SCALE),
			Vec3d( SCALE,  SCALE, -SCALE),
			Vec3d( SCALE, -SCALE, -SCALE),
			Vec3d(-SCALE, -SCALE, -SCALE),
			Vec3d(-SCALE,  SCALE, -SCALE)
		)

		private val faces = arrayOf(
			intArrayOf(2, 1, 0, 3),
			intArrayOf(6, 7, 4, 5),
			intArrayOf(7, 3, 0, 4),
			intArrayOf(2, 6, 5, 1),
			intArrayOf(1, 5, 4, 0),
			intArrayOf(2, 3, 7, 6)
		)
	}
}

class CubeParticleEffect(val color: Vector3f, val lifespan: Int) : ParticleEffect {
	object Factory : ParticleEffect.Factory<CubeParticleEffect> {
		override fun read(type: ParticleType<CubeParticleEffect>, buf: PacketByteBuf): CubeParticleEffect {
			return CubeParticleEffect(AbstractDustParticleEffect.readColor(buf), buf.readInt())
		}

		override fun read(particleType: ParticleType<CubeParticleEffect>, stringReader: StringReader): CubeParticleEffect {
			val color = AbstractDustParticleEffect.readColor(stringReader)
			stringReader.expect(' ')
			val lifespan = stringReader.readInt()
			return CubeParticleEffect(color, lifespan)
		}
	}

	override fun getType() = HexicalParticles.CUBE_PARTICLE
	override fun asString() = String.format(Locale.ROOT, "cube_particle %.2f %.2f %.2f", color.x(), color.y(), color.z())

	override fun write(packet: PacketByteBuf) {
		packet.writeFloat(color.x())
		packet.writeFloat(color.y())
		packet.writeFloat(color.z())
		packet.writeInt(lifespan)
	}
}