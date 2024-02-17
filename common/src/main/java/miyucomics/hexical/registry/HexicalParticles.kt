package miyucomics.hexical.registry

import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import net.minecraft.particle.ParticleType
import net.minecraft.util.registry.Registry

object HexicalParticles {
	private val PARTICLES: DeferredRegister<ParticleType<*>> = DeferredRegister.create(Hexical.MOD_ID, Registry.PARTICLE_TYPE_KEY)

	@JvmStatic
	fun init() {
		PARTICLES.register()
	}
}