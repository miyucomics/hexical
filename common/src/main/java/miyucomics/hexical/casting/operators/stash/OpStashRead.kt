package miyucomics.hexical.casting.operators.stash

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.MishapNoAkashicRecord
import at.petrak.hexcasting.common.blocks.akashic.AkashicFloodfiller
import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicRecord
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.NoBoundLibraryMishap
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class OpStashRead(private val key: HexPattern) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val world = ctx.world
		val record = PersistentStateHandler.getPlayerBoundLibrary(ctx.caster) ?: throw NoBoundLibraryMishap()
		if (world.getBlockState(record).block !is BlockAkashicRecord)
			throw MishapNoAkashicRecord(record)

		val existingShelf = AkashicFloodfiller.floodFillFor(record, world) { pos: BlockPos?, _: BlockState?, world2: World -> world2.getBlockEntity(pos) is BlockEntityAkashicBookshelf && (world2.getBlockEntity(pos) as BlockEntityAkashicBookshelf).pattern == key }
		if (existingShelf != null)
			return listOf(HexIotaTypes.deserialize((world.getBlockEntity(existingShelf) as BlockEntityAkashicBookshelf).iotaTag!!, world))
		return listOf(NullIota())
	}
}