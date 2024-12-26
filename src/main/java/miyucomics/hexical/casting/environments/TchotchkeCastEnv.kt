package miyucomics.hexical.casting.environments

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

class TchotchkeCastEnv(caster: ServerPlayerEntity, castingHand: Hand, val stack: ItemStack) : PackagedItemCastEnv(caster, castingHand) {
	override fun produceParticles(particles: ParticleSpray, pigment: FrozenPigment) {}

	override fun getCastingHand(): Hand = this.castingHand
	override fun getPigment(): FrozenPigment = IXplatAbstractions.INSTANCE.getPigment(this.caster)

	fun getInternalStorage(): Iota {
		val nbt = this.stack.orCreateNbt
		if (nbt.contains("storage"))
			return IotaType.deserialize(nbt.getCompound("storage"), caster.serverWorld)
		return NullIota()
	}
	fun setInternalStorage(iota: Iota) =
		this.stack.orCreateNbt.putCompound("storage", IotaType.serialize(iota))
}