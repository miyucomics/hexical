package miyucomics.hexical.features.media_jar

import at.petrak.hexcasting.api.client.ScryingLensOverlayRegistry
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.lib.HexItems
import com.mojang.datafixers.util.Pair
import miyucomics.hexical.inits.HexicalBlocks.MEDIA_JAR_BLOCK
import miyucomics.hexical.inits.HexicalBlocks.MEDIA_JAR_BLOCK_ENTITY
import miyucomics.hexical.misc.DecimalFormats
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object MediaJarRenderHooks : InitHook() {
	override fun init() {
		BlockRenderLayerMap.INSTANCE.putBlock(MEDIA_JAR_BLOCK, RenderLayer.getCutout())
		BlockEntityRendererFactories.register(MEDIA_JAR_BLOCK_ENTITY) { MediaJarBlockEntityRenderer() }
		ScryingLensOverlayRegistry.addDisplayer(MEDIA_JAR_BLOCK) { lines, _, pos, _, world, _ ->
			val jar = world.getBlockEntity(pos) as MediaJarBlockEntity
			lines.add(Pair(ItemStack(HexItems.AMETHYST_DUST), Text.translatable("hexcasting.tooltip.media", DecimalFormats.DUST_AMOUNT.format(jar.getMedia().toFloat() / MediaConstants.DUST_UNIT.toFloat()))))
		}
	}
}