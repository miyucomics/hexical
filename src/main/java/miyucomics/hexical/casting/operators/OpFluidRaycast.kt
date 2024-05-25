package miyucomics.hexical.casting.operators

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext

object OpFluidRaycast : ConstMediaAction {
	override val argc = 2
	override val mediaCost = MediaConstants.DUST_UNIT / 100
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val origin = args.getVec3(0, argc)
		val look = args.getVec3(1, argc)
		ctx.assertVecInRange(origin)
		val hit = ctx.world.raycast(RaycastContext(origin, Action.raycastEnd(origin, look), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, ctx.caster))
		return if (hit.type == HitResult.Type.BLOCK && ctx.isVecInRange(Vec3d.ofCenter(hit.blockPos))) {
			listOf(Vec3Iota(Vec3d.ofCenter(hit.blockPos)))
		} else {
			listOf(NullIota())
		}
	}
}