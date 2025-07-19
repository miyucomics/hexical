package miyucomics.hexical.features.grimoires

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.item.ItemStack

object OpGrimoireErase : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val itemInfo = env.getHeldItemToOperateOn { stack -> stack.isOf(HexicalItems.GRIMOIRE_ITEM) }
		if (itemInfo == null)
			throw MishapBadOffhandItem.of(null, "grimoire")
		val stack = itemInfo.stack
		OpGrimoireIndex.populateGrimoireMetadata(stack)
		val pattern = args.getPattern(0, argc)
		return SpellAction.Result(Spell(stack, pattern), 0, listOf())
	}

	private data class Spell(val stack: ItemStack, val key: HexPattern) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			if (!stack.orCreateNbt.contains("expansions"))
				return
			stack.orCreateNbt.getCompound("expansions").remove(key.anglesSignature())
			stack.orCreateNbt.getCompound("metadata").remove(key.anglesSignature())
		}
	}
}