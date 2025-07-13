package miyucomics.hexical.features.media_log

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import miyucomics.hexical.casting.components.MediaLogComponent
import miyucomics.hexical.features.player.fields.MediaLogField.Companion.isEnvCompatible
import miyucomics.hexical.inits.Hook
import net.minecraft.nbt.NbtCompound

object ServerSpyingHooks : Hook() {
	override fun registerCallbacks() {
		CastingEnvironment.addCreateEventListener { env: CastingEnvironment, _: NbtCompound ->
			if (isEnvCompatible(env))
				env.addExtension(MediaLogComponent(env as PlayerBasedCastEnv))
		}
	}
}