package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.item.ItemStack

class OpScarab : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = env.getHeldItemToOperateOn { stack -> stack.isOf(HexicalItems.SCARAB_BEETLE_ITEM) }
		if (stack == null)
			throw MishapBadOffhandItem.of(null, "scarab_beetle")

		args.getList(0, argc)
		CastingUtils.assertNoTruename(args[0], env)
		return SpellAction.Result(Spell(stack.stack, args[0]), MediaConstants.SHARD_UNIT, listOf())
	}

	private data class Spell(val stack: ItemStack, val program: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			stack.orCreateNbt.putCompound("program", IotaType.serialize(program))
		}
	}
}