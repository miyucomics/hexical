package miyucomics.hexical.features.sentinel_beds

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.inits.InitHook
import net.minecraft.nbt.NbtCompound

object SentinelBedAmbitHook : InitHook() {
	override fun init() {
		CastingEnvironment.addCreateEventListener { env: CastingEnvironment, _: NbtCompound ->
			env.addExtension(SentinelBedComponent(env))
		}
	}
}