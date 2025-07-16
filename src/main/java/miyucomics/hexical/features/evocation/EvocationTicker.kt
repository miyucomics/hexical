package miyucomics.hexical.features.evocation

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.environments.EvocationCastEnv
import miyucomics.hexical.features.player.types.PlayerTicker
import miyucomics.hexical.inits.HexicalAdvancements
import miyucomics.hexical.inits.HexicalSounds
import miyucomics.hexical.misc.CastingUtils
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.MathHelper

class EvocationTicker : PlayerTicker {
	override fun tick(player: PlayerEntity) {
		if (player.world.isClient && player.evocationActive) {
			val rot = player.bodyYaw * (Math.PI.toFloat() / 180) + MathHelper.cos(player.age.toFloat() * 0.6662f) * 0.25f
			val cos = MathHelper.cos(rot)
			val sin = MathHelper.sin(rot)
			val color = IXplatAbstractions.INSTANCE.getPigment(player).colorProvider.getColor((player.world.time * 10).toFloat(), player.pos)
			val r = ColorHelper.Argb.getRed(color) / 255f
			val g = ColorHelper.Argb.getGreen(color) / 255f
			val b = ColorHelper.Argb.getBlue(color) / 255f
			player.world.addParticle(ParticleTypes.ENTITY_EFFECT, player.x + cos.toDouble() * 0.6, player.y + 1.8, player.z + sin.toDouble() * 0.6, r.toDouble(), g.toDouble(), b.toDouble())
			player.world.addParticle(ParticleTypes.ENTITY_EFFECT, player.x - cos.toDouble() * 0.6, player.y + 1.8, player.z - sin.toDouble() * 0.6, r.toDouble(), g.toDouble(), b.toDouble())
		}

		if (player.world.isClient)
			return

		if (player.evocationActive)
			player.evocationDuration -= 1

		if (player.evocationActive && player.evocationDuration == 0 && CastingUtils.isEnlightened(player as ServerPlayerEntity)) {
			player.incrementStat(HexicalAdvancements.EVOCATION_STATISTIC)
			player.evocationDuration = HexicalMain.Companion.EVOKE_DURATION
			val hex = IotaType.deserialize(player.evocation, player.world as ServerWorld)
			if (hex is ListIota) {
				val hand = if(!player.getStackInHand(Hand.MAIN_HAND).isEmpty && player.getStackInHand(Hand.OFF_HAND).isEmpty){ Hand.OFF_HAND } else { Hand.MAIN_HAND }
				val vm = CastingVM(CastingImage(), EvocationCastEnv(player, hand))
				vm.queueExecuteAndWrapIotas(hex.list.toList(), player.serverWorld)
				player.world.playSound(null, player.x, player.y, player.z, HexicalSounds.EVOKING_CAST, SoundCategory.PLAYERS, 1f, 1f)
			}
		}
	}
}