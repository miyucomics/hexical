package miyucomics.hexical.features.sparkle

import com.mojang.brigadier.StringReader
import miyucomics.hexical.inits.HexicalParticles
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.AbstractDustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import org.joml.Vector3f
import java.util.*

class SparkleParticleEffect(val color: Vector3f, val lifespan: Int) : ParticleEffect {
	object Factory : ParticleEffect.Factory<SparkleParticleEffect> {
		override fun read(type: ParticleType<SparkleParticleEffect>, buf: PacketByteBuf) = SparkleParticleEffect(AbstractDustParticleEffect.readColor(buf), buf.readInt())

		override fun read(particleType: ParticleType<SparkleParticleEffect>, stringReader: StringReader): SparkleParticleEffect {
			val color = AbstractDustParticleEffect.readColor(stringReader)
			stringReader.expect(' ')
			val lifespan = stringReader.readInt()
			return SparkleParticleEffect(color, lifespan)
		}
	}

	override fun getType() = HexicalParticles.SPARKLE_PARTICLE
	override fun asString() = String.format(Locale.ROOT, "sparkle %.2f %.2f %.2f", color.x(), color.y(), color.z())

	override fun write(packet: PacketByteBuf) {
		packet.writeFloat(color.x())
		packet.writeFloat(color.y())
		packet.writeFloat(color.z())
		packet.writeInt(lifespan)
	}
}