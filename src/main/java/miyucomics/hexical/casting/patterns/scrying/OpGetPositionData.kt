package miyucomics.hexical.casting.patterns.scrying

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

class OpGetPositionData(private val process: (ServerWorld, BlockPos) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		return process(env.world, position)
	}
}