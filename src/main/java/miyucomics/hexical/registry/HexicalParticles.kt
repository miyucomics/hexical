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
	lateinit var CONFETTI_PARTICLE: DefaultParticleType
	lateinit var CUBE_PARTICLE: ParticleType<CubeParticleEffect>
	lateinit var SPARKLE_PARTICLE: ParticleType<SparkleParticleEffect>

	@JvmStatic
	fun clientInit() {
		CONFETTI_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("confetti"), FabricParticleTypes.simple(true))
		CUBE_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("cube"), FabricParticleTypes.complex(CubeParticleEffect.Factory))
		SPARKLE_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("sparkle"), FabricParticleTypes.complex(SparkleParticleEffect.Factory))

		ParticleFactoryRegistry.getInstance().register(CONFETTI_PARTICLE) { sprite -> ConfettiParticle.Factory(sprite) }
		ParticleFactoryRegistry.getInstance().register(CUBE_PARTICLE) { sprite -> CubeParticle.Factory(sprite) }
		ParticleFactoryRegistry.getInstance().register(SPARKLE_PARTICLE) { sprite -> SparkleParticle.Factory(sprite) }
	}
}