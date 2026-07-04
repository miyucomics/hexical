package miyucomics.hexical.features.babelbug

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.inits.HexicalSounds
import miyucomics.hexical.misc.HexSerialization
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

object BabelbugItem : Item(Settings().maxCount(1).rarity(Rarity.UNCOMMON)), IotaHolderItem {
	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		val stack = user.getStackInHand(hand)
		val nbt = stack.orCreateNbt
		nbt.putBoolean("active", !nbt.getBoolean("active"))
		if (world.isClient)
			world.playSound(user.x, user.y, user.z, HexicalSounds.BABELBUG_CHIRPS, SoundCategory.MASTER, 1f, 1f, true)
		return TypedActionResult.success(stack)
	}

	override fun writeable(stack: ItemStack) = true
	override fun readIotaTag(stack: ItemStack) = null
	override fun canWrite(stack: ItemStack, iota: Iota?) = iota == null || iota is ListIota
	override fun writeDatum(stack: ItemStack, iota: Iota?) {
		if (iota == null)
			stack.orCreateNbt.remove("program")
		else
			stack.orCreateNbt.putList("program", HexSerialization.serializeHex((iota as ListIota).list.toList()))
	}
}