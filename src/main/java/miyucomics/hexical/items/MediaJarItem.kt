package miyucomics.hexical.items

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.mediaBarColor
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import miyucomics.hexical.blocks.MediaJarBlock
import miyucomics.hexical.registry.HexicalBlocks
import miyucomics.hexical.registry.HexicalSounds
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.ClickType
import net.minecraft.world.World

class MediaJarItem : BlockItem(HexicalBlocks.MEDIA_JAR_BLOCK, Settings().maxCount(1)) {
	override fun appendTooltip(stack: ItemStack, world: World?, list: MutableList<Text>, tooltipContext: TooltipContext) {
		val tag = stack.nbt?.getCompound("BlockEntityTag")
		val media = tag?.getLong("media") ?: 0
		list.add(Text.translatable("hexcasting.tooltip.media_amount.advanced",
			Text.literal(RenderUtils.DUST_AMOUNT.format((media / MediaConstants.DUST_UNIT.toFloat()).toDouble())).styled { style -> style.withColor(ItemMediaHolder.HEX_COLOR) },
			Text.translatable("hexcasting.tooltip.media", RenderUtils.DUST_AMOUNT.format((MediaJarBlock.MAX_CAPACITY / MediaConstants.DUST_UNIT.toFloat()).toDouble())).styled { style -> style.withColor(ItemMediaHolder.HEX_COLOR) },
			Text.literal(RenderUtils.PERCENTAGE.format((100f * media / MediaJarBlock.MAX_CAPACITY).toDouble()) + "%").styled { style -> style.withColor(TextColor.fromRgb(mediaBarColor(media, MediaJarBlock.MAX_CAPACITY))) }
		))
	}

	override fun onStackClicked(jar: ItemStack, slot: Slot, clickType: ClickType, player: PlayerEntity): Boolean {
		if (clickType != ClickType.RIGHT)
			return false

		val target = slot.stack
		if (!target.isEmpty) {
			val world = player.world
			val recipe = MediaJarBlock.getRecipe(target, world)
			val jarData = jar.nbt?.getCompound("BlockEntityTag") ?: return false
			if (recipe != null && jarData.getLong("media") >= recipe.cost) {
				target.decrement(1)
				jarData.putLong("media", jarData.getLong("media") - recipe.cost)

				val output = recipe.output.map { it.copy() }.toMutableList()
				slot.stack = output.removeFirst()
				output.forEach(player::giveItemStack)

				world.playSound(player.x, player.y, player.z, HexicalSounds.ITEM_DUNKS, SoundCategory.BLOCKS, 1f, 1f, true)
				return true
			}
		}

		return false
	}
}