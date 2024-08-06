package miyucomics.hexical.items

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.registry.HexicalAdvancements
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.registry.HexicalSounds
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World

class HandLampItem : ItemPackagedHex(Settings().maxCount(1).group(HexicalItems.HEXICAL_GROUP)) {
	override fun appendStacks(group: ItemGroup, stacks: DefaultedList<ItemStack>) {
		if (this.isIn(group)) {
			val stack = ItemStack(HexicalItems.LAMP_ITEM)
			val holder = IXplatAbstractions.INSTANCE.findHexHolder(stack)
			holder!!.writeHex(listOf(), MediaConstants.DUST_UNIT * 64000)
			stacks.add(stack)
		}
	}

	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		val stack = user.getStackInHand(hand)
		if (!hasHex(stack)) return TypedActionResult.fail(stack)
		val stackNbt = stack.orCreateNbt
		world.playSound(user.x, user.y, user.z, HexicalSounds.LAMP_ACTIVATE, SoundCategory.MASTER, 1f, 1f, true)
		if (!world.isClient) {
			stackNbt.putLongArray("position", user.eyePos.serializeToNBT().longArray)
			stackNbt.putLongArray("rotation", user.rotationVector.serializeToNBT().longArray)
			stackNbt.putLongArray("velocity", user.velocity.serializeToNBT().longArray)
			stackNbt.putCompound("storage", HexIotaTypes.serialize(NullIota()))
			stackNbt.putLong("start_time", world.time)
		}
		user.setCurrentHand(hand)
		return TypedActionResult.success(stack)
	}

	override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
		if (world.isClient) return
		if (getMedia(stack) == 0) return
		CastingUtils.castSpecial(world as ServerWorld, user as ServerPlayerEntity, getHex(stack, world) ?: return, SpecializedSource.HAND_LAMP, finale = false)
		if (getMedia(stack) == 0)
			HexicalAdvancements.USE_UP_LAMP.trigger(user)
	}

	override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
		if (!world.isClient)
			CastingUtils.castSpecial(world as ServerWorld, user as ServerPlayerEntity, getHex(stack, world) ?: return, SpecializedSource.HAND_LAMP, finale = true)
		world.playSound(user.x, user.y, user.z, HexicalSounds.LAMP_DEACTIVATE, SoundCategory.MASTER, 1f, 1f, true)
	}

	override fun getMaxUseTime(stack: ItemStack) = Int.MAX_VALUE
	override fun canDrawMediaFromInventory(stack: ItemStack) = false
	override fun getUseAction(stack: ItemStack) = UseAction.BOW
	override fun canRecharge(stack: ItemStack?) = false
	override fun breakAfterDepletion() = false
}