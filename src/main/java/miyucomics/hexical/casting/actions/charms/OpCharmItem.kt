package miyucomics.hexical.casting.actions.charms

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.features.charms.CharmedItemUtilities
import miyucomics.hexical.misc.CastingUtils
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

class OpCharmItem : SpellAction {
	override val argc = 7
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val item = args.getItemEntity(0, argc)
		env.assertEntityInRange(item)
		if (CharmedItemUtilities.isStackCharmed(item.stack))
			throw MishapBadEntity.of(item, "uncharmed_item")

		args.getList(1, argc)
		CastingUtils.assertNoTruename(args[1], env)
		val battery = args.getPositiveDouble(2, argc)
		return SpellAction.Result(
			Spell(
				item.stack,
				args[1],
				(battery * MediaConstants.DUST_UNIT).toLong(),
				args.getBool(3, argc),
				args.getBool(4, argc),
				args.getBool(5, argc),
				args.getBool(6, argc)
			),
			3 * MediaConstants.CRYSTAL_UNIT + MediaConstants.DUST_UNIT * battery.toInt(),
			listOf(ParticleSpray.burst(item.pos, 1.0))
		)
	}

	private data class Spell(val stack: ItemStack, val instructions: Iota, val battery: Long, val left: Boolean, val leftSneak: Boolean, val right: Boolean, val rightSneak: Boolean) : RenderedSpell {
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