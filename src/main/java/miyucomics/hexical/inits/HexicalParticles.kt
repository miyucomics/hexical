package miyucomics.hexical.inits

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.particles.*
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object HexicalParticles {
	val CONFETTI_PARTICLE: DefaultParticleType = FabricParticleTypes.simple(true)
	val SPARKLE_PARTICLE: ParticleType<SparkleParticleEffect> = FabricParticleTypes.complex(SparkleParticleEffect.Factory)

	fun init() {
		Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("confetti"), CONFETTI_PARTICLE)
		Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("sparkle"), SPARKLE_PARTICLE)
	}

	fun clientInit() {
		ParticleFactoryRegistry.getInstance().register(CONFETTI_PARTICLE) { sprite -> ConfettiParticle.Factory(sprite) }
		ParticleFactoryRegistry.getInstance().register(SPARKLE_PARTICLE) { sprite -> SparkleParticle.Factory(sprite) }
	}
}