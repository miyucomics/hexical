package miyucomics.hexical.features.wristpocket

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

object WristpocketUtils {
	fun getWristpocketStack(env: CastingEnvironment): ItemStack? {
		return when (env) {
			is PlayerBasedCastEnv -> (env.castingEntity as PlayerEntity).wristpocket
			else -> null
		}
	}

	fun setWristpocketStack(env: CastingEnvironment, stack: ItemStack) {
		when (env) {
			is PlayerBasedCastEnv -> (env.castingEntity as PlayerEntity).wristpocket = stack
		}
	}
}