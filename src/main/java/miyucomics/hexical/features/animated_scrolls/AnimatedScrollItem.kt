package miyucomics.hexical.features.animated_scrolls

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.utils.*
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.HexBlocks
import at.petrak.hexcasting.common.lib.HexSounds
import net.minecraft.client.item.TooltipData
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.event.GameEvent
import java.util.*

class AnimatedScrollItem(private val size: Int) : Item(Settings()), IotaHolderItem {
	private fun canPlaceOn(player: PlayerEntity, side: Direction, stack: ItemStack, pos: BlockPos) = !side.axis.isVertical && player.canPlaceOn(pos, side, stack)

	override fun useOnBlock(context: ItemUsageContext): ActionResult {
		val direction = context.side
		val position = context.blockPos.offset(direction)
		val player = context.player
		val stack = context.stack
		val world = context.world

		if (!world.isClient && world.getBlockState(context.blockPos).isOf(HexBlocks.AKASHIC_BOOKSHELF)) {
			val key = (world.getBlockEntity(context.blockPos) as BlockEntityAkashicBookshelf).pattern
			if (key != null) {
				player!!.swingHand(context.hand)
				world.playSound(null, context.blockPos, HexSounds.SCROLL_SCRIBBLE, SoundCategory.BLOCKS, 1f, 1f)
				writeDatum(stack, PatternIota(key))
				return ActionResult.SUCCESS
			}
		}

		if (player != null && !canPlaceOn(player, direction, stack, position))
			return ActionResult.FAIL

		val scrollStack = stack.copy()
		scrollStack.count = 1
		val scroll = AnimatedScrollEntity(world, position, direction, size, stack.getCompound("pattern")?.let(HexPattern::fromNBT), scrollStack)

		scroll.setState(stack.orCreateNbt.getInt("state"))
		if (stack.orCreateNbt.getBoolean("glow"))
			scroll.toggleGlow()
		if (stack.orCreateNbt.hasInt("color"))
			scroll.setColor(stack.orCreateNbt.getInt("color"))

		if (scroll.canStayAttached()) {
			if (!world.isClient) {
				scroll.onPlace()
				world.emitGameEvent(player, GameEvent.ENTITY_PLACE, scroll.pos)
				world.spawnEntity(scroll)
			}
			stack.decrement(1)
			return ActionResult.success(world.isClient)
		}

		return ActionResult.CONSUME
	}

	override fun getTooltipData(stack: ItemStack): Optional<TooltipData> {
		val pattern = stack.getCompound("pattern") ?: return Optional.empty()
		return Optional.of(AnimatedPatternTooltip(if (stack.containsTag("color")) stack.orCreateNbt.getInt("color") else 0xff_000000.toInt(), HexPattern.fromNBT(pattern), stack.getInt("state")))
	}

	override fun readIotaTag(stack: ItemStack): NbtCompound {
		val pattern = stack.getCompound("pattern") ?: return IotaType.serialize(NullIota())
		return IotaType.serialize(PatternIota(HexPattern.fromNBT(pattern)))
	}

	override fun writeable(stack: ItemStack) = true

	override fun canWrite(stack: ItemStack, iota: Iota?): Boolean {
		if (iota == null)
			return stack.containsTag("pattern")
		return iota is PatternIota
	}

	override fun writeDatum(stack: ItemStack, iota: Iota?) {
		if (iota == null) {
			stack.orCreateNbt.remove("pattern")
			return
		}

		stack.orCreateNbt.putCompound("pattern", (iota as PatternIota).pattern.serializeToNBT())
	}
}