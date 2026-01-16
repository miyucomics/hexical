package miyucomics.hexical.features.prestidigitation

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.features.prestidigitation.handlers.PrestidigitationHandlerBlock
import miyucomics.hexical.features.prestidigitation.handlers.PrestidigitationHandlerEntity
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

object OpPrestidigitation : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		return when (val iota = args[0]) {
			is EntityIota -> {
				val entity = iota.entity
				env.assertEntityInRange(entity)
				SpellAction.Result(EntitySpell(entity, PrestidigitationHandlers.resolve(env, entity) ?: throw MishapNoPrestidigitation(entity.eyePos)), MediaConstants.DUST_UNIT / 10, listOf(ParticleSpray.cloud(entity.pos, 1.0)))
			}
			is Vec3Iota -> {
				val pos = BlockPos.ofFloored(iota.vec3)
				env.assertPosInRange(pos)
				SpellAction.Result(BlockSpell(pos, PrestidigitationHandlers.resolve(env, pos) ?: throw MishapNoPrestidigitation(Vec3d.ofCenter(pos))), MediaConstants.DUST_UNIT / 10, listOf(ParticleSpray.cloud(Vec3d.ofCenter(pos), 1.0)))
			}
			else -> throw MishapInvalidIota.of(args[0], 0, "entity_or_vector")
		}
	}

	private data class BlockSpell(val pos: BlockPos, val handler: PrestidigitationHandlerBlock) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			handler.affect(env, pos)
		}
	}

	private data class EntitySpell(val entity: Entity, val handler: PrestidigitationHandlerEntity<*>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			handler.affectSafely(env, entity)
		}
	}
}