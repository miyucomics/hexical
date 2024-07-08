package miyucomics.hexical.casting.patterns.akashic

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.spell.mishaps.MishapOthersName
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.HexBlocks
import net.minecraft.util.math.BlockPos

class OpWriteAkashicShelf : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val position = args.getBlockPos(0, argc)
		ctx.assertVecInRange(position)
		val block = ctx.world.getBlockState(position)
		if (!block.isOf(HexBlocks.AKASHIC_BOOKSHELF))
			throw MishapBadBlock.of(position, "akashic_bookshelf")
		val pattern = args.getPattern(1, argc)
		val iota = args[2]
		val trueName = MishapOthersName.getTrueNameFromDatum(iota, ctx.caster)
		if (trueName != null)
			throw MishapOthersName(trueName)
		return Triple(Spell(position, pattern, iota), 0, listOf())
	}

	private data class Spell(val position: BlockPos, val pattern: HexPattern, val iota: Iota) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val shelf = ctx.world.getBlockEntity(position)!! as BlockEntityAkashicBookshelf
			shelf.clearIota()
			shelf.setNewMapping(pattern, iota)
			shelf.markDirty()
		}
	}
}