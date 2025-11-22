package miyucomics.hexical.features.driver_dots

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.misc.CastingUtils
import miyucomics.hexical.misc.HexSerialization
import net.minecraft.item.ItemStack

object OpProgramDriver : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val dot = env.getHeldItemToOperateOn { it.isOf(HexicalItems.DRIVER_DOT_ITEM) } ?: throw MishapBadOffhandItem.of(null, "driver_dot")
		val pattern = args.getPattern(0, argc)
		val program = args.getList(1, argc).toList()
		CastingUtils.assertNoTruename(args[0], env)
		return SpellAction.Result(Spell(dot.stack, pattern, program), 0, listOf())
	}

	private data class Spell(val dot: ItemStack, val pattern: HexPattern, val program: List<Iota>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			dot.orCreateNbt.apply {
				putCompound("pattern", pattern.serializeToNBT())
				putList("program", HexSerialization.serializeHex(program))
			}
		}
	}
}