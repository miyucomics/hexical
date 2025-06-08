package miyucomics.hexical.items

import miyucomics.hexical.blocks.MediaJarBlock
import miyucomics.hexical.registry.HexicalBlocks
import miyucomics.hexical.utils.MediaJarRenderStuffs
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack

class MediaJarItemRenderer : BuiltinItemRendererRegistry.DynamicItemRenderer {
	override fun render(stack: ItemStack, mode: ModelTransformationMode, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
		MinecraftClient.getInstance().blockRenderManager.renderBlockAsEntity(HexicalBlocks.MEDIA_JAR_BLOCK.defaultState, matrices, vertexConsumers, light, overlay)
		val tag = stack.nbt?.getCompound("BlockEntityTag")
		val media = tag?.getLong("media") ?: 0
		MediaJarRenderStuffs.renderFluid(matrices, vertexConsumers, media.toFloat() / MediaJarBlock.MAX_CAPACITY.toFloat())
	}
}