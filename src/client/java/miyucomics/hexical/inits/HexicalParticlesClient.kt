package miyucomics.hexical.inits

import miyucomics.hexical.features.confetti.ConfettiParticle
import miyucomics.hexical.features.sparkle.SparkleParticle
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry

object HexicalParticlesClient {
	fun clientInit() {
		ParticleFactoryRegistry.getInstance().register(HexicalParticles.CONFETTI_PARTICLE) { sprite -> ConfettiParticle.Factory(sprite) }
		ParticleFactoryRegistry.getInstance().register(HexicalParticles.SPARKLE_PARTICLE) { sprite -> SparkleParticle.Factory(sprite) }
	}
}