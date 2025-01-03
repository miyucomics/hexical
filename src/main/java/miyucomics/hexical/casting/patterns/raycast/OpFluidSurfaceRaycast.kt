package miyucomics.hexical.casting.patterns.raycast

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexical.utils.DDAUtils
import net.minecraft.block.FluidBlock
import net.minecraft.util.math.Vec3d

class OpFluidSurfaceRaycast : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val start = args.getVec3(0, argc)
		env.assertVecInRange(start)
		val direction = args.getVec3(1, argc).normalize()
		if (direction == Vec3d.ZERO)
			return listOf(NullIota())
		return DDAUtils.raycastNormal(start, direction, { pos -> env.world.getBlockState(pos).block is FluidBlock }, { pos -> !env.isVecInRange(pos.toCenterPos()) })
	}
}