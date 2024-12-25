package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.entity.Entity
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

class OpMageHand : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		when (val iota = args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				ctx.assertEntityInRange(entity)
				return Triple(EntitySpell(entity), MediaConstants.DUST_UNIT, listOf())
			}
			is Vec3Iota -> {
				val block = args.getBlockPos(0, argc)
				ctx.assertVecInRange(block)
				return Triple(BlockSpell(block), MediaConstants.DUST_UNIT, listOf())
			}
			else -> throw MishapInvalidIota.of(iota, 0, "entity_or_vector")
		}
	}

	private data class BlockSpell(val position: BlockPos) : RenderedSpell {
		override fun cast(ctx: CastingEnvironment) {
			val stack = PersistentStateHandler.getWristpocketStack(ctx.caster)
			val originalItem = ctx.caster.getStackInHand(ctx.castingHand)
			ctx.caster.setStackInHand(ctx.castingHand, stack)
			val block = ctx.world.getBlockState(position)
			block.onUse(ctx.world, ctx.caster, ctx.castingHand, BlockHitResult(Vec3d.ofCenter(position), Direction.UP, position, false))
			stack.useOnBlock(ItemUsageContext(ctx.caster, ctx.castingHand, BlockHitResult(Vec3d.ofCenter(position), Direction.UP, position, false)))
			PersistentStateHandler.setWristpocketStack(ctx.caster, ctx.caster.getStackInHand(ctx.castingHand))
			ctx.caster.setStackInHand(ctx.castingHand, originalItem)
		}
	}

	private data class EntitySpell(val entity: Entity) : RenderedSpell {
		override fun cast(ctx: CastingEnvironment) {
			val stack = PersistentStateHandler.getWristpocketStack(ctx.caster)
			val originalItem = ctx.caster.getStackInHand(ctx.castingHand)
			ctx.caster.setStackInHand(ctx.castingHand, stack)
			entity.interact(ctx.caster, Hand.MAIN_HAND)
			PersistentStateHandler.setWristpocketStack(ctx.caster, ctx.caster.getStackInHand(ctx.castingHand))
			ctx.caster.setStackInHand(ctx.castingHand, originalItem)
		}
	}
}