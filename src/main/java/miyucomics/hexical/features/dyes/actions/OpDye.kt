package miyucomics.hexical.features.dyes.actions

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
import miyucomics.hexical.features.dyes.DyeOption
import miyucomics.hexical.features.dyes.DyeableMishap
import miyucomics.hexical.features.dyes.DyeingUtils
import miyucomics.hexical.features.dyes.block.DyeingBlockRecipe
import miyucomics.hexical.features.dyes.entity.DyeEntityHandler
import miyucomics.hexical.features.dyes.entity.DyeingEntityRegistry
import miyucomics.hexical.features.dyes.getDye
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.state.property.Property
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

object OpDye : SpellAction {
	override val argc = 2
	private const val COST = MediaConstants.DUST_UNIT / 8
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val dye = args.getDye(1, argc)
		when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				val handler = DyeingEntityRegistry.resolve(entity, dye) ?: throw DyeableMishap(entity.pos)
				return SpellAction.Result(EntitySpell(entity, dye, handler), COST, listOf(ParticleSpray.cloud(entity.pos, 1.0)))
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				env.assertPosInRange(position)
				val state = env.world.getBlockState(position)
				val recipe = DyeingUtils.getRecipe(env.world, state, dye) ?: throw DyeableMishap(position.toCenterPos())
				return SpellAction.Result(BlockSpell(position, state, recipe), COST, listOf(ParticleSpray.cloud(Vec3d.ofCenter(position), 1.0)))
			}
			else -> throw MishapInvalidIota.of(args[0], 1, "entity_or_vector")
		}
	}

	private data class EntitySpell(val entity: Entity, val dye: DyeOption, val handler: DyeEntityHandler<*>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			handler.affectSafely(entity, dye)
		}
	}

	private data class BlockSpell(val position: BlockPos, val old: BlockState, val recipe: DyeingBlockRecipe) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			var newState = recipe.output
			old.properties.filter(newState.properties::contains).forEach {
				@Suppress("UNCHECKED_CAST")
				val prop = it as Property<Comparable<Any>>
				newState = newState.with(prop, old.get(prop))
			}
			env.world.setBlockState(position, newState)
		}
	}
}