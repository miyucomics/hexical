package miyucomics.hexical.registry

import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import miyucomics.hexical.blocks.ConjuredBouncyBlock
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.registry.Registry

object HexicalBlocks {
	private val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(Hexical.MOD_ID, Registry.BLOCK_KEY)
	val CONJURED_BOUNCY_BLOCK: Block = ConjuredBouncyBlock(AbstractBlock.Settings.of(Material.ORGANIC_PRODUCT).breakInstantly().sounds(BlockSoundGroup.AMETHYST_CLUSTER))

	@JvmStatic
	fun init() {
		BLOCKS.register("conjured_bouncy_block") { CONJURED_BOUNCY_BLOCK }
		BLOCKS.register()
	}
}