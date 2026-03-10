package miyucomics.hexical.features.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import miyucomics.hexical.HexicalMain;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageWithText;

public class BlockPage extends PageWithText {
	@SerializedName("block")
	public String blockId;
	private transient BlockState state;

	@Override
	public int getTextHeight() {
		return 55;
	}

	@Override
	public void build(World level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(level, entry, builder, pageNum);
		state = Registries.BLOCK
			.getOrEmpty(Identifier.tryParse(blockId))
			.map(Block::getDefaultState)
			.orElse(null);
	}

	@Override
	public void render(DrawContext graphics, int mouseX, int mouseY, float pticks) {
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		renderBlock(graphics, GuiBook.PAGE_WIDTH / 2, 25);
		super.render(graphics, mouseX, mouseY, pticks);
	}

	private void renderBlock(DrawContext graphics, int centerX, int centerY) {
		MatrixStack matrices = graphics.getMatrices();
		matrices.push();
		matrices.translate(centerX, centerY, 100);
		matrices.scale(25F, -25F, 25F);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30F));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45F));
		matrices.translate(-0.5, -0.5, -0.5);

		MinecraftClient client = MinecraftClient.getInstance();
		VertexConsumerProvider.Immediate consumers = client.getBufferBuilders().getEntityVertexConsumers();
		client.getBlockRenderManager().renderBlock(state, client.player.getBlockPos().add(0, 1000, 0), client.world, matrices, consumers.getBuffer(RenderLayers.getBlockLayer(state)), false, HexicalMain.RANDOM);
		consumers.draw();

		matrices.pop();
	}
}