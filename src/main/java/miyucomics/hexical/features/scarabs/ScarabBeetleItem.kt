package miyucomics.hexical.features.scarabs

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.utils.*
import miyucomics.hexical.inits.HexicalSounds
import miyucomics.hexical.misc.HexSerialization
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

object ScarabBeetleItem : Item(Settings().maxCount(1).rarity(Rarity.UNCOMMON)), IotaHolderItem {
	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		val stack = user.getStackInHand(hand)
		val nbt = stack.orCreateNbt
		nbt.putBoolean("active", !nbt.getBoolean("active"))
		if (world.isClient)
			world.playSound(user.x, user.y, user.z, HexicalSounds.SCARAB_CHIRPS, SoundCategory.MASTER, 1f, 1f, true)
		return TypedActionResult.success(stack)
	}

	override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
		val nbt = stack.nbt ?: return
		if (!nbt.contains("hex"))
			return
		tooltip.add("hexical.scarab.hex".asTranslatedComponent(stack.getList("hex", NbtElement.COMPOUND_TYPE.toInt())!!.fold(Text.empty()) { acc, curr -> acc.append(IotaType.getDisplay(curr.asCompound)) }).styledWith(Formatting.GRAY))
	}

	override fun readIotaTag(stack: ItemStack) = null
	override fun writeable(stack: ItemStack) = true
	override fun canWrite(stack: ItemStack, iota: Iota?) = iota == null || iota is ListIota
	override fun writeDatum(stack: ItemStack, iota: Iota?) {
		if (iota == null)
			stack.remove("hex")
		else
			stack.putList("hex", HexSerialization.serializeHex((iota as ListIota).list.toList()))
	}
}