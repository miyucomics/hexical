package miyucomics.hexical.datagen.generators

import miyucomics.hexical.inits.HexicalBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider

class HexicalBlockLootTableGenerator(generator: FabricDataOutput) : FabricBlockLootTableProvider(generator) {
	override fun generate() {
		addDrop(HexicalBlocks.CASTING_CARPET)
		addDrop(HexicalBlocks.HEX_CANDLE_BLOCK, candleDrops(HexicalBlocks.HEX_CANDLE_BLOCK))
		addDrop(HexicalBlocks.HEX_CANDLE_CAKE_BLOCK, candleCakeDrops(HexicalBlocks.HEX_CANDLE_CAKE_BLOCK))
		addDrop(HexicalBlocks.PEDESTAL_BLOCK)
		addDrop(HexicalBlocks.PERIWINKLE_FLOWER, flowerbedDrops(HexicalBlocks.PERIWINKLE_FLOWER))
		addDrop(HexicalBlocks.SENTINEL_BED_BLOCK)
	}
}