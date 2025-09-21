package miyucomics.hexical.features.zap

import com.mojang.brigadier.StringReader
import miyucomics.hexical.inits.HexicalParticles
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.AbstractDustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import java.util.*

class ZapParticleEffect(val offset: Vec3d, val color: Vector3f) : ParticleEffect {
	object Factory : ParticleEffect.Factory<ZapParticleEffect> {
		override fun read(type: ParticleType<ZapParticleEffect>, buf: PacketByteBuf) = ZapParticleEffect(Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()), AbstractDustParticleEffect.readColor(buf))

		override fun read(particleType: ParticleType<ZapParticleEffect>, reader: StringReader): ZapParticleEffect {
			reader.expect(' ')
			val x = reader.readDouble()
			reader.expect(' ')
			val y = reader.readDouble()
			reader.expect(' ')
			val z = reader.readDouble()
			val offset = Vec3d(x, y, z)
			return ZapParticleEffect(offset, AbstractDustParticleEffect.readColor(reader))
		}
	}

	override fun getType() = HexicalParticles.ZAP_PARTICLE
	override fun asString() = String.format(Locale.ROOT, "zap %.2f %.2f %.2f %.2f %.2f %.2f", offset.x, offset.y, offset.z, color.x(), color.y(), color.z())

	override fun write(packet: PacketByteBuf) {
		packet.writeDouble(offset.x)
		packet.writeDouble(offset.y)
		packet.writeDouble(offset.z)
		packet.writeFloat(color.x())
		packet.writeFloat(color.y())
		packet.writeFloat(color.z())
	}
}