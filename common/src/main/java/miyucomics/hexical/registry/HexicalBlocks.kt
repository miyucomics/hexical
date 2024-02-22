package miyucomics.hexical.registry

import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import miyucomics.hexical.blocks.*
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry

object HexicalBlocks {
	private val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(Hexical.MOD_ID, Registry.BLOCK_KEY)
	private val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(Hexical.MOD_ID, Registry.BLOCK_ENTITY_TYPE_KEY)

	val CONJURED_BOUNCY_BLOCK: Block = ConjuredBouncyBlock()
	val CONJURED_SLIPPERY_BLOCK: Block = ConjuredSlipperyBlock()
	val CONJURED_VOLATILE_BLOCK: Block = ConjuredVolatileBlock()
	val CONJURED_BOUNCY_BLOCK_ENTITY: BlockEntityType<ConjuredBouncyBlockEntity> = BlockEntityType.Builder.create(ConjuredBouncyBlockEntity::init, CONJURED_BOUNCY_BLOCK).build(null)
	val CONJURED_SLIPPERY_BLOCK_ENTITY: BlockEntityType<ConjuredSlipperyBlockEntity> = BlockEntityType.Builder.create(ConjuredSlipperyBlockEntity::init, CONJURED_SLIPPERY_BLOCK).build(null)
	val CONJURED_VOLATILE_BLOCK_ENTITY: BlockEntityType<ConjuredVolatileBlockEntity> = BlockEntityType.Builder.create(ConjuredVolatileBlockEntity::init, CONJURED_VOLATILE_BLOCK).build(null)

	@JvmStatic
	fun init() {
		BLOCKS.register("conjured_bouncy_block") { CONJURED_BOUNCY_BLOCK }
		BLOCKS.register("conjured_slippery_block") { CONJURED_SLIPPERY_BLOCK }
		BLOCKS.register("conjured_volatile_block") { CONJURED_VOLATILE_BLOCK }
		BLOCK_ENTITIES.register("conjured_bouncy_block") { CONJURED_BOUNCY_BLOCK_ENTITY }
		BLOCK_ENTITIES.register("conjured_slippery_block") { CONJURED_SLIPPERY_BLOCK_ENTITY }
		BLOCK_ENTITIES.register("conjured_volatile_block") { CONJURED_VOLATILE_BLOCK_ENTITY }
		BLOCK_ENTITIES.register()
		BLOCKS.register()
	}
}