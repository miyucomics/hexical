package miyucomics.hexical.features.wristpocket

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import miyucomics.hexical.features.pedestal.PedestalBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos

object WristpocketUtils {
	fun getWristpocketStack(env: CastingEnvironment): ItemStack? {
		return when (env) {
			is PlayerBasedCastEnv -> (env.castingEntity as PlayerEntity).wristpocket
			is CircleCastEnv -> {
				val data = env.circleState().currentImage.userData
				if (data.contains("impetus_hand")) {
					val position = data.getIntArray("impetus_hand")
					val pedestal = env.world.getBlockEntity(BlockPos(position[0], position[1], position[2]))
					(pedestal as PedestalBlockEntity).getStack(0)
				} else
					null
			}
			else -> null
		}
	}

	fun setWristpocketStack(env: CastingEnvironment, stack: ItemStack) {
		when (env) {
			is PlayerBasedCastEnv -> (env.castingEntity as PlayerEntity).wristpocket = stack
			is CircleCastEnv -> {
				val data = env.circleState().currentImage.userData
				if (data.contains("impetus_hand")) {
					val position = data.getIntArray("impetus_hand")
					val pedestal = env.world.getBlockEntity(BlockPos(position[0], position[1], position[2]))
					(pedestal as PedestalBlockEntity).setStack(0, stack)
				}
			}
		}
	}
}