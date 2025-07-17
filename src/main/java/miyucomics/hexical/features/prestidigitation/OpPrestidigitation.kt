package miyucomics.hexical.features.prestidigitation

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class OpPrestidigitation : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		return when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				SpellAction.Result(EntitySpell(entity), MediaConstants.DUST_UNIT / 10, listOf(ParticleSpray.Companion.cloud(entity.pos, 1.0)))
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				env.assertPosInRange(position)
				SpellAction.Result(BlockSpell(position), MediaConstants.DUST_UNIT / 10, listOf(
					ParticleSpray.Companion.cloud(
						Vec3d.ofCenter(position), 1.0)))
			}
			else -> throw MishapInvalidIota.Companion.of(args[0], 0, "entity_or_vector")
		}
	}

	private data class BlockSpell(val position: BlockPos) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			PrestidigitationHandlersHook.PRESTIDIGITATION_HANDLER.forEach {
				if (it.tryHandleBlock(env, position))
					return
			}
		}
	}

	private data class EntitySpell(val entity: Entity) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			PrestidigitationHandlersHook.PRESTIDIGITATION_HANDLER.forEach {
				if (it.tryHandleEntity(env, entity))
					return
			}
		}
	}
}