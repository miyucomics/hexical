package miyucomics.hexical.inits

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.environments.CharmedItemCastEnv
import miyucomics.hexical.features.charms.CharmedItemUtilities
import miyucomics.hexical.features.media_log.MediaLogDisplayer
import miyucomics.hexical.features.player.fields.MediaLogField
import miyucomics.hexical.misc.ShaderRenderer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import kotlin.random.Random

object HexicalHooks {
	@JvmField
	val CHARMED_ITEM_USE_CHANNEL: Identifier = HexicalMain.id("charmed_item")
	val CONFETTI_CHANNEL: Identifier = HexicalMain.id("confetti")
	val SHADER_CHANNEL: Identifier = HexicalMain.id("shader")

	fun serverInit() {
		ServerPlayNetworking.registerGlobalReceiver(CHARMED_ITEM_USE_CHANNEL) { server, player, _, buf, _ ->
			val inputMethod = buf.readInt()
			val hand = enumValues<Hand>()[buf.readInt()]
			val stack = player.getStackInHand(hand)
			server.execute {
				val vm = CastingVM(CastingImage().copy(stack = inputMethod.asActionResult), CharmedItemCastEnv(player, hand, stack))
				vm.queueExecuteAndWrapIotas(CharmedItemUtilities.getHex(stack, player.serverWorld), player.serverWorld)
			}
		}
	}

	fun clientInit() {
		MediaLogField.registerClientCallbacks()
		MediaLogDisplayer.registerClientCallbacks()

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
	}
}