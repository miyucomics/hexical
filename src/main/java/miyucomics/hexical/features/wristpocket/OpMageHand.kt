package miyucomics.hexical.features.wristpocket

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

class OpMageHand : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()

		val wristpocket = WristpocketUtils.getWristpocketStack(env) ?: throw NeedsWristpocketMishap()

		when (val iota = args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				return SpellAction.Result(EntitySpell(entity, wristpocket), MediaConstants.DUST_UNIT, listOf())
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				env.assertPosInRange(position)
				return SpellAction.Result(BlockSpell(position, wristpocket), MediaConstants.DUST_UNIT, listOf())
			}
			else -> throw MishapInvalidIota.of(iota, 0, "entity_or_vector")
		}
	}

	private data class BlockSpell(val position: BlockPos, val wristpocket: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val caster = env.castingEntity as ServerPlayerEntity
			val originalItem = caster.getStackInHand(env.castingHand)

			caster.setStackInHand(env.castingHand, wristpocket)
			val block = env.world.getBlockState(position)
			val result = block.onUse(env.world, caster, env.castingHand, BlockHitResult(Vec3d.ofCenter(position), Direction.UP, position, false))
			if (!result.isAccepted)
				wristpocket.useOnBlock(ItemUsageContext(caster, env.castingHand, BlockHitResult(Vec3d.ofCenter(position), Direction.UP, position, false)))

			WristpocketUtils.setWristpocketStack(env, caster.getStackInHand(env.castingHand))
			caster.setStackInHand(env.castingHand, originalItem)
		}
	}

	private data class EntitySpell(val entity: Entity, val wristpocket: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val caster = env.castingEntity as ServerPlayerEntity
			val originalItem = caster.getStackInHand(env.castingHand)

			caster.setStackInHand(env.castingHand, wristpocket)
			val result = entity.interact(caster, env.castingHand)
			if (!result.isAccepted && entity is LivingEntity)
				wristpocket.useOnEntity(caster, entity, env.castingHand)

			WristpocketUtils.setWristpocketStack(env, caster.getStackInHand(env.castingHand))
			caster.setStackInHand(env.castingHand, originalItem)
		}
	}
}