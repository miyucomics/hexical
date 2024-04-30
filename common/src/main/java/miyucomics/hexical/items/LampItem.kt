package miyucomics.hexical.items

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.CastingContextMinterface
import miyucomics.hexical.registry.HexicalAdvancements
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.registry.HexicalSounds
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

class LampItem : ItemPackagedHex(Settings().maxCount(1).group(HexicalItems.HEXICAL_GROUP)) {
	override fun appendStacks(group: ItemGroup?, stacks: DefaultedList<ItemStack>?) {
		if (this.isIn(group)) {
			val lampStack = ItemStack(HexicalItems.LAMP_ITEM)
			val lampHexHolder = IXplatAbstractions.INSTANCE.findHexHolder(lampStack)
			lampHexHolder!!.writeHex(listOf(), MediaConstants.DUST_UNIT * 64000)
			stacks!!.add(lampStack)
		}
	}

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
		lampCast(world as ServerWorld, user as ServerPlayerEntity, getHex(stack, world) ?: return, SpecializedSource.HAND_LAMP, finale = false)
		if (getMedia(stack) == 0) {
			user.setStackInHand(user.activeHand, ItemStack(HexicalItems.LAMP_ITEM))
			HexicalAdvancements.USE_UP_LAMP.trigger(user)
		}
	}

	override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
		if (!world.isClient)
			lampCast(world as ServerWorld, user as ServerPlayerEntity, getHex(stack, world) ?: return, SpecializedSource.HAND_LAMP, finale = true)
		world.playSound(user.x, user.y, user.z, HexicalSounds.LAMP_DEACTIVATE_SOUND_EVENT, SoundCategory.MASTER, 1f, 1f, true)
	}

	override fun canDrawMediaFromInventory(stack: ItemStack) = false
	override fun getUseAction(pStack: ItemStack) = UseAction.BOW
	override fun getMaxUseTime(stack: ItemStack) = 72000
	override fun canRecharge(stack: ItemStack?) = false
	override fun breakAfterDepletion() = false
}

@Suppress("CAST_NEVER_SUCCEEDS")
fun lampCast(world: ServerWorld, user: ServerPlayerEntity, hex: List<Iota>, source: SpecializedSource, finale: Boolean) {
	val ctx = CastingContext(user, user.activeHand, CastingContext.CastSource.PACKAGED_HEX)
	(ctx as CastingContextMinterface).setSpecializedSource(source)
	(ctx as CastingContextMinterface).setFinale(finale)
	CastingHarness(ctx).executeIotas(hex, world)
}