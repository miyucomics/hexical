package miyucomics.hexical.casting.patterns.greatwork

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
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
		return when (val iota = args[1]) {
			is NullIota -> SpellAction.Result(WritePatternSpell(position, env.world.getBlockEntity(position)!! as BlockEntitySlate, null), MediaConstants.DUST_UNIT / 2L, listOf())
			is PatternIota -> SpellAction.Result(WritePatternSpell(position, env.world.getBlockEntity(position)!! as BlockEntitySlate, iota.pattern), MediaConstants.DUST_UNIT / 2L, listOf())
			else -> throw MishapInvalidIota.of(iota, 0, "null_or_pattern")
		}
	}

	private data class WritePatternSpell(val position: BlockPos, val slate: BlockEntitySlate, val pattern: HexPattern?) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			slate.pattern = pattern
			val state = env.world.getBlockState(position)
			env.world.updateListeners(position, state, state, 3)
			slate.markDirty()
		}
	}
}