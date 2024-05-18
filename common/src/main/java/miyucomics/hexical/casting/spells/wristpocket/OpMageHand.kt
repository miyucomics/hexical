package miyucomics.hexical.casting.spells.wristpocket

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.entity.Entity
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos

class OpMageHand : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val entity = args.getEntity(0, argc)
		ctx.assertEntityInRange(entity)
		return Triple(EntitySpell(entity), MediaConstants.DUST_UNIT, listOf())
	}

	private data class BlockSpell(val position: BlockPos) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val stack = PersistentStateHandler.wristpocketItem(ctx.caster)
			val originalItem = ctx.caster.getStackInHand(ctx.castingHand)
			ctx.caster.setStackInHand(ctx.castingHand, stack)
			entity.interact(ctx.caster, Hand.MAIN_HAND)
			PersistentStateHandler.stashWristpocket(ctx.caster, ctx.caster.getStackInHand(ctx.castingHand))
			ctx.caster.setStackInHand(ctx.castingHand, originalItem)
		}
	}

	private data class EntitySpell(val entity: Entity) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val stack = PersistentStateHandler.wristpocketItem(ctx.caster)
			val originalItem = ctx.caster.getStackInHand(ctx.castingHand)
			ctx.caster.setStackInHand(ctx.castingHand, stack)
			entity.interact(ctx.caster, Hand.MAIN_HAND)
			PersistentStateHandler.stashWristpocket(ctx.caster, ctx.caster.getStackInHand(ctx.castingHand))
			ctx.caster.setStackInHand(ctx.castingHand, originalItem)
		}
	}
}