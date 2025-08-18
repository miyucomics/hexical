package miyucomics.hexical.features.confetti

import miyucomics.hexical.inits.HexicalParticles
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Vec3d
import kotlin.random.Random

object ClientConfettiReceiver : InitHook() {
	override fun init() {
		ClientPlayNetworking.registerGlobalReceiver(ConfettiHelper.CONFETTI_CHANNEL) { client, _, packet, _ ->
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
	}
}