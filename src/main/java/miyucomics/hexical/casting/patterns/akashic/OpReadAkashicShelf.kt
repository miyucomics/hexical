package miyucomics.hexical.casting.patterns.akashic

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getBlockPos
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadBlock
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.HexBlocks
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes

class OpReadAkashicShelf : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val position = args.getBlockPos(0, argc)
		ctx.assertVecInRange(position)
		val block = ctx.world.getBlockState(position)
		if (!block.isOf(HexBlocks.AKASHIC_BOOKSHELF))
			throw MishapBadBlock.of(position, "akashic_bookshelf")
		val nbt = (ctx.world.getBlockEntity(position) as BlockEntityAkashicBookshelf).iotaTag?: return listOf(NullIota())
		return listOf(HexIotaTypes.deserialize(nbt, ctx.world))
	}
}