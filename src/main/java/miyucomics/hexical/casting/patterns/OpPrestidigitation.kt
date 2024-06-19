package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.data.PrestidigitationData
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos

class OpPrestidigitation : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		return when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				ctx.assertEntityInRange(entity)
				val effect = PrestidigitationData.entityEffect(entity) ?: throw MishapBadEntity.of(entity, "prestidigitation")
				Triple(EntitySpell(entity, effect), effect.getCost(), listOf())
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				ctx.assertVecInRange(position)
				val effect = PrestidigitationData.blockEffect(ctx.world.getBlockState(position).block) ?: throw MishapBadBlock.of(position, "prestidigitation")
				Triple(BlockSpell(position, effect), effect.getCost(), listOf())
			}
			else -> throw MishapInvalidIota.of(args[0], 0, "entity_or_vector")
		}
	}

	private data class BlockSpell(val position: BlockPos, val effect: PrestidigitationEffect) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			effect.effectBlock(ctx.caster, position)
		}
	}

	private data class EntitySpell(val entity: Entity, val effect: PrestidigitationEffect) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			effect.effectEntity(ctx.caster, entity)
		}
	}
}