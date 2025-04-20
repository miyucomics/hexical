package miyucomics.hexical.items

import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.utils.containsTag
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.remove
import at.petrak.hexcasting.api.utils.styledWith
import miyucomics.hexical.casting.frames.ScarabFrame
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.registry.HexicalSounds
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.*
import net.minecraft.world.World

class ScarabBeetleItem : Item(Settings().maxCount(1).rarity(Rarity.UNCOMMON)), IotaHolderItem {
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

	override fun readIotaTag(stack: ItemStack) = null
	override fun canWrite(stack: ItemStack, iota: Iota?) = iota == null || writeable(stack)
	override fun writeable(stack: ItemStack) = !stack.containsTag("program")
	override fun writeDatum(stack: ItemStack, iota: Iota?) {
		if (iota == null) {
			stack.remove("program")
			return
		}
		stack.putCompound("program", IotaType.serialize(iota))
	}

	companion object {
		fun registerModelPredicate() {
			ModelPredicateProviderRegistry.register(HexicalItems.SCARAB_BEETLE_ITEM, Identifier("active")) { stack, _, _, _ ->
				if (stack.nbt?.getBoolean("active") == true)
					1.0f
				else
					0.0f
			}
		}

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