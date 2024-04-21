package miyucomics.hexical.iota

import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.IotaType
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.api.utils.styledWith
import miyucomics.hexical.registry.HexicalIota
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ItemStackIota(identifier: ItemStack) : Iota(HexicalIota.ITEM_STACK_IOTA, identifier) {
	override fun isTruthy() = true
	val stack = this.payload as ItemStack
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is ItemStackIota) && this.stack == that.stack

	override fun serialize(): NbtElement {
		val compound = NbtCompound()
		compound.putCompound("stack", stack.serializeToNBT())
		compound.putString("name", stack.name.string)
		return compound
	}

	companion object {
		var TYPE: IotaType<ItemStackIota> = object : IotaType<ItemStackIota>() {
			override fun color() = -0x2155e3
			override fun display(tag: NbtElement) = Text.literal((tag as NbtCompound).getString("name")).styledWith(Formatting.BLUE)
			override fun deserialize(tag: NbtElement?, world: ServerWorld?) = ItemStackIota(ItemStack.fromNbt((tag as NbtCompound).getCompound("stack")))
		}
	}
}

fun List<Iota>.getItemStack(idx: Int, argc: Int = 0): ItemStack {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is ItemStackIota)
		return x.stack
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "item_stack")
}