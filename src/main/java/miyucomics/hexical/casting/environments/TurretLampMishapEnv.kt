package miyucomics.hexical.casting.environments

import at.petrak.hexcasting.api.casting.eval.MishapEnvironment
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d

class TurretLampMishapEnv(world: ServerWorld) : MishapEnvironment(world, null) {
	override fun yeetHeldItemsTowards(targetPos: Vec3d?) {}
	override fun dropHeldItems() {}
	override fun drown() {}
	override fun damage(healthProportion: Float) {}
	override fun removeXp(amount: Int) {}
	override fun blind(ticks: Int) {}
}