package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.particles.ConfettiParticle
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.DefaultParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object HexicalParticles {
	lateinit var CONFETTI_PARTICLE: DefaultParticleType

	@JvmStatic
	fun clientInit() {
		CONFETTI_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, HexicalMain.id("confetti"), FabricParticleTypes.simple(true))
		ParticleFactoryRegistry.getInstance().register(CONFETTI_PARTICLE) { sprite -> ConfettiParticle.Factory(sprite) }
	}
}