package miyucomics.hexical.features.media_jar

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.mediaBarColor
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import miyucomics.hexical.features.transmuting.TransmutationResult
import miyucomics.hexical.features.transmuting.TransmutingHelper
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.inits.HexicalSounds
import miyucomics.hexical.misc.TextUtilities
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.screen.slot.Slot
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.ClickType
import net.minecraft.world.World
import kotlin.math.max
import kotlin.math.min

class MediaJarItem : BlockItem(HexicalBlocks.MEDIA_JAR_BLOCK, Settings().maxCount(1)) {
	override fun appendTooltip(stack: ItemStack, world: World?, list: MutableList<Text>, tooltipContext: TooltipContext) {
		val tag = stack.nbt?.getCompound("BlockEntityTag")
		val media = tag?.getLong("media") ?: 0
		list.add(Text.translatable("hexcasting.tooltip.media_amount.advanced",
			Text.literal(TextUtilities.DUST_AMOUNT.format((media / MediaConstants.DUST_UNIT.toFloat()).toDouble())).styled { style -> style.withColor(ItemMediaHolder.HEX_COLOR) },
			Text.translatable("hexcasting.tooltip.media", TextUtilities.DUST_AMOUNT.format((MediaJarBlock.MAX_CAPACITY / MediaConstants.DUST_UNIT.toFloat()).toDouble())).styled { style -> style.withColor(ItemMediaHolder.HEX_COLOR) },
			Text.literal(TextUtilities.PERCENTAGE.format((100f * media / MediaJarBlock.MAX_CAPACITY).toDouble()) + "%").styled { style -> style.withColor(TextColor.fromRgb(mediaBarColor(media, MediaJarBlock.MAX_CAPACITY))) }
		))
	}

	override fun onStackClicked(jar: ItemStack, slot: Slot, clickType: ClickType, player: PlayerEntity): Boolean {
		if (clickType != ClickType.RIGHT)
			return false
		val stack = slot.stack
		if (stack.isEmpty)
			return false
		val world = player.world
		val jarData = jar.nbt?.getCompound("BlockEntityTag") ?: return false

		return when (val result = TransmutingHelper.transmuteItem(world, stack, jarData.getLong("media"), { insertMedia(jarData, it) }, { withdrawMedia(jarData, it) })) {
			is TransmutationResult.AbsorbedMedia -> {
				world.playSound(player.x, player.y, player.z, HexicalSounds.AMETHYST_MELT, SoundCategory.BLOCKS, 1f, 1f, true)
				true
			}
			is TransmutationResult.TransmutedItems -> {
				world.playSound(player.x, player.y, player.z, HexicalSounds.ITEM_DUNKS, SoundCategory.BLOCKS, 1f, 1f, true)
				val output = result.output.toMutableList()
				if (slot.stack.isEmpty)
					slot.stack = output.removeFirst()
				output.forEach(player::giveItemStack)
				true
			}
			is TransmutationResult.RefilledHolder -> {
				world.playSound(player.x, player.y, player.z, HexicalSounds.ITEM_DUNKS, SoundCategory.BLOCKS, 1f, 1f, true)
				true
			}
			is TransmutationResult.Pass -> false
		}
	}

	companion object {
		fun getMedia(jarData: NbtCompound) = jarData.getLong("media")
		fun setMedia(jarData: NbtCompound, media: Long) {
			jarData.putLong("media", max(min(media, MediaJarBlock.MAX_CAPACITY), 0))
		}

		fun insertMedia(jarData: NbtCompound, media: Long): Long {
			val currentMedia = getMedia(jarData)
			setMedia(jarData, currentMedia + media)
			return this.getMedia(jarData) - currentMedia
		}

		fun withdrawMedia(jarData: NbtCompound, media: Long): Boolean {
			if (getMedia(jarData) >= media) {
				setMedia(jarData, getMedia(jarData) - media)
				return true
			} else {
				setMedia(jarData, 0)
				return false
			}
		}
	}
}