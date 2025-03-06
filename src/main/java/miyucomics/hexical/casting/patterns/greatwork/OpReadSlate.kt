package miyucomics.hexical.casting.patterns.greatwork

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.common.blocks.circles.BlockEntitySlate
import at.petrak.hexcasting.common.lib.HexBlocks

class OpReadSlate : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		val block = env.world.getBlockState(position)
		if (!block.isOf(HexBlocks.SLATE))
			throw MishapBadBlock.of(position, "slate")
		val pattern = (env.world.getBlockEntity(position) as BlockEntitySlate).pattern ?: return listOf(NullIota())
		return pattern.asActionResult
	}
}