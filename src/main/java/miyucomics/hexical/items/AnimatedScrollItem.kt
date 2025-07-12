package miyucomics.hexical.items

import at.petrak.hexcasting.api.casting.iota.*
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.utils.*
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.HexBlocks
import at.petrak.hexcasting.common.lib.HexSounds
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.client.AnimatedPatternTooltip
import miyucomics.hexical.client.ClientStorage
import miyucomics.hexical.entities.AnimatedScrollEntity
import net.minecraft.client.item.TooltipData
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
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

		val patterns = if (stack.containsTag("patterns"))
			stack.getList("patterns", NbtElement.COMPOUND_TYPE.toInt())!!.map { it.asCompound }
		else
			listOf()

		val scrollStack = stack.copy()
		scrollStack.count = 1
		val scroll = AnimatedScrollEntity(world, position, direction, size, patterns, scrollStack)

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
		val patterns = stack.getList("patterns", NbtElement.COMPOUND_TYPE.toInt())
		if (patterns != null && patterns.isNotEmpty()) {
			val pattern = HexPattern.fromNBT(patterns[(ClientStorage.ticks / 20) % patterns.size].asCompound)
			return Optional.of(AnimatedPatternTooltip(if (stack.containsTag("color")) stack.orCreateNbt.getInt("color") else 0xff_000000.toInt(), pattern, stack.getInt("state"), stack.getBoolean("glow")))
		}
		return Optional.empty()
	}

	override fun readIotaTag(stack: ItemStack): NbtCompound {
		val patterns = stack.getList("patterns", NbtElement.COMPOUND_TYPE.toInt())
		if (patterns == null)
			return IotaType.serialize(NullIota())
		return IotaType.serialize(ListIota(patterns.map { PatternIota(HexPattern.fromNBT(it.asCompound)) }))
	}

	override fun writeable(stack: ItemStack) = true

	override fun canWrite(stack: ItemStack, iota: Iota?): Boolean {
		if (iota == null)
			return stack.containsTag("patterns")
		if (iota is PatternIota)
			return true
		if (iota !is ListIota)
			return false

		iota.list.forEach {
			if (it.type != HexIotaTypes.PATTERN)
				return false
		}
		return true
	}

	override fun writeDatum(stack: ItemStack, iota: Iota?) {
		if (iota == null) {
			stack.orCreateNbt.remove("patterns")
			return
		}

		val list = NbtList()
		when (iota) {
			is PatternIota -> list.add(iota.pattern.serializeToNBT())
			is ListIota -> iota.list.forEach { list.add((it as PatternIota).pattern.serializeToNBT()) }
		}
		stack.orCreateNbt.putList("patterns", list)
	}
}