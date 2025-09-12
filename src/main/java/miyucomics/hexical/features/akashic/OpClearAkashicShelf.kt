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
		val pos = args.getBlockPos(0, argc)
		env.assertPosInRange(pos)
		val state = env.world.getBlockState(pos)
		if (!state.isOf(HexBlocks.AKASHIC_BOOKSHELF))
			throw MishapBadBlock.of(pos, "akashic_bookshelf")
		return SpellAction.Result(Spell(env.world.getBlockEntity(pos)!! as BlockEntityAkashicBookshelf), 0, listOf())
	}

	private data class Spell(val shelf: BlockEntityAkashicBookshelf) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			shelf.clearIota()
			shelf.markDirty()
		}
	}
}