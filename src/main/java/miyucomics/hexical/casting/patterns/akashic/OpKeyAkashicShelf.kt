package miyucomics.hexical.casting.patterns.akashic

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.HexBlocks

class OpKeyAkashicShelf : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val position = args.getBlockPos(0, argc)
		ctx.assertPosInRange(position)
		val block = ctx.world.getBlockState(position)
		if (!block.isOf(HexBlocks.AKASHIC_BOOKSHELF))
			throw MishapBadBlock.of(position, "akashic_bookshelf")
		val pattern = (ctx.world.getBlockEntity(position) as BlockEntityAkashicBookshelf).pattern ?: return listOf(NullIota())
		return pattern.asActionResult
	}
}