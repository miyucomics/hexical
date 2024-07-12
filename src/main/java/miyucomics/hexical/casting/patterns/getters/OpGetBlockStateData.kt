package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getBlockPos
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicBookshelf
import net.minecraft.block.*
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction

class OpGetBlockStateData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val position = args.getBlockPos(0, argc)
		ctx.assertVecInRange(position)
		val state = ctx.world.getBlockState(position)
		return when (mode) {
			0 -> { // Plumber's Purification - returns whether a block is water-logged
				state.entries[Properties.WATERLOGGED] ?: listOf(NullIota())
				state.get(Properties.WATERLOGGED).asActionResult
			}
			1 -> { // Rotation Purification - returns a unit vector of how a block is facing
				if (state.entries[Properties.FACING] != null)
					state.get(Properties.FACING).unitVector.asActionResult
				else if (state.entries[Properties.HORIZONTAL_FACING] != null)
					state.get(Properties.HORIZONTAL_FACING).unitVector.asActionResult
				else if (state.entries[Properties.VERTICAL_DIRECTION] != null)
					state.get(Properties.VERTICAL_DIRECTION).unitVector.asActionResult
				else if (state.entries[Properties.AXIS] != null)
					Direction.from(state.get(Properties.AXIS), Direction.AxisDirection.POSITIVE).unitVector.asActionResult
				else if (state.entries[Properties.HORIZONTAL_AXIS] != null)
					Direction.from(state.get(Properties.HORIZONTAL_AXIS), Direction.AxisDirection.POSITIVE).unitVector.asActionResult
				else if (state.entries[Properties.HOPPER_FACING] != null)
					state.get(Properties.HOPPER_FACING).unitVector.asActionResult
				else
					listOf(NullIota())
			}
			2 -> { // Bunching Purification - returns how many things are in a bunch, used for candles, sea pickles, and turtle eggs
				when (state.block) {
					is CandleBlock -> {
						state.entries[Properties.CANDLES] ?: listOf(NullIota())
						state.get(Properties.CANDLES).asActionResult
					}
					is SeaPickleBlock -> {
						state.entries[Properties.PICKLES] ?: listOf(NullIota())
						state.get(Properties.PICKLES).asActionResult
					}
					is TurtleEggBlock -> {
						state.entries[Properties.EGGS] ?: listOf(NullIota())
						state.get(Properties.EGGS).asActionResult
					}
					else -> listOf(NullIota())
				}
			}
			3 -> { // Farmer's Purification - returns various age-related information regarding crops and the like
				if (state.entries[Properties.AGE_1] != null)
					state.get(Properties.AGE_1).asActionResult
				else if (state.entries[Properties.AGE_2] != null)
					state.get(Properties.AGE_2).asActionResult
				else if (state.entries[Properties.AGE_3] != null)
					state.get(Properties.AGE_3).asActionResult
				else if (state.entries[Properties.AGE_4] != null)
					state.get(Properties.AGE_4).asActionResult
				else if (state.entries[Properties.AGE_5] != null)
					state.get(Properties.AGE_5).asActionResult
				else if (state.entries[Properties.AGE_7] != null)
					state.get(Properties.AGE_7).asActionResult
				else if (state.entries[Properties.AGE_15] != null)
					state.get(Properties.AGE_15).asActionResult
				else if (state.entries[Properties.AGE_25] != null)
					state.get(Properties.AGE_25).asActionResult
				else
					listOf(NullIota())
			}
			4 -> { // Glow Purification - returns whether a block is in a "lit" state
				state.entries[Properties.LIT] ?: listOf(NullIota())
				state.get(Properties.LIT).asActionResult
			}
			5 -> { // Lock Purification - returns whether a block is in an "open" state
				state.entries[Properties.OPEN] ?: listOf(NullIota())
				state.get(Properties.OPEN).asActionResult
			}
			6 -> { // Book Purification - returns whether a jukebox, lectern, or an akashic bookshelf has books/records
				if (state.entries[Properties.HAS_BOOK] != null)
					state.get(Properties.HAS_BOOK).asActionResult
				else if (state.entries[Properties.HAS_RECORD] != null)
					state.get(Properties.HAS_RECORD).asActionResult
				else if (state.entries[BlockAkashicBookshelf.HAS_BOOKS] != null)
					state.get(BlockAkashicBookshelf.HAS_BOOKS).asActionResult
				else
					listOf(NullIota())
			}
			7 -> { // Tank Purification - for beehives, cauldrons,
				if (state.entries[Properties.LEVEL_3] != null)
					state.get(Properties.LEVEL_3).asActionResult
				else if (state.entries[Properties.LEVEL_8] != null)
					state.get(Properties.LEVEL_8).asActionResult
				else if (state.entries[Properties.HONEY_LEVEL] != null)
					state.get(Properties.HONEY_LEVEL).asActionResult
				else
					listOf(NullIota())
			}
			8 -> { // Cake Purification - for cakes
				state.entries[Properties.BITES] ?: listOf(NullIota())
				state.get(Properties.BITES).asActionResult
			}
			9 -> { // Rotation Purification - for signs, skulls, and banners: returns by what increment they were rotated
				state.entries[Properties.ROTATION] ?: listOf(NullIota())
				state.get(Properties.ROTATION).asActionResult
			}
			else -> throw IllegalStateException()
		}
	}
}