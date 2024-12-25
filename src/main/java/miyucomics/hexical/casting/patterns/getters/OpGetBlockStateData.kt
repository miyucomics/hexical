package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.block.BlockState

class OpGetBlockStateData(private val process: (BlockState) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		val state = env.world.getBlockState(position)
		return process(state)
	}
}