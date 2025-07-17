package miyucomics.hexical.features.akashic

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.HexBlocks

class OpReadAkashicShelf : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)

		val block = env.world.getBlockState(position)
		if (!block.isOf(HexBlocks.AKASHIC_BOOKSHELF))
			throw MishapBadBlock.of(position, "akashic_bookshelf")
		val nbt = (env.world.getBlockEntity(position) as BlockEntityAkashicBookshelf).iotaTag ?: return listOf(NullIota())
		return listOf(IotaType.deserialize(nbt, env.world))
	}
}