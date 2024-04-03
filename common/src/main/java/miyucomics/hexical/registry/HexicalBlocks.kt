package miyucomics.hexical.registry

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry
import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import miyucomics.hexical.blocks.MageBlock
import miyucomics.hexical.blocks.MageBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry

object HexicalBlocks {
	private val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(Hexical.MOD_ID, Registry.BLOCK_KEY)
	private val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(Hexical.MOD_ID, Registry.BLOCK_ENTITY_TYPE_KEY)

	val MAGE_BLOCK: MageBlock = MageBlock()
	val MAGE_BLOCK_ENTITY: BlockEntityType<MageBlockEntity> = BlockEntityType.Builder.create(::MageBlockEntity, MAGE_BLOCK).build(null)

	@JvmStatic
	fun init() {
		BLOCKS.register("mage_block") { MAGE_BLOCK }
		BLOCK_ENTITIES.register("mage_block") { MAGE_BLOCK_ENTITY }
		BLOCKS.register()
		BLOCK_ENTITIES.register()
	}
}