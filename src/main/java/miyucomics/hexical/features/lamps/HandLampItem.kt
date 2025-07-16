package miyucomics.hexical.features.lamps

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.casting.environments.HandLampCastEnv
import miyucomics.hexical.inits.HexicalSounds
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

class HandLampItem : ItemPackagedHex(Settings().maxCount(1).rarity(Rarity.RARE)) {
	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		val stack = user.getStackInHand(hand)
		if (!hasHex(stack)) return TypedActionResult.fail(stack)
		val stackNbt = stack.orCreateNbt
		world.playSound(user.x, user.y, user.z, HexicalSounds.LAMP_ACTIVATE, SoundCategory.MASTER, 1f, 1f, true)
		if (!world.isClient) {
			stackNbt.putCompound("position", user.eyePos.serializeToNBT())
			stackNbt.putCompound("rotation", user.rotationVector.serializeToNBT())
			stackNbt.putCompound("velocity", user.velocity.serializeToNBT())
			stackNbt.putCompound("storage", IotaType.serialize(NullIota()))
			stackNbt.putLong("start_time", world.time)
		}
		user.setCurrentHand(hand)
		return TypedActionResult.success(stack)
	}

	override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
		if (world.isClient) return
		if (getMedia(stack) == 0L) return
		val vm = CastingVM(CastingImage(), HandLampCastEnv(user as ServerPlayerEntity, Hand.MAIN_HAND, false, stack))
		vm.queueExecuteAndWrapIotas((stack.item as HandLampItem).getHex(stack, world as ServerWorld)!!, world)
	}

	override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
		if (!world.isClient) {
			val vm = CastingVM(CastingImage(), HandLampCastEnv(user as ServerPlayerEntity, Hand.MAIN_HAND, true, stack))
			vm.queueExecuteAndWrapIotas((stack.item as HandLampItem).getHex(stack, world as ServerWorld)!!, world)
		}
		world.playSound(user.x, user.y, user.z, HexicalSounds.LAMP_DEACTIVATE, SoundCategory.MASTER, 1f, 1f, true)
	}

	override fun getMaxUseTime(stack: ItemStack) = Int.MAX_VALUE
	override fun canDrawMediaFromInventory(stack: ItemStack) = false
	override fun getUseAction(stack: ItemStack) = UseAction.BOW
	override fun canRecharge(stack: ItemStack?) = false
	override fun breakAfterDepletion() = false
	override fun cooldown() = 0
}