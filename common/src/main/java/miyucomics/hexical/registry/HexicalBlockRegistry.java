package miyucomics.hexical.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import miyucomics.hexical.Hexical;
import miyucomics.hexical.blocks.ConjuredBouncyBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class HexicalBlockRegistry {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Hexical.MOD_ID, Registry.BLOCK_KEY);

	public static void init() {
		BLOCKS.register();
	}

	public static final RegistrySupplier<Block> CONJURED_BOUNCY_BLOCK = BLOCKS.register("conjured_bouncy_block", () -> new ConjuredBouncyBlock(AbstractBlock.Settings.of(Material.ORGANIC_PRODUCT).breakInstantly().sounds(BlockSoundGroup.AMETHYST_CLUSTER)));
}