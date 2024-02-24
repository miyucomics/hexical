package miyucomics.hexical.registry

import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import miyucomics.hexical.blocks.AdvancedConjuredBlock
import miyucomics.hexical.blocks.AdvancedConjuredBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry

object HexicalBlocks {
	private val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(Hexical.MOD_ID, Registry.BLOCK_KEY)
	private val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(Hexical.MOD_ID, Registry.BLOCK_ENTITY_TYPE_KEY)

	val ADVANCED_CONJURED_BLOCK: AdvancedConjuredBlock = AdvancedConjuredBlock()
	val ADVANCED_CONJURED_BLOCK_ENTITY: BlockEntityType<AdvancedConjuredBlockEntity> = BlockEntityType.Builder.create(::AdvancedConjuredBlockEntity, ADVANCED_CONJURED_BLOCK).build(null)

	@JvmStatic
	fun init() {
		BLOCKS.register("advanced_conjured_block") { ADVANCED_CONJURED_BLOCK }
		BLOCK_ENTITIES.register("advanced_conjured_block") { ADVANCED_CONJURED_BLOCK_ENTITY }
		BLOCKS.register()
		BLOCK_ENTITIES.register()
	}
}