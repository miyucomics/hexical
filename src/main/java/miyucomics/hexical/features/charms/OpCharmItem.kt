package miyucomics.hexical.features.charms

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.misc.CastingUtils
import miyucomics.hexical.misc.HexSerialization
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

object OpCharmItem : SpellAction {
	override val argc = 7
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val item = args.getItemEntity(0, argc)
		env.assertEntityInRange(item)
		if (CharmUtilities.isStackCharmed(item.stack))
			throw MishapBadEntity.of(item, "uncharmed_item")

		CastingUtils.assertNoTruename(args[1], env)
		val battery = args.getPositiveDouble(2, argc)

		return SpellAction.Result(
			Spell(
				item.stack,
				args.getList(1, argc).toList(),
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

	private data class Spell(val stack: ItemStack, val hex: List<Iota>, val battery: Long, val left: Boolean, val leftSneak: Boolean, val right: Boolean, val rightSneak: Boolean) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val nbt = stack.orCreateNbt
			val charm = NbtCompound()
			charm.putLong("media", battery)
			charm.putLong("max_media", battery)
			charm.putList("hex", HexSerialization.serializeHex(hex))
			charm.putBoolean("left", left)
			charm.putBoolean("right", right)
			charm.putBoolean("left_sneak", leftSneak)
			charm.putBoolean("right_sneak", rightSneak)
			nbt.putCompound("charmed", charm)
		}
	}
}