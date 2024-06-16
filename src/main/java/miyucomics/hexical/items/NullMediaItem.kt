package miyucomics.hexical.items

import at.petrak.hexcasting.api.item.HexHolderItem
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld

class IHaveNoFormAndIMustOvercast : Item(Settings()), HexHolderItem {
	override fun getMedia(stack: ItemStack?) = 0
	override fun getMaxMedia(stack: ItemStack?) = 0
	override fun setMedia(stack: ItemStack?, media: Int) {}
	override fun canRecharge(stack: ItemStack?) = false
	override fun canProvideMedia(stack: ItemStack?) = false
	override fun canDrawMediaFromInventory(stack: ItemStack?) = true
	override fun hasHex(stack: ItemStack?) = false
	override fun getHex(stack: ItemStack?, level: ServerWorld?) = listOf<Iota>()
	override fun writeHex(stack: ItemStack?, program: MutableList<Iota>?, media: Int) {}
	override fun clearHex(stack: ItemStack?) {}
}