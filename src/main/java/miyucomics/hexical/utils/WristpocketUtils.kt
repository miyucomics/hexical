package miyucomics.hexical.utils

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import net.minecraft.item.ItemStack

object WristpocketUtils {
	fun getWristpocketStack(env: CastingEnvironment): ItemStack? {
		return when (env) {
			is PlayerBasedCastEnv -> (env.castingEntity as PlayerEntityMinterface).getWristpocket()
			else -> null
		}
	}

	fun setWristpocketStack(env: CastingEnvironment, stack: ItemStack) {
		when (env) {
			is PlayerBasedCastEnv -> (env.castingEntity as PlayerEntityMinterface).setWristpocket(stack)
		}
	}
}