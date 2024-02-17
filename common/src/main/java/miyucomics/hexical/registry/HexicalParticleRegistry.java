package miyucomics.hexical.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import miyucomics.hexical.Hexical;
import miyucomics.hexical.blocks.ConjuredBouncyBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class HexicalParticleRegistry {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Hexical.MOD_ID, Registry.PARTICLE_TYPE_KEY);

	public static void init() {
		PARTICLES.register();
	}
}