package miyucomics.hexical.items

import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.styledWith
import miyucomics.hexical.casting.frames.ScarabFrame
import miyucomics.hexical.inits.HexicalSounds
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class ScarabBeetleItem : Item(Settings().maxCount(1).rarity(Rarity.UNCOMMON)) {
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
		if (!nbt.contains("program"))
			return
		val program = IotaType.getDisplay(nbt.getCompound("program"))
		tooltip.add(Text.translatable("hexical.scarab.program", program).styledWith(Formatting.GRAY))
		super.appendTooltip(stack, world, tooltip, context)
	}

	companion object {
		fun wouldBeRecursive(pattern: String, continuation: SpellContinuation): Boolean {
			var cont = continuation
			while (cont is SpellContinuation.NotDone) {
				if (cont.frame is ScarabFrame && (cont.frame as ScarabFrame).signature == pattern)
					return true
				cont = cont.next
			}
			return false
		}
	}
}