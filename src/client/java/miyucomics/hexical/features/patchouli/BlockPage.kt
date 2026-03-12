package miyucomics.hexical.features.patchouli

import com.google.gson.annotations.SerializedName
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.client.gui.DrawContext
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.world.World
import vazkii.patchouli.client.book.BookContentsBuilder
import vazkii.patchouli.client.book.BookEntry
import vazkii.patchouli.client.book.gui.GuiBook
import vazkii.patchouli.client.book.page.abstr.PageWithText

class BlockPage : PageWithText() {
	@SerializedName("block")
	var blockId: String? = null
	@Transient private var state: BlockState = Blocks.AIR.defaultState

	override fun build(level: World, entry: BookEntry, builder: BookContentsBuilder, pageNum: Int) {
		super.build(level, entry, builder, pageNum)
		state = Registries.BLOCK.get(Identifier(blockId)).defaultState
	}

	override fun render(graphics: DrawContext, mouseX: Int, mouseY: Int, pticks: Float) {
		RenderSystem.enableBlend()
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
		super.render(graphics, mouseX, mouseY, pticks)
		PageUtils.renderBlock(graphics, state, GuiBook.PAGE_WIDTH / 2, 25, 25f)
	}

	override fun getTextHeight() = 55
}