package miyucomics.hexical.items

import at.petrak.hexcasting.api.item.HexHolderItem
import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class SonicScrewdriverItem : Item(Settings().maxCount(1).group(HexicalItems.HEXICAL_GROUP)), HexHolderItem, IotaHolderItem {
	override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
		val stack = user!!.getStackInHand(hand)
		setMedia(stack, getMedia(stack) - MediaConstants.DUST_UNIT)
		return super.use(world, user, hand)
	}

	override fun canRecharge(stack: ItemStack?) = true
	override fun getMedia(stack: ItemStack?) = stack!!.orCreateNbt.getInt("media")
	override fun getMaxMedia(stack: ItemStack?) = MediaConstants.CRYSTAL_UNIT * 512
	override fun setMedia(stack: ItemStack?, media: Int) = stack!!.orCreateNbt.putInt("media", media)

	override fun hasHex(stack: ItemStack?) = false
	override fun canProvideMedia(stack: ItemStack?) = false
	override fun canDrawMediaFromInventory(stack: ItemStack?) = false
	override fun getHex(stack: ItemStack?, level: ServerWorld?) = listOf<Iota>()
	override fun writeHex(stack: ItemStack?, program: MutableList<Iota>?, media: Int) {}
	override fun clearHex(stack: ItemStack?) {}

	override fun readIotaTag(stack: ItemStack?) = null
	override fun canWrite(stack: ItemStack?, iota: Iota?): Boolean {
		TODO("Not yet implemented")
	}
	override fun writeDatum(stack: ItemStack?, iota: Iota?) {
		TODO("Not yet implemented")
	}
}