package miyucomics.hexical.features.amber_seal

import miyucomics.hexical.inits.HexicalBlocks
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtHelper
import net.minecraft.registry.Registries
import net.minecraft.util.math.BlockPos

object AmberSealItemRenderer : BuiltinItemRendererRegistry.DynamicItemRenderer {
	override fun render(stack: ItemStack, mode: ModelTransformationMode, matrices: MatrixStack, consumers: VertexConsumerProvider, light: Int, overlay: Int) {
		val client = MinecraftClient.getInstance()
		client.blockRenderManager.renderBlockAsEntity(HexicalBlocks.AMBER_SEAL_BLOCK.defaultState, matrices, consumers, light, overlay)

		val nbt = stack.nbt?.getCompound("BlockEntityTag") ?: return
		val state = NbtHelper.toBlockState(Registries.BLOCK.readOnlyWrapper, nbt.getCompound("EncasedBlockState"))
		val blockEntity = BlockEntity.createFromNbt(client.cameraEntity?.blockPos ?: BlockPos.ORIGIN, state, nbt.getCompound("EncasedBlockEntity")) ?: return
		blockEntity.setWorld(client.world)
		AmberSealBlockEntityRenderer.renderAmberSealInnerContents(client.blockRenderManager, client.blockEntityRenderDispatcher, state, blockEntity, client.tickDelta, matrices, consumers, light, overlay)
	}
}