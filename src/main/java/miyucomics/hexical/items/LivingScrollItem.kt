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
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
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
	private fun canPlaceOn(player: PlayerEntity, side: Direction?, stack: ItemStack?, pos: BlockPos?): Boolean {
		return !player.world.isOutOfHeightLimit(pos) && player.canPlaceOn(pos, side, stack)
	}

	override fun useOnBlock(context: ItemUsageContext): ActionResult {
		val direction = context.side
		val adjustedPosition = context.blockPos.offset(direction)
		val player = context.player
		val stack = context.stack

		if (player != null && !canPlaceOn(player, direction, stack, adjustedPosition))
			return ActionResult.FAIL

		val world = context.world
		val patternList = mutableListOf(HexPattern.fromAngles("eeeee", HexDir.EAST))
		if (stack.hasCompound("patterns") && !world.isClient) {
			patternList.clear()
			val list = (HexIotaTypes.deserialize(stack.orCreateNbt.getCompound("patterns")!!, world as ServerWorld) as ListIota).list
			for (iota in list)
				patternList.add((iota as PatternIota).pattern)
		}

		val scroll = LivingScrollEntity(world, adjustedPosition, direction, size, patternList)
		val nbtCompound = stack.nbt
		if (nbtCompound != null)
			EntityType.loadFromEntityNbt(world, player, scroll, nbtCompound)

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

	override fun readIotaTag(stack: ItemStack): NbtCompound? {
		if (stack.orCreateNbt.hasCompound("patterns"))
			return stack.orCreateNbt.getCompound("patterns")
		return HexIotaTypes.serialize(NullIota())
	}

	override fun canWrite(stack: ItemStack, iota: Iota?): Boolean {
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

	override fun writeDatum(stack: ItemStack, iota: Iota?) {
		if (canWrite(stack, iota)) {
			var toSerialize = iota!!
			if (iota.type == HexIotaTypes.PATTERN)
				toSerialize = ListIota(listOf(iota))
			stack.orCreateNbt.put("patterns", HexIotaTypes.serialize(toSerialize))
		}
	}
}