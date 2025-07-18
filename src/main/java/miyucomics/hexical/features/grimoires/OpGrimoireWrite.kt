package miyucomics.hexical.features.grimoires

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.utils.containsTag
import at.petrak.hexcasting.api.utils.getCompound
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.features.grimoires.OpGrimoireIndex.Companion.populateGrimoireMetadata
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.misc.CastingUtils
import miyucomics.hexical.misc.SerializationUtils
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

class OpGrimoireWrite : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val itemInfo = env.getHeldItemToOperateOn { stack -> stack.isOf(HexicalItems.GRIMOIRE_ITEM) }
		if (itemInfo == null)
			throw MishapBadOffhandItem.of(null, "grimoire")

		val stack = itemInfo.stack
		if (stack.containsTag("expansions") && stack.getCompound("expansions")!!.size > 512)
			throw MishapBadOffhandItem.of(null, "nonfull_grimoire")

		populateGrimoireMetadata(stack)
		CastingUtils.assertNoTruename(args[1], env)

		return SpellAction.Result(Spell(stack, args.getPattern(0, argc), args.getList(1, argc).toList()), 0, listOf())
	}

	private data class Spell(val stack: ItemStack, val key: HexPattern, val expansion: List<Iota>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			if (!stack.orCreateNbt.contains("expansions"))
				stack.orCreateNbt.putCompound("expansions", NbtCompound())
			stack.orCreateNbt.getCompound("expansions").putList(key.anglesSignature(), SerializationUtils.serializeHex(expansion))

			if (!stack.orCreateNbt.contains("metadata"))
				stack.orCreateNbt.putCompound("metadata", NbtCompound())
			val data = NbtCompound()
			data.putInt("direction", key.startDir.ordinal)
			stack.orCreateNbt.getCompound("metadata").putCompound(key.anglesSignature(), data)
		}
	}
}