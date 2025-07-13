package miyucomics.hexical.features.player.fields

import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.xplat.IXplatAbstractions
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.actions.evocation.OpSetEvocation
import miyucomics.hexical.misc.PlayerAnimations
import miyucomics.hexical.features.player.PlayerField
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

class EvocationField : PlayerField {
	var active: Boolean = false
	var duration: Int = -1
	var evocation: NbtCompound? = NbtCompound()

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

	override fun readNbt(compound: NbtCompound) {
		if (compound.contains("evocation"))
			this.evocation = compound.getCompound("evocation")
	}

	override fun writeNbt(compound: NbtCompound) {
		this.evocation?.let { compound.putCompound("evocation", it) }
	}

	override fun handleRespawn(new: PlayerEntity, old: PlayerEntity) {
		new.evocation = old.evocation
	}

	companion object {
		val START_EVOKE_CHANNEL: Identifier = HexicalMain.id("start_evoking")
		val END_EVOKING_CHANNEL: Identifier = HexicalMain.id("end_evoking")

		fun registerServerCallbacks() {
			ServerPlayNetworking.registerGlobalReceiver(START_EVOKE_CHANNEL) { server, player, _, _, _ ->
				if (!CastingUtils.isEnlightened(player))
					return@registerGlobalReceiver
				player.evocationActive = true
				player.evocationDuration = HexicalMain.EVOKE_DURATION
				player.world.playSound(null, player.x, player.y, player.z, HexicalSounds.EVOKING_MURMUR, SoundCategory.PLAYERS, 1f, 1f)
				for (otherPlayer in server.playerManager.playerList) {
					val packet = PacketByteBufs.create()
					packet.writeUuid(player.uuid)
					ServerPlayNetworking.send(otherPlayer, START_EVOKE_CHANNEL, packet)
				}
			}

			ServerPlayNetworking.registerGlobalReceiver(END_EVOKING_CHANNEL) { server, player, _, _, _ ->
				if (!CastingUtils.isEnlightened(player))
					return@registerGlobalReceiver
				player.evocationActive = false
				for (otherPlayer in server.playerManager.playerList) {
					val packet = PacketByteBufs.create()
					packet.writeUuid(player.uuid)
					ServerPlayNetworking.send(otherPlayer, END_EVOKING_CHANNEL, packet)
				}
			}
		}

		fun registerClientCallbacks() {
			ClientPlayNetworking.registerGlobalReceiver(START_EVOKE_CHANNEL) { client, _, packet, _ ->
				val uuid = packet.readUuid()
				val player = client.world!!.getPlayerByUuid(uuid) ?: return@registerGlobalReceiver
				val container = (player as PlayerAnimations).hexicalModAnimations()
				container.setAnimation(KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(HexicalMain.id("cast_loop"))!!))
				player.evocationActive = true
			}

			ClientPlayNetworking.registerGlobalReceiver(END_EVOKING_CHANNEL) { client, _, packet, _ ->
				val uuid = packet.readUuid()
				val player = client.world!!.getPlayerByUuid(uuid) ?: return@registerGlobalReceiver
				val container = (player as PlayerAnimations).hexicalModAnimations()
				container.setAnimation(KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(HexicalMain.id("cast_end"))!!))
				player.evocationActive = false
			}
		}
	}
}

var PlayerEntity.evocationActive: Boolean
	get() = this.getHexicalPlayerManager().get(EvocationField::class).active
	set(active) { this.getHexicalPlayerManager().get(EvocationField::class).active = active }
var PlayerEntity.evocationDuration: Int
	get() = this.getHexicalPlayerManager().get(EvocationField::class).duration
	set(duration) { this.getHexicalPlayerManager().get(EvocationField::class).duration = duration }
var PlayerEntity.evocation: NbtCompound?
	get() = this.getHexicalPlayerManager().get(EvocationField::class).evocation
	set(hex) { this.getHexicalPlayerManager().get(EvocationField::class).evocation = hex }