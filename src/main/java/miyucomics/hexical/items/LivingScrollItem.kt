package miyucomics.hexical.items

import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.hasCompound
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.entities.LivingScrollEntity
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.event.GameEvent

class LivingScrollItem(private val size: Int) : Item(Settings().group(HexicalItems.HEXICAL_GROUP)), IotaHolderItem {
	private fun canPlaceOn(player: PlayerEntity, side: Direction, stack: ItemStack, pos: BlockPos) = !side.axis.isVertical && player.canPlaceOn(pos, side, stack)

	override fun useOnBlock(context: ItemUsageContext): ActionResult {
		val direction = context.side
		val position = context.blockPos.offset(direction)
		val player = context.player
		val stack = context.stack
		if (player != null && !canPlaceOn(player, direction, stack, position))
			return ActionResult.FAIL

		val world = context.world
		val patternList = mutableListOf(HexPattern.fromAngles("eeeee", HexDir.EAST).serializeToNBT())
		if (stack.hasCompound("patterns") && !world.isClient) {
			patternList.clear()
			val list = (HexIotaTypes.deserialize(stack.orCreateNbt.getCompound("patterns")!!, world as ServerWorld) as ListIota).list
			for (iota in list)
				patternList.add((iota as PatternIota).pattern.serializeToNBT())
		}

		val scroll = LivingScrollEntity(world, position, direction, size, patternList)
		if (stack.orCreateNbt.getBoolean("aged"))
			scroll.toggleAged()

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

	override fun readIotaTag(stack: ItemStack): NbtCompound {
		if (stack.orCreateNbt.hasCompound("patterns"))
			return stack.orCreateNbt.getCompound("patterns")
		return HexIotaTypes.serialize(NullIota())
	}

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
			stack.orCreateNbt.put("patterns", HexIotaTypes.serialize(toSerialize))
		}
	}
}