package miyucomics.hexical.casting.env

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

class HandLampCastEnv(caster: ServerPlayerEntity, castingHand: Hand, finale: Boolean, private val stack: ItemStack) : LampCastEnv(caster, castingHand, finale) {
	override fun produceParticles(particles: ParticleSpray, pigment: FrozenPigment) {}

	override fun getCastingHand(): Hand = this.castingHand
	override fun getPigment(): FrozenPigment = IXplatAbstractions.INSTANCE.getPigment(this.caster)

	fun setInternalIota(iota: Iota) {
		stack.orCreateNbt.putCompound("storage", IotaType.serialize(iota))
	}
}