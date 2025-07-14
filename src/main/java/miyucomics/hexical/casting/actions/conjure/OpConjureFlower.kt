package miyucomics.hexical.casting.actions.conjure

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexpose.iotas.getIdentifier
import net.minecraft.block.Block
import net.minecraft.block.FlowerPotBlock
import net.minecraft.block.TallPlantBlock
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

class OpConjureFlower : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)

		val id = args.getIdentifier(1, argc)
		if (!Registries.BLOCK.containsId(id))
			throw MishapInvalidIota.of(args[1], 0, "conjurable_flower_id")
		val type = Registries.BLOCK.get(id)
		if (!type.defaultState.isIn(HexicalBlocks.CONJURABLE_FLOWERS))
			throw MishapInvalidIota.of(args[1], 0, "conjurable_flower_id")

		if (env.world.getBlockState(position).isIn(BlockTags.FLOWER_POTS))
			return SpellAction.Result(PotPlant(position, type), MediaConstants.DUST_UNIT / 4, listOf(ParticleSpray.cloud(Vec3d.ofCenter(position), 1.0)))

		if (!env.world.getBlockState(position).isReplaceable)
			throw MishapBadBlock.of(position, "flower_spawnable")
		if (type.defaultState.properties.contains(TallPlantBlock.HALF) && !env.world.getBlockState(position.up()).isReplaceable)
			throw MishapBadBlock.of(position.up(), "flower_spawnable")
		if (!env.world.getBlockState(position.down()).isSideSolidFullSquare(env.world, position.down(), Direction.UP))
			throw MishapBadBlock.of(position.down(), "solid_platform")

		return SpellAction.Result(GroundPlant(position, type), MediaConstants.DUST_UNIT / 4, listOf(ParticleSpray.cloud(Vec3d.ofCenter(position), 1.0)))
	}

	private data class GroundPlant(val position: BlockPos, val flower: Block) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			if (flower is TallPlantBlock) {
				TallPlantBlock.placeAt(env.world, flower.defaultState, position, Block.NOTIFY_LISTENERS or Block.FORCE_STATE)
			}
			else {
				env.world.setBlockState(position, flower.defaultState)
			}
		}
	}

	private data class PotPlant(val position: BlockPos, val flower: Block) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val pot = FlowerPotBlock.CONTENT_TO_POTTED[flower] ?: return
			env.world.setBlockState(position, pot.defaultState)
		}
	}
}