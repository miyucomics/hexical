package miyucomics.hexical.items

import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.*
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.entities.LivingScrollEntity
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.world.event.GameEvent

class LivingScrollItem(private val size: Int) : Item(Settings().group(HexicalItems.HEXICAL_GROUP)), IotaHolderItem {
	override fun useOnBlock(ctx: ItemUsageContext?): ActionResult {
		val direction = ctx!!.side
		val clickPosition = ctx.blockPos
		val position = clickPosition.offset(direction)

		val stack = ctx.stack
		val player = ctx.player ?: return ActionResult.FAIL
		if (direction.axis.isVertical || !player.canPlaceOn(position, direction, stack))
			return ActionResult.FAIL

		val world = ctx.world
		val patternList = mutableListOf(HexPattern.fromAngles("eeeee", HexDir.EAST))
		if (stack.hasCompound("patterns") && !world.isClient) {
			patternList.clear()
			val list = (HexIotaTypes.deserialize(stack.orCreateNbt.getCompound("patterns")!!, world as ServerWorld) as ListIota).list
			for (iota in list)
				patternList.add((iota as PatternIota).pattern)
		}

		val scrollStack = stack.copy();
		scrollStack.setCount(1)
		val scrollEntity = LivingScrollEntity(world, position, direction, size, scrollStack, patternList)
		if (!scrollEntity.canStayAttached())
			return ActionResult.CONSUME

		if (!world.isClient) {
			scrollEntity.onPlace()
			world.emitGameEvent(player, GameEvent.ENTITY_PLACE, clickPosition)
			world.spawnEntity(scrollEntity)
		}
		stack.decrement(1)
		return ActionResult.success(world.isClient)
	}

	override fun readIotaTag(stack: ItemStack?): NbtCompound? {
		if (stack!!.hasCompound("patterns"))
			return stack.getCompound("patterns")
		return HexIotaTypes.serialize(NullIota())
	}

	override fun canWrite(stack: ItemStack?, iota: Iota?): Boolean {
		if (iota!!.type == HexIotaTypes.PATTERN)
			return true
		if (iota.type != HexIotaTypes.LIST)
			return false
		(iota as ListIota).list.forEach {
			if (it.type != HexIotaTypes.PATTERN)
				return false
		}
		return true
	}

	override fun writeDatum(stack: ItemStack?, iota: Iota?) {
		if (canWrite(stack, iota)) {
			var toSerialize = iota!!
			if (iota.type == HexIotaTypes.PATTERN)
				toSerialize = ListIota(listOf(iota))
			stack!!.orCreateNbt.put("patterns", HexIotaTypes.serialize(toSerialize))
		}
	}
}