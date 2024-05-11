package miyucomics.hexical.casting.operators.stash

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.MishapNoAkashicRecord
import at.petrak.hexcasting.api.spell.mishaps.MishapOthersName
import at.petrak.hexcasting.common.blocks.akashic.AkashicFloodfiller
import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicRecord
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import miyucomics.hexical.casting.mishaps.NoBoundLibraryMishap
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class OpStashWrite(private val key: HexPattern) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val record = PersistentStateHandler.getPlayerBoundLibrary(ctx.caster) ?: throw NoBoundLibraryMishap()
		val trueName = MishapOthersName.getTrueNameFromDatum(args[0], ctx.caster)
		if (trueName != null)
			throw MishapOthersName(trueName)
		write(record, key, ctx.world, args[0])
		return listOf()
	}

	fun write(start: BlockPos, key: HexPattern, world: ServerWorld, iota: Iota) {
		val record = world.getBlockState(start).block
		if (record !is BlockAkashicRecord)
			throw MishapNoAkashicRecord(start)

		val existingShelf = AkashicFloodfiller.floodFillFor(start, world) { pos: BlockPos?, _: BlockState?, world2: World -> world2.getBlockEntity(pos) is BlockEntityAkashicBookshelf && (world2.getBlockEntity(pos) as BlockEntityAkashicBookshelf).pattern == key }
		if (existingShelf != null) {
			(world.getBlockEntity(existingShelf) as BlockEntityAkashicBookshelf).setNewMapping(key, iota)
			world.markDirty(existingShelf)
			return
		}
		val blankShelf = AkashicFloodfiller.floodFillFor(start, world) { pos: BlockPos?, _: BlockState?, world2: World -> world2.getBlockEntity(pos) is BlockEntityAkashicBookshelf && (world2.getBlockEntity(pos) as BlockEntityAkashicBookshelf).pattern == null }
		if (blankShelf != null)
			(world.getBlockEntity(blankShelf) as BlockEntityAkashicBookshelf).setNewMapping(key, iota)
	}
}