package miyucomics.hexical.casting.environments

import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.utils.TweakedItemsUtils
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

class TweakedItemCastEnv(caster: ServerPlayerEntity, castingHand: Hand, val stack: ItemStack) : PlayerBasedCastEnv(caster, castingHand) {
	override fun extractMediaEnvironment(cost: Long, simulate: Boolean): Long {
		if (caster.isCreative) return 0
		var costLeft = cost
		val currentMedia = TweakedItemsUtils.getMedia(stack)
		costLeft -= currentMedia
		if (simulate) {
			val mediaLeft = currentMedia - cost
			TweakedItemsUtils.deductMedia(stack, mediaLeft)
		}
		println(costLeft)
		return costLeft
	}

	override fun getCastingHand(): Hand = this.castingHand
	override fun getPigment(): FrozenPigment = IXplatAbstractions.INSTANCE.getPigment(this.caster)

	fun getInternalStorage(): Iota {
		val nbt = this.stack.orCreateNbt
		if (nbt.contains("tweaked_storage"))
			return IotaType.deserialize(nbt.getCompound("tweaked_storage"), caster.serverWorld)
		return NullIota()
	}

	fun setInternalStorage(iota: Iota) =
		this.stack.orCreateNbt.putCompound("tweaked_storage", IotaType.serialize(iota))
}