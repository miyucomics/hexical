package miyucomics.hexical.casting.env

import at.petrak.hexcasting.api.addldata.ADMediaHolder
import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.function.Predicate

class TurretLampCastEnv(val lamp: ItemEntity, world: ServerWorld) : CastingEnvironment(world) {
	private var mediaHolder: ADMediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(lamp.stack)!!

	override fun getCastingEntity() = null
	override fun printMessage(message: Text?) {}
	override fun getMishapEnvironment() = TurretLampMishapEnv(world)
	override fun postExecution(result: CastResult) = super.postExecution(result.copy(cast = NullIota()))
	override fun mishapSprayPos(): Vec3d = lamp.pos
	override fun getCastingHand(): Hand = Hand.MAIN_HAND
	override fun getPrimaryStacks() = emptyList<HeldItemInfo>()
	override fun getUsableStacks(mode: StackDiscoveryMode?) = emptyList<ItemStack>()
	override fun replaceItem(stackOk: Predicate<ItemStack>?, replaceWith: ItemStack?, hand: Hand?) = false
	override fun hasEditPermissionsAtEnvironment(pos: BlockPos?) = true
	override fun isVecInRangeEnvironment(vec: Vec3d) = vec.distanceTo(lamp.pos) < 16
	override fun getPigment(): FrozenPigment = FrozenPigment.DEFAULT.get()
	override fun setPigment(pigment: FrozenPigment?): FrozenPigment = FrozenPigment.DEFAULT.get()

	override fun produceParticles(particles: ParticleSpray, pigment: FrozenPigment) {
		particles.sprayParticles(this.world, pigment)
	}

	override fun extractMediaEnvironment(costLeft: Long, simulate: Boolean): Long {
		return costLeft - mediaHolder.withdrawMedia(costLeft.toInt().toLong(), simulate)
	}
}