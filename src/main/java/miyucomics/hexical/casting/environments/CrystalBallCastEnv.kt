package miyucomics.hexical.casting.environments

import at.petrak.hexcasting.api.addldata.ADMediaHolder
import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

class CrystalBallCastEnv(caster: ServerPlayerEntity, crystalBall: ItemStack, val permittedPatterns: Int, val previousContinuation: SpellContinuation? = null) : PlayerBasedCastEnv(caster, Hand.MAIN_HAND) {
	private var mediaHolder: ADMediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(crystalBall)!!

	override fun produceParticles(particles: ParticleSpray, pigment: FrozenPigment) {}
	override fun postExecution(result: CastResult) = super.postExecution(result.copy(cast = NullIota()))
	override fun getPigment(): FrozenPigment = IXplatAbstractions.INSTANCE.getPigment(this.caster)
	override fun getCastingHand(): Hand = Hand.MAIN_HAND

	override fun extractMediaEnvironment(costLeft: Long, simulate: Boolean): Long {
		if (caster.isCreative)
			return 0
		return costLeft - mediaHolder.withdrawMedia(costLeft.toInt().toLong(), simulate)
	}
}

class CrystalBallException(val image: CastingImage, val continuation: SpellContinuation) : Exception("You should never see this. Report to Miyu.")