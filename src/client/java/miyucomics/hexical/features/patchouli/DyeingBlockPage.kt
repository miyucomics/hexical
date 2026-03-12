package miyucomics.hexical.features.patchouli

import net.minecraft.block.BlockState
import net.minecraft.client.gui.DrawContext
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

class DyeingBlockPage : AbstractDyeingPage<BlockState>() {
	override fun parseProvided(name: String): BlockState = Registries.BLOCK.get(Identifier(name)).defaultState
	override fun renderCustom(graphics: DrawContext, thing: BlockState, x: Int, y: Int, mouseX: Int, mouseY: Int) {
		PageUtils.renderBlock(graphics, thing, x, y, 10f)
	}
}