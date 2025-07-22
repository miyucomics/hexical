package miyucomics.hexical.features.charms

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.misc.CastingUtils
import miyucomics.hexical.misc.HexSerialization
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

object OpCharmItem : SpellAction {
	override val argc = 5
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val item = args.getItemEntity(0, argc)
		env.assertEntityInRange(item)
		if (CharmUtilities.isStackCharmed(item.stack))
			throw MishapBadEntity.of(item, "uncharmed_item")
		val hex = args.getList(1, argc).toList()
		CastingUtils.assertNoTruename(args[1], env)
		val battery = args.getPositiveDouble(2, argc)

		val normalInputs = args.getList(3, argc).map {
			if (it !is DoubleIota) throw MishapInvalidIota.of(args[3], 1, "number_list")
			it.double.toInt()
		}
		if (normalInputs.isEmpty()) throw MishapInvalidIota.of(args[3], 1, "number_list")

		val sneakInputs = args.getList(4, argc).map {
			if (it !is DoubleIota) throw MishapInvalidIota.of(args[4], 0, "number_list")
			it.double.toInt()
		}

		return SpellAction.Result(
			Spell(item.stack, hex, (battery * MediaConstants.DUST_UNIT).toLong(), normalInputs, sneakInputs),
			3 * MediaConstants.CRYSTAL_UNIT + MediaConstants.DUST_UNIT * battery.toInt(),
			listOf(ParticleSpray.burst(item.pos, 1.0))
		)
	}

	private data class Spell(val stack: ItemStack, val hex: List<Iota>, val battery: Long, val normalInputs: List<Int>, val sneakInputs: List<Int>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			stack.orCreateNbt.putCompound("charmed", NbtCompound().also {
				it.putLong("media", battery)
				it.putLong("max_media", battery)
				it.putList("hex", HexSerialization.serializeHex(hex))
				it.putIntArray("normal_inputs", normalInputs)
				it.putIntArray("sneak_inputs", sneakInputs)
			})
		}
	}
}