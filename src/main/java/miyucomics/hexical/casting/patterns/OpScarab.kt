package miyucomics.hexical.casting.patterns

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
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

class OpScarab : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = env.getHeldItemToOperateOn { stack -> stack.isOf(HexicalItems.SCARAB_BEETLE_ITEM) }
		if (stack == null)
			throw MishapBadOffhandItem.of(null, "scarab_beetle")

		val key = args.getPattern(0, argc)
		args.getList(1, argc)
		CastingUtils.assertNoTruename(args[1], env)
		return SpellAction.Result(Spell(stack.stack, key, args[1]), MediaConstants.SHARD_UNIT, listOf())
	}

	private data class Spell(val stack: ItemStack, val key: HexPattern, val expansion: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			stack.orCreateNbt.putCompound("pattern", key.serializeToNBT())
			stack.orCreateNbt.putCompound("expansion", IotaType.serialize(expansion))
		}
	}
}