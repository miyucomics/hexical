package miyucomics.hexical.items

import at.petrak.hexcasting.api.item.HexHolderItem
import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.item.MediaHolderItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

class SonicScrewdriverItem : Item(Settings().maxCount(1).group(HexicalItems.HEXICAL_GROUP)), MediaHolderItem, IotaHolderItem {
	override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
		user!!.setCurrentHand(hand)
		return TypedActionResult.success(user.getStackInHand(hand))
	}
	override fun getUseAction(stack: ItemStack) = UseAction.BOW
	override fun getMaxUseTime(stack: ItemStack) = 20 * 2
	override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
		user.sendMessage(Text.literal("you just got sonic-ed"))
		return super.finishUsing(stack, world, user)
	}

	override fun canRecharge(stack: ItemStack?) = true
	override fun getMedia(stack: ItemStack?) = stack!!.orCreateNbt.getInt("media")
	override fun getMaxMedia(stack: ItemStack?) = MediaConstants.CRYSTAL_UNIT * 512
	override fun setMedia(stack: ItemStack?, media: Int) = stack!!.orCreateNbt.putInt("media", media)
	override fun canProvideMedia(stack: ItemStack?) = false

	override fun readIotaTag(stack: ItemStack?) = null
	override fun canWrite(stack: ItemStack?, iota: Iota?): Boolean {
		TODO("Not yet implemented")
	}
	override fun writeDatum(stack: ItemStack?, iota: Iota?) {
		TODO("Not yet implemented")
	}
}