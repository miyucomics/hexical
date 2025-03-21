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
	val CONFETTI_PARTICLE: DefaultParticleType = Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("confetti"), FabricParticleTypes.simple(true))
	val CUBE_PARTICLE: ParticleType<CubeParticleEffect> = Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("cube"), FabricParticleTypes.complex(CubeParticleEffect.Factory))
	val SPARKLE_PARTICLE: ParticleType<SparkleParticleEffect> = Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("sparkle"), FabricParticleTypes.complex(SparkleParticleEffect.Factory))

	@JvmStatic
	fun clientInit() {
		ParticleFactoryRegistry.getInstance().register(CONFETTI_PARTICLE) { sprite -> ConfettiParticle.Factory(sprite) }
		ParticleFactoryRegistry.getInstance().register(CUBE_PARTICLE) { sprite -> CubeParticle.Factory(sprite) }
		ParticleFactoryRegistry.getInstance().register(SPARKLE_PARTICLE) { sprite -> SparkleParticle.Factory(sprite) }
	}
}