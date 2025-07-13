package miyucomics.hexical.casting.actions.akashic

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.HexBlocks
import miyucomics.hexical.utils.CastingUtils

class OpWriteAkashicShelf : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		val block = env.world.getBlockState(position)
		if (!block.isOf(HexBlocks.AKASHIC_BOOKSHELF))
			throw MishapBadBlock.of(position, "akashic_bookshelf")

		val key = args.getPattern(1, argc)
		val iota = args[2]
		CastingUtils.assertNoTruename(iota, env)
		return SpellAction.Result(Spell(env.world.getBlockEntity(position)!! as BlockEntityAkashicBookshelf, key, iota), 0, listOf())
	}

	private data class Spell(val shelf: BlockEntityAkashicBookshelf, val key: HexPattern, val iota: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			shelf.clearIota()
			shelf.setNewMapping(key, iota)
			shelf.markDirty()
		}
	}
}