package miyucomics.hexical.feature.sentinel_bed

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.misc.InitHook
import net.minecraft.nbt.NbtCompound

object SentinelBedAmbitHook : InitHook() {
	override fun init() {
		CastingEnvironment.addCreateEventListener { env: CastingEnvironment, _: NbtCompound ->
			env.addExtension(SentinelBedComponent(env))
		}
	}
}