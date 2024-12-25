package miyucomics.hexical.casting.patterns.prestidigitation

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.data.PrestidigitationData
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos

class OpPrestidigitation : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		return when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				val effect = PrestidigitationData.entityEffect(entity) ?: throw MishapBadEntity.of(entity, "prestidigitation")
				SpellAction.Result(EntitySpell(entity, effect), MediaConstants.DUST_UNIT / 10, listOf())
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				env.assertPosInRange(position)
				val effect = PrestidigitationData.blockEffect(env.world.getBlockState(position).block) ?: throw MishapBadBlock.of(position, "prestidigitation")
				SpellAction.Result(BlockSpell(position, effect), MediaConstants.DUST_UNIT / 10, listOf())
			}
			else -> throw MishapInvalidIota.of(args[0], 0, "entity_or_vector")
		}
	}

	private data class BlockSpell(val position: BlockPos, val effect: PrestidigitationEffect) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			effect.effectBlock(env.castingEntity, position)
		}
	}

	private data class EntitySpell(val entity: Entity, val effect: PrestidigitationEffect) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			effect.effectEntity(env.castingEntity, entity)
		}
	}
}