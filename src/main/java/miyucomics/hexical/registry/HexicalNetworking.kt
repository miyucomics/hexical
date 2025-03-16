package miyucomics.hexical.registry

import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.client.ClientStorage
import miyucomics.hexical.client.PlayerAnimations
import miyucomics.hexical.client.ShaderRenderer
import miyucomics.hexical.data.EvokeState
import miyucomics.hexical.data.KeybindData
import miyucomics.hexical.data.LedgerData
import miyucomics.hexical.data.LedgerInstance
import miyucomics.hexical.items.TchotchkeItem
import miyucomics.hexical.items.getTchotchke
import miyucomics.hexical.utils.CastingUtils
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import kotlin.random.Random

object HexicalNetworking {
	@JvmField
	val TCHOTCHKE_CHANNEL: Identifier = HexicalMain.id("tchotchke")
	val PRESSED_KEY_CHANNEL: Identifier = HexicalMain.id("press_key")
	val RELEASED_KEY_CHANNEL: Identifier = HexicalMain.id("release_key")

	val CONFETTI_CHANNEL: Identifier = HexicalMain.id("confetti")

	val START_EVOKE_CHANNEL: Identifier = HexicalMain.id("start_evoking")
	val END_EVOKING_CHANNEL: Identifier = HexicalMain.id("end_evoking")

	val LEDGER_CHANNEL: Identifier = HexicalMain.id("ledger")
	val SHADER_CHANNEL: Identifier = HexicalMain.id("shader")

	@JvmStatic
	fun serverInit() {
		ServerPlayNetworking.registerGlobalReceiver(LEDGER_CHANNEL) { _, player, _, _, _ -> LedgerData.clearLedger(player) }

		ServerPlayNetworking.registerGlobalReceiver(TCHOTCHKE_CHANNEL) { server, player, _, buf, _ ->
			val hand = getTchotchke(player) ?: return@registerGlobalReceiver
			val inputs = mutableListOf<Iota>()
			val staffRank = buf.readInt()
			for (i in 0 until staffRank)
				inputs.add(BooleanIota(buf.readBoolean()))
			server.execute {
				TchotchkeItem.cast(player, hand, player.getStackInHand(hand), ListIota(inputs))
			}
		}

		ServerPlayNetworking.registerGlobalReceiver(PRESSED_KEY_CHANNEL) { _, player, _, buf, _ ->
			if (!KeybindData.active.containsKey(player.uuid)) {
				KeybindData.active[player.uuid] = HashMap()
				KeybindData.duration[player.uuid] = HashMap()
			}
			val key = buf.readString()
			KeybindData.active[player.uuid]!![key] = true
			KeybindData.duration[player.uuid]!![key] = 0
		}
		ServerPlayNetworking.registerGlobalReceiver(RELEASED_KEY_CHANNEL) { _, player, _, buf, _ ->
			val key = buf.readString()
			KeybindData.active[player.uuid]!![key] = false
			KeybindData.duration[player.uuid]!![key] = 0
		}

		ServerPlayNetworking.registerGlobalReceiver(START_EVOKE_CHANNEL) { server, player, _, _, _ ->
			if (!CastingUtils.isEnlightened(player))
				return@registerGlobalReceiver
			EvokeState.active[player.uuid] = true
			EvokeState.duration[player.uuid] = HexicalMain.EVOKE_DURATION
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
			EvokeState.active[player.uuid] = false
			for (otherPlayer in server.playerManager.playerList) {
				val packet = PacketByteBufs.create()
				packet.writeUuid(player.uuid)
				ServerPlayNetworking.send(otherPlayer, END_EVOKING_CHANNEL, packet)
			}
		}
	}

	@JvmStatic
	fun clientInit() {
		ClientPlayNetworking.registerGlobalReceiver(LEDGER_CHANNEL) { _, _, packet, _ -> ClientStorage.ledger = LedgerInstance.createFromNbt(packet.readNbt()!!) }

		ClientPlayNetworking.registerGlobalReceiver(CONFETTI_CHANNEL) { client, _, packet, _ ->
			val random = Random(packet.readLong())
			val pos = Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble())
			val dir = Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble())
			val speed = packet.readDouble()
			client.execute {
				client.world!!.playSound(pos.x, pos.y, pos.z, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 1f, 1f, true)
				for (i in 0..99) {
					val alteredVelocity = if (dir == Vec3d.ZERO) {
						Vec3d.fromPolar(random.nextFloat() * 180f - 90f, random.nextFloat() * 360f).multiply(speed)
					} else {
						dir.add(
							(random.nextDouble() * 2 - 1) / 5,
							(random.nextDouble() * 2 - 1) / 5,
							(random.nextDouble() * 2 - 1) / 5
						).multiply((random.nextFloat() * 0.25 + 0.75) * speed)
					}
					client.world!!.addParticle(HexicalParticles.CONFETTI_PARTICLE, pos.x, pos.y, pos.z, alteredVelocity.x, alteredVelocity.y, alteredVelocity.z)
				}
			}
		}

		ClientPlayNetworking.registerGlobalReceiver(SHADER_CHANNEL) { client, _, packet, _ ->
			val shader = packet.readString()
			if (shader == "null")
				client.execute { ShaderRenderer.setEffect(null) }
			else
				client.execute {  ShaderRenderer.setEffect(Identifier(shader)) }
		}
		ClientPlayNetworking.registerGlobalReceiver(START_EVOKE_CHANNEL) { client, _, packet, _ ->
			val uuid = packet.readUuid()
			val player = client.world!!.getPlayerByUuid(uuid) ?: return@registerGlobalReceiver
			val container = (player as PlayerAnimations).hexicalModAnimations()
			container.setAnimation(KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(HexicalMain.id("cast_loop"))!!))
			EvokeState.active[uuid] = true
		}
		ClientPlayNetworking.registerGlobalReceiver(END_EVOKING_CHANNEL) { client, _, packet, _ ->
			val uuid = packet.readUuid()
			val player = client.world!!.getPlayerByUuid(uuid) ?: return@registerGlobalReceiver
			val container = (player as PlayerAnimations).hexicalModAnimations()
			container.setAnimation(KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(HexicalMain.id("cast_end"))!!))
			EvokeState.active[uuid] = false
		}
	}
}