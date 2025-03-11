package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.particles.ConfettiParticle
import miyucomics.hexical.particles.CubeParticle
import miyucomics.hexical.particles.CubeParticleEffect
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object HexicalParticles {
	lateinit var CONFETTI_PARTICLE: DefaultParticleType
	lateinit var CUBE_PARTICLE: ParticleType<CubeParticleEffect>

	@JvmStatic
	fun clientInit() {
		CONFETTI_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("confetti"), FabricParticleTypes.simple(true))
		CUBE_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("cube"), FabricParticleTypes.complex(CubeParticleEffect.Factory))

		ParticleFactoryRegistry.getInstance().register(CONFETTI_PARTICLE) { sprite -> ConfettiParticle.Factory(sprite) }
		ParticleFactoryRegistry.getInstance().register(CUBE_PARTICLE) { sprite -> CubeParticle.Factory(sprite) }
	}
}