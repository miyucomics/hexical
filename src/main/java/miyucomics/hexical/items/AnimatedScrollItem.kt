package miyucomics.hexical.items

import at.petrak.hexcasting.api.casting.iota.*
import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.utils.hasCompound
import at.petrak.hexcasting.api.utils.hasInt
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.HexBlocks
import at.petrak.hexcasting.common.lib.HexSounds
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.entities.AnimatedScrollEntity
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent

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

		val patternList = mutableListOf<NbtCompound>()
		if (stack.hasCompound("patterns") && !world.isClient) {
			val list = (IotaType.deserialize(stack.orCreateNbt.getCompound("patterns")!!, world as ServerWorld) as ListIota).list
			for (iota in list)
				patternList.add((iota as PatternIota).pattern.serializeToNBT())
		}

		val scroll = AnimatedScrollEntity(world, position, direction, size, patternList)
		if (stack.orCreateNbt.getBoolean("aged"))
			scroll.toggleAged()
		if (stack.orCreateNbt.getBoolean("glow"))
			scroll.toggleGlow()
		if (stack.orCreateNbt.getBoolean("vanished"))
			scroll.toggleVanished()
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

	override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
		if (stack.orCreateNbt.getBoolean("aged"))
			tooltip.add(Text.translatable("tooltip.hexical.scroll_aged").formatted(Formatting.GOLD))
		if (stack.orCreateNbt.getBoolean("glow"))
			tooltip.add(Text.translatable("tooltip.hexical.scroll_glow").formatted(Formatting.GOLD))
		if (stack.orCreateNbt.getBoolean("vanished"))
			tooltip.add(Text.translatable("tooltip.hexical.scroll_vanished").formatted(Formatting.GOLD))
		super.appendTooltip(stack, world, tooltip, context)
	}

	override fun readIotaTag(stack: ItemStack): NbtCompound {
		if (stack.orCreateNbt.hasCompound("patterns"))
			return stack.orCreateNbt.getCompound("patterns")
		return IotaType.serialize(NullIota())
	}

	override fun writeable(stack: ItemStack) = true

	override fun canWrite(stack: ItemStack, iota: Iota?): Boolean {
		if (iota == null)
			return true
		if (iota.type == HexIotaTypes.PATTERN)
			return true
		if (iota.type != HexIotaTypes.LIST)
			return false
		(iota as ListIota).list.forEach {
			if (it.type != HexIotaTypes.PATTERN)
				return false
		}
		return true
	}

	override fun writeDatum(stack: ItemStack, iota: Iota?) {
		if (iota == null) {
			stack.orCreateNbt.remove("patterns")
		} else {
			var toSerialize = iota
			if (iota.type == HexIotaTypes.PATTERN)
				toSerialize = ListIota(listOf(iota))
			stack.orCreateNbt.put("patterns", IotaType.serialize(toSerialize))
		}
	}
}