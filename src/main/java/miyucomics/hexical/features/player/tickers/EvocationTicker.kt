package miyucomics.hexical.features.player.tickers

import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.xplat.IXplatAbstractions
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.actions.evocation.OpSetEvocation
import miyucomics.hexical.misc.PlayerAnimations
import miyucomics.hexical.features.player.PlayerField
import miyucomics.hexical.features.player.PlayerTicker
import miyucomics.hexical.features.player.getHexicalPlayerManager
import miyucomics.hexical.inits.HexicalSounds
import miyucomics.hexical.misc.CastingUtils
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.MathHelper

class EvocationTicker : PlayerTicker {
	override fun tick(player: PlayerEntity) {
		if (player.world.isClient && this.active) {
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
		if (this.active)
			this.duration -= 1
		if (this.active && this.duration == 0 && CastingUtils.isEnlightened(player as ServerPlayerEntity))
			OpSetEvocation.evoke(player)
	}
}