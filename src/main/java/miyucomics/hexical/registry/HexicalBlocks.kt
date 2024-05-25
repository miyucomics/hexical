package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.blocks.MageBlock
import miyucomics.hexical.blocks.MageBlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry

object HexicalBlocks {
	val MAGE_BLOCK: MageBlock = MageBlock()
	val MAGE_BLOCK_ENTITY: BlockEntityType<MageBlockEntity> = BlockEntityType.Builder.create(::MageBlockEntity, MAGE_BLOCK).build(null)

	@JvmStatic
	fun init() {
		Registry.register(Registry.BLOCK, HexicalMain.id("mage_block"), MAGE_BLOCK)
		Registry.register(Registry.BLOCK_ENTITY_TYPE, HexicalMain.id("mage_block"), MAGE_BLOCK_ENTITY)
	}
}