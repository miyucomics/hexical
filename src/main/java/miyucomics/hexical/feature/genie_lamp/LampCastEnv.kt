package miyucomics.hexical.feature.genie_lamp

import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.misc.CastingUtils
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

class LampCastEnv(caster: ServerPlayerEntity, castingHand: Hand, val finale: Boolean, val stack: ItemStack) : PlayerBasedCastEnv(caster, castingHand) {
	override fun extractMediaEnvironment(cost: Long, simulate: Boolean): Long {
		if (caster.isCreative) return 0
		var costLeft = cost
		val currentMedia = CastingUtils.getMedia(stack.nbt!!)
		val mediaToDeduct = minOf(currentMedia, costLeft)
		costLeft -= mediaToDeduct
		if (!simulate) {
			if (currentMedia <= cost)
				stack.orCreateNbt.putLong("media", 0)
			else
				stack.orCreateNbt.putLong("media", currentMedia - cost)
		}
		return costLeft
	}

	override fun getCastingHand(): Hand = this.castingHand
	override fun getPigment(): FrozenPigment = IXplatAbstractions.INSTANCE.getPigment(this.caster)

	fun setInternalIota(iota: Iota) {
		stack.orCreateNbt.putCompound("storage", IotaType.serialize(iota))
	}
}