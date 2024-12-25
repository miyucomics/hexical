package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.casting.ParticleSpray
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
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.entity.Entity
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

class OpMageHand : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()

		when (val iota = args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				return SpellAction.Result(EntitySpell(entity), MediaConstants.DUST_UNIT, listOf())
			}
			is Vec3Iota -> {
				val block = args.getBlockPos(0, argc)
				env.assertVecInRange(block)
				return SpellAction.Result(BlockSpell(block), MediaConstants.DUST_UNIT, listOf())
			}
			else -> throw MishapInvalidIota.of(iota, 0, "entity_or_vector")
		}
	}

	private data class BlockSpell(val position: BlockPos) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val stack = PersistentStateHandler.getWristpocketStack(env.caster)
			val originalItem = env.caster.getStackInHand(env.castingHand)
			env.caster.setStackInHand(env.castingHand, stack)
			val block = env.world.getBlockState(position)
			block.onUse(env.world, env.caster, env.castingHand, BlockHitResult(Vec3d.ofCenter(position), Direction.UP, position, false))
			stack.useOnBlock(ItemUsageContext(env.caster, env.castingHand, BlockHitResult(Vec3d.ofCenter(position), Direction.UP, position, false)))
			PersistentStateHandler.setWristpocketStack(env.caster, env.caster.getStackInHand(env.castingHand))
			env.caster.setStackInHand(env.castingHand, originalItem)
		}
	}

	private data class EntitySpell(val entity: Entity) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val stack = PersistentStateHandler.getWristpocketStack(env.caster)
			val originalItem = env.caster.getStackInHand(env.castingHand)
			env.caster.setStackInHand(env.castingHand, stack)
			entity.interact(env.caster, Hand.MAIN_HAND)
			PersistentStateHandler.setWristpocketStack(env.caster, env.caster.getStackInHand(env.castingHand))
			env.caster.setStackInHand(env.castingHand, originalItem)
		}
	}
}