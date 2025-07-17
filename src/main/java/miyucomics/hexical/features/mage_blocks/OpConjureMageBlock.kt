package miyucomics.hexical.features.mage_blocks

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class OpConjureMageBlock : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		if (!env.world.getBlockState(position).isReplaceable)
			throw MishapBadBlock.of(position, "replaceable")
		return SpellAction.Result(Spell(position), MediaConstants.DUST_UNIT * 3, listOf(ParticleSpray.cloud(Vec3d.ofCenter(position), 1.0)))
	}

	private data class Spell(val position: BlockPos) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			if (!env.canEditBlockAt(position))
				return
			env.world.setBlockState(position, HexicalBlocks.MAGE_BLOCK.defaultState)
		}
	}
}