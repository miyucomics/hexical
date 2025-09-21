package miyucomics.hexical.features.lamps

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.inits.HexicalSounds
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.world.GameMode
import net.minecraft.world.World

object ArchLampItem : ItemPackagedHex(Settings().maxCount(1).rarity(Rarity.EPIC)) {
	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		val stack = user.getStackInHand(hand)
		if (!hasHex(stack))
			return TypedActionResult.fail(stack)

		val stackNbt = stack.orCreateNbt
		if (!stackNbt.contains("active"))
			stackNbt.putBoolean("active", false)

		if (world.isClient) {
			world.playSound(user.x, user.y, user.z, if (stackNbt.getBoolean("active")) HexicalSounds.LAMP_DEACTIVATE else HexicalSounds.LAMP_ACTIVATE, SoundCategory.MASTER, 1f, 1f, true)
			return TypedActionResult.success(stack)
		}

		if (stackNbt.getBoolean("active")) {
			val vm = CastingVM(CastingImage(), ArchLampCastEnv(user as ServerPlayerEntity, hand, true, stack))
			vm.queueExecuteAndWrapIotas((stack.item as ArchLampItem).getHex(stack, world as ServerWorld)!!, world)
			stackNbt.putBoolean("active", false)
			return TypedActionResult.success(stack)
		}

		stackNbt.putBoolean("active", true)

		val state = user.getArchLampField()
		state.position = user.eyePos
		state.rotation = user.rotationVector
		state.velocity = user.velocity
		state.storage = IotaType.serialize(NullIota())
		state.time = world.time

		return TypedActionResult.success(stack)
	}

	override fun inventoryTick(stack: ItemStack, world: World, user: Entity, slot: Int, selected: Boolean) {
		if (world.isClient) return
		if (getMedia(stack) == 0L) return
		if (user !is ServerPlayerEntity) return
		if (!stack.orCreateNbt.getBoolean("active")) return
		if (user.interactionManager.gameMode == GameMode.SPECTATOR) return
		val vm = CastingVM(CastingImage(), ArchLampCastEnv(user, Hand.MAIN_HAND, false, stack))
		vm.queueExecuteAndWrapIotas((stack.item as ArchLampItem).getHex(stack, world as ServerWorld)!!, world)
	}

	override fun canDrawMediaFromInventory(stack: ItemStack) = false
	override fun canRecharge(stack: ItemStack) = false
	override fun breakAfterDepletion() = false
	override fun cooldown() = 0
}

fun hasActiveArchLamp(player: ServerPlayerEntity): Boolean {
	for (stack in player.inventory.main)
		if (stack.item == HexicalItems.ARCH_LAMP_ITEM && stack.orCreateNbt.getBoolean("active"))
			return true
	for (stack in player.inventory.offHand)
		if (stack.item == HexicalItems.ARCH_LAMP_ITEM && stack.orCreateNbt.getBoolean("active"))
			return true
	return false
}