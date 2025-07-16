package miyucomics.hexical.features.media_jar

import at.petrak.hexcasting.api.client.ScryingLensOverlayRegistry
import miyucomics.hexical.inits.HexicalBlocks.MEDIA_JAR_BLOCK
import miyucomics.hexical.inits.HexicalBlocks.MEDIA_JAR_BLOCK_ENTITY
import miyucomics.hexical.inits.InitHook
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories

object MediaJarRenderHooks : InitHook() {
	override fun init() {
		BlockRenderLayerMap.INSTANCE.putBlock(MEDIA_JAR_BLOCK, RenderLayer.getCutout())
		BlockEntityRendererFactories.register(MEDIA_JAR_BLOCK_ENTITY) { MediaJarBlockEntityRenderer() }
		ScryingLensOverlayRegistry.addDisplayer(MEDIA_JAR_BLOCK) { lines, _, pos, _, world, _ -> (world.getBlockEntity(pos) as MediaJarBlockEntity).scryingLensOverlay(lines) }
	}
}