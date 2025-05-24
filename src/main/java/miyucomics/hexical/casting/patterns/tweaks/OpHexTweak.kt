package miyucomics.hexical.casting.patterns.tweaks

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

class OpHexTweak : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = env.getHeldItemToOperateOn { true }
		if (stack == null)
			throw MishapBadOffhandItem.of(null, "anything")

		args.getList(0, argc)
		CastingUtils.assertNoTruename(args[0], env)

		val battery = args.getPositiveDouble(1, argc)
		return SpellAction.Result(Spell(stack.stack, args[0], (battery * MediaConstants.DUST_UNIT).toLong()), MediaConstants.CRYSTAL_UNIT + MediaConstants.DUST_UNIT * battery.toInt(), listOf())
	}

	private data class Spell(val stack: ItemStack, val instructions: Iota, val battery: Long) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val nbt = stack.orCreateNbt
			val hexTweak = NbtCompound()
			hexTweak.putLong("media", battery)
			hexTweak.putLong("max_media", battery)
			hexTweak.putCompound("instructions", IotaType.serialize(instructions))
			nbt.putCompound("hex_tweak", hexTweak)
		}
	}
}