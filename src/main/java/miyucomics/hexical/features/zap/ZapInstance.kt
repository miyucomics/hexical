package miyucomics.hexical.features.zap

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.math.BlockPos

data class ZapInstance(val position: BlockPos, val power: Int, val time: Int) {
	companion object {
		val CODEC: Codec<ZapInstance> = RecordCodecBuilder.create { it.group(
			BlockPos.CODEC.fieldOf("position").forGetter(ZapInstance::position),
			Codec.INT.fieldOf("power").forGetter(ZapInstance::power),
			Codec.INT.fieldOf("time").forGetter(ZapInstance::time)
		).apply(it, ::ZapInstance) }
	}
}