package miyucomics.hexical.features.media_log

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import miyucomics.hexical.inits.InitHook
import net.minecraft.nbt.NbtCompound

object ServerSpyingHooks : InitHook() {
	override fun init() {
		CastingEnvironment.addCreateEventListener { env: CastingEnvironment, _: NbtCompound ->
			if (MediaLogField.isEnvCompatible(env))
				env.addExtension(MediaLogComponent(env as PlayerBasedCastEnv))
		}
	}
}