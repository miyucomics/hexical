package miyucomics.hexical.items

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.interfaces.CastingContextMixinInterface
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.registry.HexicalSounds
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

class LampItem : ItemPackagedHex(Settings().maxCount(1).group(HexicalItems.HEXICAL_GROUP)) {
	override fun use(world: World, user: PlayerEntity, usedHand: Hand): TypedActionResult<ItemStack> {
		val stack = user.getStackInHand(usedHand)
		if (!hasHex(stack)) return TypedActionResult.fail(stack)
		val stackNbt = stack.orCreateNbt
		world.playSound(user.x, user.y, user.z, HexicalSounds.LAMP_ACTIVATE_SOUND_EVENT, SoundCategory.MASTER, 1f, 1f, true)
		if (!world.isClient) {
			stackNbt.putLongArray("position", user.eyePos.serializeToNBT().longArray)
			stackNbt.putLongArray("rotation", user.rotationVector.serializeToNBT().longArray)
			stackNbt.putLongArray("velocity", user.velocity.serializeToNBT().longArray)
			stackNbt.putLong("start_time", world.time)
		}
		user.setCurrentHand(usedHand)
		return TypedActionResult.success(stack)
	}

	override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
		if (world.isClient) return
		if (getMedia(stack) == 0) return
		lampCast(world as ServerWorld, user as ServerPlayerEntity, getHex(stack, world) ?: return, false, false)
		if (getMedia(stack) == 0)
			user.setStackInHand(user.activeHand, ItemStack(HexicalItems.LAMP_ITEM))
	}

	override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
		if (!world.isClient)
			lampCast(world as ServerWorld, user as ServerPlayerEntity, getHex(stack, world) ?: return, false, true)
		world.playSound(user.x, user.y, user.z, HexicalSounds.LAMP_DEACTIVATE_SOUND_EVENT, SoundCategory.MASTER, 1f, 1f, true)
	}

	override fun getUseAction(pStack: ItemStack): UseAction {
		return UseAction.BOW
	}

	override fun getMaxUseTime(stack: ItemStack): Int {
		return 72000
	}

	override fun breakAfterDepletion(): Boolean {
		return false
	}

	override fun canDrawMediaFromInventory(stack: ItemStack): Boolean {
		return false
	}
}

@Suppress("CAST_NEVER_SUCCEEDS")
fun lampCast(world: ServerWorld, user: ServerPlayerEntity, hex: List<Iota>, archLamp: Boolean, finale: Boolean) {
	val ctx = CastingContext(user, user.activeHand, CastingContext.CastSource.PACKAGED_HEX)
	(ctx as CastingContextMixinInterface).setCastByLamp(true)
	(ctx as CastingContextMixinInterface).setArchLamp(archLamp)
	(ctx as CastingContextMixinInterface).setFinale(finale)
	CastingHarness(ctx).executeIotas(hex, world)
}