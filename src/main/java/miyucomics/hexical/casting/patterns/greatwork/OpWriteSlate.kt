package miyucomics.hexical.casting.patterns.greatwork

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.blocks.circles.BlockEntitySlate
import at.petrak.hexcasting.common.lib.HexBlocks
import net.minecraft.util.math.BlockPos

class OpWriteSlate : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		val block = env.world.getBlockState(position)
		if (!block.isOf(HexBlocks.SLATE))
			throw MishapBadBlock.of(position, "slate")
		val pattern = args.getPattern(1, argc)
		return SpellAction.Result(Spell(position, env.world.getBlockEntity(position)!! as BlockEntitySlate, pattern), MediaConstants.DUST_UNIT * 3L / 8L, listOf())
	}

	private data class Spell(val position: BlockPos, val slate: BlockEntitySlate, val pattern: HexPattern) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			slate.pattern = pattern
			val state = env.world.getBlockState(position)
			env.world.updateListeners(position, state, state, 3)
			slate.markDirty()
		}
	}
}