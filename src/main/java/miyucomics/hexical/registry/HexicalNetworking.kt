package miyucomics.hexical.registry

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.environments.CharmedItemCastEnv
import miyucomics.hexical.client.ClientStorage
import miyucomics.hexical.client.PlayerAnimations
import miyucomics.hexical.client.ShaderRenderer
import miyucomics.hexical.data.*
import miyucomics.hexical.utils.CastingUtils
import miyucomics.hexical.utils.CharmedItemUtilities
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
	val CHARMED_ITEM_USE_CHANNEL: Identifier = HexicalMain.id("charmed_item")
	val PRESSED_KEY_CHANNEL: Identifier = HexicalMain.id("press_key")
	val RELEASED_KEY_CHANNEL: Identifier = HexicalMain.id("release_key")

	val SCROLL_CHANNEL: Identifier = HexicalMain.id("scroll")

	val CONFETTI_CHANNEL: Identifier = HexicalMain.id("confetti")

	val START_EVOKE_CHANNEL: Identifier = HexicalMain.id("start_evoking")
	val END_EVOKING_CHANNEL: Identifier = HexicalMain.id("end_evoking")

	val LEDGER_CHANNEL: Identifier = HexicalMain.id("ledger")
	val SHADER_CHANNEL: Identifier = HexicalMain.id("shader")

	@JvmStatic
	fun serverInit() {
		ServerPlayNetworking.registerGlobalReceiver(LEDGER_CHANNEL) { _, player, _, _, _ -> LedgerData.clearLedger(player) }

		ServerPlayNetworking.registerGlobalReceiver(CHARMED_ITEM_USE_CHANNEL) { server, player, _, buf, _ ->
			val charmedItem = CharmedItemUtilities.getCharmedItem(player) ?: return@registerGlobalReceiver
			val inputMethod = buf.readInt()
			server.execute {
				val vm = CastingVM(CastingImage().copy(stack = inputMethod.asActionResult), CharmedItemCastEnv(player, charmedItem.first, charmedItem.second))
				vm.queueExecuteAndWrapIotas(CharmedItemUtilities.getHex(charmedItem.second, player.serverWorld), player.serverWorld)
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
			if (key == "key.hexical.telepathy")
				KeybindData.scroll[player.uuid] = 0
		}
		ServerPlayNetworking.registerGlobalReceiver(RELEASED_KEY_CHANNEL) { _, player, _, buf, _ ->
			val key = buf.readString()
			KeybindData.active[player.uuid]!![key] = false
			KeybindData.duration[player.uuid]!![key] = 0
		}
		ServerPlayNetworking.registerGlobalReceiver(SCROLL_CHANNEL) { _, player, _, buf, _ ->
			KeybindData.scroll[player.uuid] = KeybindData.scroll.getOrDefault(player.uuid, 0) + buf.readInt()
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
		LesserSentinelState.registerClientReciever()

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