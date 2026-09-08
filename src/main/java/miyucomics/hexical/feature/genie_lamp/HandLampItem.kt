package miyucomics.hexical.feature.genie_lamp

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.mediaBarColor
import at.petrak.hexcasting.api.utils.mediaBarWidth
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import miyucomics.hexical.init.HexicalSounds
import miyucomics.hexical.misc.CastingUtils
import miyucomics.hexical.misc.TextUtilities
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

object HandLampItem : Item(Settings().maxCount(1).rarity(Rarity.RARE)) {
	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		val stack = user.getStackInHand(hand)
		if (stack.nbt?.contains("hex", NbtElement.LIST_TYPE.toInt()) != true)
			return TypedActionResult.fail(stack)

		val nbt = stack.orCreateNbt
		world.playSound(user.x, user.y, user.z, HexicalSounds.LAMP_ACTIVATE, SoundCategory.MASTER, 1f, 1f, true)
		if (!world.isClient) {
			nbt.putCompound("position", user.eyePos.serializeToNBT())
			nbt.putCompound("rotation", user.rotationVector.serializeToNBT())
			nbt.putCompound("velocity", user.velocity.serializeToNBT())
			nbt.putCompound("storage", IotaType.serialize(NullIota()))
			nbt.putLong("start_time", world.time)
		}

		user.setCurrentHand(hand)
		return TypedActionResult.success(stack)
	}

	override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
		if (world.isClient)
			return
		if (stack.nbt!!.getLong("media") == 0L)
			return
		val vm = CastingVM(CastingImage(), LampCastEnv(user as ServerPlayerEntity, user.activeHand, false, stack))
		vm.queueExecuteAndWrapIotas(CastingUtils.getHex(stack.nbt!!, world as ServerWorld), world)
	}

	override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
		if (!world.isClient) {
			val vm = CastingVM(CastingImage(), LampCastEnv(user as ServerPlayerEntity, user.activeHand, true, stack))
			vm.queueExecuteAndWrapIotas(CastingUtils.getHex(stack.nbt!!, world as ServerWorld), world)
		}

		world.playSound(user.x, user.y, user.z, HexicalSounds.LAMP_DEACTIVATE, SoundCategory.MASTER, 1f, 1f, true)
	}

	override fun isItemBarVisible(stack: ItemStack) = stack.hasNbt() && CastingUtils.getMaxMedia(stack.nbt!!) > 0

	override fun getItemBarColor(stack: ItemStack): Int {
		if (stack.hasNbt())
			return mediaBarColor(CastingUtils.getMedia(stack.nbt!!), CastingUtils.getMaxMedia(stack.nbt!!))
		return 0
	}

	override fun getItemBarStep(stack: ItemStack): Int {
		if (stack.hasNbt())
			return mediaBarWidth(CastingUtils.getMedia(stack.nbt!!), CastingUtils.getMaxMedia(stack.nbt!!))
		return 0
	}

	override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
		if (!stack.hasNbt())
			return
		val media = CastingUtils.getMedia(stack.nbt!!)
		val maxMedia = CastingUtils.getMaxMedia(stack.nbt!!)
		tooltip.add(Text.translatable("hexcasting.tooltip.media_amount.advanced",
			Text.literal(TextUtilities.DUST_AMOUNT.format((media / MediaConstants.DUST_UNIT.toFloat()).toDouble())).styled { style -> style.withColor(ItemMediaHolder.HEX_COLOR) },
			Text.translatable("hexcasting.tooltip.media", TextUtilities.DUST_AMOUNT.format((maxMedia / MediaConstants.DUST_UNIT.toFloat()).toDouble())).styled { style -> style.withColor(ItemMediaHolder.HEX_COLOR) },
			Text.literal(TextUtilities.PERCENTAGE.format((100f * media / maxMedia).toDouble()) + "%").styled { style -> style.withColor(TextColor.fromRgb(mediaBarColor(media, maxMedia))) }
		))
	}

	override fun getMaxUseTime(stack: ItemStack) = Int.MAX_VALUE
	override fun getUseAction(stack: ItemStack) = UseAction.BOW
}