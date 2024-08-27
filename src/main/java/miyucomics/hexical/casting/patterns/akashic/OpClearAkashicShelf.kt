package miyucomics.hexical.casting.patterns.akashic

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getBlockPos
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadBlock
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.HexBlocks
import net.minecraft.util.math.BlockPos

class OpClearAkashicShelf : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val position = args.getBlockPos(0, argc)
		ctx.assertVecInRange(position)
		val block = ctx.world.getBlockState(position)
		if (!block.isOf(HexBlocks.AKASHIC_BOOKSHELF))
			throw MishapBadBlock.of(position, "akashic_bookshelf")
		return Triple(Spell(position), 0, listOf())
	}

	private data class Spell(val position: BlockPos) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val shelf = ctx.world.getBlockEntity(position)!! as BlockEntityAkashicBookshelf
			shelf.clearIota()
			shelf.markDirty()
		}
	}
}