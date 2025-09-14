package miyucomics.hexical.features.akashic

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
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.misc.CastingUtils
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

object OpWriteAkashicShelf : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getBlockPos(0, argc)
		env.assertPosInRange(pos)
		val state = env.world.getBlockState(pos)
		if (!state.isOf(HexBlocks.AKASHIC_BOOKSHELF))
			throw MishapBadBlock.of(pos, "akashic_bookshelf")

		val key = args.getPattern(1, argc)
		val iota = args[2]
		CastingUtils.assertNoTruename(iota, env)
		return SpellAction.Result(Spell(pos, state, env.world.getBlockEntity(pos)!! as BlockEntityAkashicBookshelf, key, iota), 0, listOf())
	}

	private data class Spell(val pos: BlockPos, val state: BlockState, val shelf: BlockEntityAkashicBookshelf, val key: HexPattern, val iota: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			shelf.clearIota()
			shelf.setNewMapping(key, iota)
			shelf.markDirty()
			env.world!!.updateNeighborsAlways(pos, HexicalBlocks.MAGE_BLOCK)
			env.world!!.updateListeners(pos, state, state, 3)
		}
	}
}