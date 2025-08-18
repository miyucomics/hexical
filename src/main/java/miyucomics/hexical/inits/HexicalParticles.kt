package miyucomics.hexical.inits

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.sparkle.SparkleParticleEffect
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
}