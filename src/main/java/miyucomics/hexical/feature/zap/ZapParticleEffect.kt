package miyucomics.hexical.feature.zap

import com.mojang.brigadier.StringReader
import miyucomics.hexical.init.HexicalParticles
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.AbstractDustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import org.joml.Vector3f
import java.util.*

class ZapParticleEffect(val offset: Vector3f, val color: Vector3f) : ParticleEffect {
	object Factory : ParticleEffect.Factory<ZapParticleEffect> {
		override fun read(type: ParticleType<ZapParticleEffect>, buf: PacketByteBuf) = ZapParticleEffect(AbstractDustParticleEffect.readColor(buf), AbstractDustParticleEffect.readColor(buf))
		override fun read(particleType: ParticleType<ZapParticleEffect>, stringReader: StringReader) = ZapParticleEffect(AbstractDustParticleEffect.readColor(stringReader), AbstractDustParticleEffect.readColor(stringReader))
	}

	override fun getType() = HexicalParticles.ZAP_PARTICLE
	override fun asString() = String.format(Locale.ROOT, "zap %.2f %.2f %.2f %.2f %.2f %.2f", offset.x(), offset.y(), offset.z(), color.x(), color.y(), color.z())

	override fun write(packet: PacketByteBuf) {
		packet.writeFloat(offset.x())
		packet.writeFloat(offset.y())
		packet.writeFloat(offset.z())
		packet.writeFloat(color.x())
		packet.writeFloat(color.y())
		packet.writeFloat(color.z())
	}
}