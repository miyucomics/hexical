package miyucomics.hexical.casting.environments

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

open class LampCastEnv(caster: ServerPlayerEntity, castingHand: Hand, val stack: ItemStack, private val finale: Boolean) : PackagedItemCastEnv(caster, castingHand) {
	override fun produceParticles(particles: ParticleSpray, pigment: FrozenPigment) {}

	override fun getCastingHand(): Hand = this.castingHand
	override fun getPigment(): FrozenPigment = IXplatAbstractions.INSTANCE.getPigment(this.caster)

	fun getFinale() = finale
}