package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.particles.*
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object HexicalParticles {
	val CONFETTI_PARTICLE: DefaultParticleType = FabricParticleTypes.simple(true)
	val CUBE_PARTICLE: ParticleType<CubeParticleEffect> = FabricParticleTypes.complex(CubeParticleEffect.Factory)
	val SPARKLE_PARTICLE: ParticleType<SparkleParticleEffect> = FabricParticleTypes.complex(SparkleParticleEffect.Factory)

	@JvmStatic
	fun init() {
		Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("confetti"), CONFETTI_PARTICLE)
		Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("cube"), CUBE_PARTICLE)
		Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("sparkle"), SPARKLE_PARTICLE)
	}

	@JvmStatic
	fun clientInit() {
		ParticleFactoryRegistry.getInstance().register(CONFETTI_PARTICLE) { sprite -> ConfettiParticle.Factory(sprite) }
		ParticleFactoryRegistry.getInstance().register(CUBE_PARTICLE) { sprite -> CubeParticle.Factory(sprite) }
		ParticleFactoryRegistry.getInstance().register(SPARKLE_PARTICLE) { sprite -> SparkleParticle.Factory(sprite) }
	}
}