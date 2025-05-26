package miyucomics.hexical.casting.patterns.charms

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.utils.CastingUtils
import miyucomics.hexical.utils.CharmedItemUtilities
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

class OpCharmItem : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = env.getHeldItemToOperateOn { !CharmedItemUtilities.isStackCharmed(it) }
		if (stack == null)
			throw MishapBadOffhandItem.of(null, "uncharmed")

		args.getList(0, argc)
		CastingUtils.assertNoTruename(args[0], env)
		val battery = args.getPositiveDouble(1, argc)
		val inputs = args.getPositiveIntUnderInclusive(0, 15, argc)
		val a = (inputs and 0b1000) != 0
		val b = (inputs and 0b0100) != 0
		val c = (inputs and 0b0010) != 0
		val d = (inputs and 0b0001) != 0
		return SpellAction.Result(Spell(stack.stack, args[0], (battery * MediaConstants.DUST_UNIT).toLong(), a, b, c, d), 3 * MediaConstants.CRYSTAL_UNIT + MediaConstants.DUST_UNIT * battery.toInt(), listOf())
	}

	private data class Spell(val stack: ItemStack, val instructions: Iota, val battery: Long, val left: Boolean, val right: Boolean, val leftSneak: Boolean, val rightSneak: Boolean) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val nbt = stack.orCreateNbt
			val charm = NbtCompound()
			charm.putLong("media", battery)
			charm.putLong("max_media", battery)
			charm.putCompound("instructions", IotaType.serialize(instructions))
			charm.putBoolean("left", left)
			charm.putBoolean("right", right)
			charm.putBoolean("left_sneak", leftSneak)
			charm.putBoolean("right_sneak", rightSneak)
			nbt.putCompound("charmed", charm)
		}
	}
}