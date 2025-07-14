package miyucomics.hexical.features.sentinel_beds

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.casting.components.SentinelBedComponent
import miyucomics.hexical.inits.Hook
import net.minecraft.nbt.NbtCompound

object SentinelBedAmbitHook : Hook() {
	override fun registerCallbacks() {
		CastingEnvironment.addCreateEventListener { env: CastingEnvironment, _: NbtCompound ->
			env.addExtension(SentinelBedComponent(env))
		}
	}
}