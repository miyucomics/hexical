package miyucomics.hexical.features.patchouli

import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

class DyeingBlockPage : AbstractDyeingPage<BlockBundle>() {
	override fun parseProvided(name: String): BlockBundle {
		val block = Registries.BLOCK.get(Identifier(name))
		val state = block.defaultState
		val entity = if (block is BlockWithEntity) block.createBlockEntity(BlockPos.ORIGIN, state) else null
		entity?.world = MinecraftClient.getInstance().world
		return BlockBundle(state, entity)
	}

	override fun renderCustom(graphics: DrawContext, thing: BlockBundle, x: Int, y: Int, mouseX: Int, mouseY: Int, tickDelta: Float) {
		PageUtils.renderBlock(graphics, thing.state, thing.entity, x, y, 10f, tickDelta)
	}
}

data class BlockBundle(val state: BlockState, val entity: BlockEntity?)