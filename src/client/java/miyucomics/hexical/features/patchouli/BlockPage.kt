package miyucomics.hexical.features.patchouli

import com.google.gson.annotations.SerializedName
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.gui.DrawContext
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import vazkii.patchouli.client.book.BookContentsBuilder
import vazkii.patchouli.client.book.BookEntry
import vazkii.patchouli.client.book.gui.GuiBook
import vazkii.patchouli.client.book.page.abstr.PageWithText

class BlockPage : PageWithText() {
	@SerializedName("block")
	var blockId: String? = null
	@Transient private var state: BlockState = Blocks.AIR.defaultState
	@Transient private var entity: BlockEntity? = null

	override fun build(level: World, entry: BookEntry, builder: BookContentsBuilder, pageNum: Int) {
		super.build(level, entry, builder, pageNum)
		val block = Registries.BLOCK.get(Identifier(blockId))
		state = Registries.BLOCK.get(Identifier(blockId)).defaultState
		if (block is BlockWithEntity) {
			this.entity = block.createBlockEntity(BlockPos.ORIGIN, state)
			this.entity?.world = level
		}
	}

	override fun render(graphics: DrawContext, mouseX: Int, mouseY: Int, pticks: Float) {
		RenderSystem.enableBlend()
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
		super.render(graphics, mouseX, mouseY, pticks)
		PageUtils.renderBlock(graphics, state, null, GuiBook.PAGE_WIDTH / 2, 25, 25f, pticks)
	}

	override fun getTextHeight() = 55
}