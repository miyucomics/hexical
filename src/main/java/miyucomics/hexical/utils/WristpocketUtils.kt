package miyucomics.hexical.utils

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import net.minecraft.item.ItemStack

object WristpocketUtils {
	fun getWristpocketStack(env: CastingEnvironment): ItemStack? {
		when (env) {
			is PlayerBasedCastEnv -> return (env.castingEntity as PlayerEntityMinterface).getWristpocket()
			else -> return null
		}
	}

	fun setWristpocketStack(env: CastingEnvironment, stack: ItemStack) {
		when (env) {
			is PlayerBasedCastEnv -> (env.castingEntity as PlayerEntityMinterface).setWristpocket(stack)
		}
	}
}