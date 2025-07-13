package miyucomics.hexical.features.items

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class TchotchkeItem : ItemPackagedHex(Settings().maxCount(1)) {
	override fun canDrawMediaFromInventory(stack: ItemStack) = false
	override fun isItemBarVisible(stack: ItemStack) = false
	override fun canRecharge(stack: ItemStack) = false
	override fun breakAfterDepletion() = true
	override fun cooldown() = 0

	override fun use(world: World, player: PlayerEntity, usedHand: Hand): TypedActionResult<ItemStack> {
		if (world.isClient)
			return TypedActionResult.success(player.getStackInHand(usedHand))
		val stack = player.getStackInHand(usedHand)
		if (hasHex(stack) && getMedia(stack) > 0) {
			val charmed = ItemStack(Items.STICK)
			val nbt = charmed.orCreateNbt
			val charm = NbtCompound()
			charm.putLong("media", getMedia(stack))
			charm.putLong("max_media", getMedia(stack))
			charm.putCompound("instructions", IotaType.serialize(ListIota(getHex(stack, world as ServerWorld)!!)))
			charm.putBoolean("left", true)
			charm.putBoolean("right", true)
			charm.putBoolean("left_sneak", true)
			charm.putBoolean("right_sneak", true)
			nbt.putCompound("charmed", charm)
			player.setStackInHand(usedHand, charmed)
		}
		return TypedActionResult.success(player.getStackInHand(usedHand))
	}
}