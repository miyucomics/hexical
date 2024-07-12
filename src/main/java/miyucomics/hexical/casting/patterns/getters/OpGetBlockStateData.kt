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
			0 -> {
				state.entries[Properties.WATERLOGGED] ?: listOf(NullIota())
				state.get(Properties.WATERLOGGED).asActionResult
			}
			1 -> {
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
			2 -> {
				if (state.entries[Properties.AGE_1] != null)
					(state.get(Properties.AGE_1)).asActionResult
				else if (state.entries[Properties.AGE_2] != null)
					(state.get(Properties.AGE_2).toDouble() / 2.0).asActionResult
				else if (state.entries[Properties.AGE_3] != null)
					(state.get(Properties.AGE_3).toDouble() / 3.0).asActionResult
				else if (state.entries[Properties.AGE_4] != null)
					(state.get(Properties.AGE_4).toDouble() / 4.0).asActionResult
				else if (state.entries[Properties.AGE_5] != null)
					(state.get(Properties.AGE_5).toDouble() / 5.0).asActionResult
				else if (state.entries[Properties.AGE_7] != null)
					(state.get(Properties.AGE_7).toDouble() / 7.0).asActionResult
				else if (state.entries[Properties.AGE_15] != null)
					(state.get(Properties.AGE_15).toDouble() / 15.0).asActionResult
				else if (state.entries[Properties.AGE_25] != null)
					(state.get(Properties.AGE_25).toDouble() / 25.0).asActionResult
				if (state.entries[Properties.LEVEL_3] != null)
					(state.get(Properties.LEVEL_3).toDouble() / 3).asActionResult
				else if (state.entries[Properties.LEVEL_8] != null)
					(state.get(Properties.LEVEL_8).toDouble() / 8).asActionResult
				else if (state.entries[Properties.HONEY_LEVEL] != null)
					(state.get(Properties.HONEY_LEVEL).toDouble() / 15.0).asActionResult
				if (state.entries[Properties.BITES] != null)
					(state.get(Properties.BITES).toDouble() / 6.0).asActionResult
				else
					listOf(NullIota())
			}
			3 -> {
				state.entries[Properties.LIT] ?: listOf(NullIota())
				state.get(Properties.LIT).asActionResult
			}
			4 -> {
				state.entries[Properties.OPEN] ?: listOf(NullIota())
				state.get(Properties.OPEN).asActionResult
			}
			5 -> {
				state.entries[Properties.ROTATION] ?: listOf(NullIota())
				state.get(Properties.ROTATION).asActionResult
			}
			6 -> {
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
			7 -> {
				if (state.entries[Properties.HAS_BOOK] != null)
					state.get(Properties.HAS_BOOK).asActionResult
				else if (state.entries[Properties.HAS_RECORD] != null)
					state.get(Properties.HAS_RECORD).asActionResult
				else if (state.entries[BlockAkashicBookshelf.HAS_BOOKS] != null)
					state.get(BlockAkashicBookshelf.HAS_BOOKS).asActionResult
				else
					listOf(NullIota())
			}
			else -> throw IllegalStateException()
		}
	}
}