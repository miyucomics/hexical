package miyucomics.hexical.features.akashic

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.HexBlocks

object OpClearAkashicShelf : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		val block = env.world.getBlockState(position)
		if (!block.isOf(HexBlocks.AKASHIC_BOOKSHELF))
			throw MishapBadBlock.of(position, "akashic_bookshelf")
		return SpellAction.Result(Spell(env.world.getBlockEntity(position)!! as BlockEntityAkashicBookshelf), 0, listOf())
	}

	private data class Spell(val shelf: BlockEntityAkashicBookshelf) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			shelf.clearIota()
			shelf.markDirty()
		}
	}
}