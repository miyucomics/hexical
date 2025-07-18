package miyucomics.hexical.features.charms

import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

class CharmCastEnv(caster: ServerPlayerEntity, castingHand: Hand, val stack: ItemStack) : PlayerBasedCastEnv(caster, castingHand) {
	override fun extractMediaEnvironment(cost: Long, simulate: Boolean): Long {
		if (caster.isCreative) return 0
		var costLeft = cost
		val currentMedia = CharmUtilities.getMedia(stack)
		val mediaToDeduct = minOf(currentMedia, costLeft)
		costLeft -= mediaToDeduct
		if (!simulate)
			CharmUtilities.deductMedia(stack, mediaToDeduct)
		return costLeft
	}

	override fun getCastingHand(): Hand = this.castingHand
	override fun getPigment(): FrozenPigment = IXplatAbstractions.INSTANCE.getPigment(this.caster)

	fun getInternalStorage() = CharmUtilities.getInternalStorage(this.stack, this.world)
	fun setInternalStorage(iota: Iota) = CharmUtilities.setInternalStorage(this.stack, iota)
}