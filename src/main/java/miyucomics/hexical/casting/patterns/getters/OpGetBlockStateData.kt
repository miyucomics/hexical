package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getBlockPos
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicBookshelf
import net.minecraft.block.BlockState
import net.minecraft.block.CandleBlock
import net.minecraft.block.SeaPickleBlock
import net.minecraft.block.TurtleEggBlock
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction

class OpGetBlockStateData(private val process: (BlockState) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val position = args.getBlockPos(0, argc)
		ctx.assertVecInRange(position)
		val state = ctx.world.getBlockState(position)
		return process(state)
	}
}