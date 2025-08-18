package miyucomics.hexical.inits

import miyucomics.hexical.inits.HexicalBlocks.PERIWINKLE_FLOWER
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.render.RenderLayer

object HexicalBlocksClient {
	fun clientInit() {
		BlockRenderLayerMap.INSTANCE.putBlock(PERIWINKLE_FLOWER, RenderLayer.getCutout())
	}
}