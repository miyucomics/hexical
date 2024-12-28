package miyucomics.hexical.casting.patterns.grimoire

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

class OpGrimoireWrite : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = env.getHeldItemToOperateOn { stack -> stack.isOf(HexicalItems.GRIMOIRE_ITEM) }
		if (stack == null)
			throw MishapBadOffhandItem.of(null, "grimoire")
		val key = args.getPattern(0, argc)
		args.getList(1, argc)
		CastingUtils.assertNoTruename(args[1], env)
		return SpellAction.Result(Spell(stack.stack, key, args[1] as ListIota), 0, listOf())
	}

	private data class Spell(val stack: ItemStack, val key: HexPattern, val expansion: ListIota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			if (!stack.orCreateNbt.contains("expansions"))
				stack.orCreateNbt.putCompound("expansions", NbtCompound())
			stack.orCreateNbt.getCompound("expansions").putCompound(key.anglesSignature(), IotaType.serialize(expansion))
		}
	}
}