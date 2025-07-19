package miyucomics.hexical.features.cracked_items

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.features.charms.CharmUtilities
import miyucomics.hexical.features.curios.CurioItem
import net.minecraft.item.ItemStack

object OpCrackDevice : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val heldData = env.getHeldItemToOperateOn { it.item is ItemPackagedHex || it.item is CurioItem }
		if (heldData == null)
			throw MishapBadOffhandItem.Companion.of(null, "crackable_item")
		val stack = heldData.stack
		if (stack.item is ItemPackagedHex && (stack.item as ItemPackagedHex).hasHex(stack))
			throw IllegalJailbreakMishap()
		if (stack.item is CurioItem && CharmUtilities.isStackCharmed(stack))
			throw IllegalJailbreakMishap()
		return SpellAction.Result(Spell(stack), MediaConstants.CRYSTAL_UNIT, listOf())
	}

	private data class Spell(val stack: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			stack.orCreateNbt.putBoolean("cracked", true)
		}
	}
}