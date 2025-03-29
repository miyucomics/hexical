package miyucomics.hexical.casting.patterns.raycast

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexical.utils.DDAUtils
import miyucomics.hexposition.iotas.getIdentifier
import net.minecraft.registry.Registries
import net.minecraft.util.math.Vec3d

class OpPiercingRaycast : ConstMediaAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val start = args.getVec3(0, argc)
		env.assertVecInRange(start)
		val direction = args.getVec3(1, argc).normalize()
		if (direction == Vec3d.ZERO)
			return listOf(NullIota())
		val id = args.getIdentifier(2, argc)
		if (!Registries.BLOCK.containsId(id))
			throw MishapInvalidIota.of(args[2], 0, "block_id")
		val desired = Registries.BLOCK.get(id)
		return DDAUtils.raycastBlock(start, direction, { pos -> env.world.getBlockState(pos).isOf(desired) }, { pos -> !env.isVecInRange(pos.toCenterPos()) })
	}
}