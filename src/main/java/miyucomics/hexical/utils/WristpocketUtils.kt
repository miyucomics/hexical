package miyucomics.hexical.utils

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import miyucomics.hexical.blocks.PedestalBlockEntity
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

object WristpocketUtils {
	fun getWristpocketStack(env: CastingEnvironment): ItemStack? {
		when (env) {
			is CircleCastEnv -> {
				val search = env.impetus!!.pos.add(0, 1, 0)
				val possiblePedestal = env.world.getBlockState(search)
				if (possiblePedestal.isOf(HexicalBlocks.PEDESTAL_BLOCK)) {
					val pedestal = env.world.getBlockEntity(search) as PedestalBlockEntity
					return pedestal.getStack(0)
				}

				if (env.castingEntity is ServerPlayerEntity)
					return PersistentStateHandler.getWristpocketStack(env.castingEntity as ServerPlayerEntity)

				return null
			}
			is PlayerBasedCastEnv -> return PersistentStateHandler.getWristpocketStack(env.castingEntity as ServerPlayerEntity)
			else -> return null
		}
	}

	fun setWristpocketStack(env: CastingEnvironment, stack: ItemStack) {
		when (env) {
			is CircleCastEnv -> {
				val search = env.impetus!!.pos.add(0, 1, 0)
				val possiblePedestal = env.world.getBlockState(search)
				if (possiblePedestal.isOf(HexicalBlocks.PEDESTAL_BLOCK)) {
					val pedestal = env.world.getBlockEntity(search) as PedestalBlockEntity
					val existing = pedestal.getStack(0)
					if (!existing.isEmpty)
						env.world.spawnEntity(ItemEntity(env.world, search.x.toDouble(), search.y.toDouble(), search.z.toDouble(), existing))
					pedestal.setStack(0, stack)
					return
				}

				if (env.castingEntity is ServerPlayerEntity)
					PersistentStateHandler.setWristpocketStack(env.castingEntity as ServerPlayerEntity, stack)
			}
			is PlayerBasedCastEnv -> PersistentStateHandler.setWristpocketStack(env.castingEntity as ServerPlayerEntity, stack)
		}
	}
}