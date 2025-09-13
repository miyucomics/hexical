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
		CastingUtils.assertNoTruename(args[1], env) // no fun allowed :Ɛ
		val battery = args.getPositiveDouble(2, argc)

		val normalValues = args.getList(3, argc).map {
			if (it !is DoubleIota) throw MishapInvalidIota.of(args[3], 1, "number_list")
			it.double
		}

		val sneakValues = args.getList(4, argc).map {
			if (it !is DoubleIota) throw MishapInvalidIota.of(args[4], 0, "number_list")
			it.double
		}

		val (normalInputs, releaseInputs) = normalValues.partition { 1 / it >= 0 }
		val (sneakInputs, sneakReleaseInputs) = sneakValues.partition { 1 / it >= 0 }

		return SpellAction.Result(
			Spell(item.stack, hex, (battery * MediaConstants.DUST_UNIT).toLong(), normalInputs.map { it.toInt() }, sneakInputs.map { it.toInt() }, releaseInputs.map { (-it).toInt() }, sneakReleaseInputs.map { (-it).toInt() }),
			(3 * MediaConstants.CRYSTAL_UNIT + MediaConstants.DUST_UNIT * battery.toInt()) * item.stack.count,
			listOf(ParticleSpray.burst(item.pos, 1.0))
		)
	}

	private data class Spell(val stack: ItemStack, val hex: List<Iota>, val battery: Long, val normalInputs: List<Int>, val sneakInputs: List<Int>, val releaseInputs: List<Int>, val sneakReleaseInputs: List<Int>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			stack.orCreateNbt.putCompound("charmed", NbtCompound().also {
				it.putLong("media", battery)
				it.putLong("max_media", battery)
				it.putList("hex", HexSerialization.serializeHex(hex))
				if (!normalInputs.isEmpty()) it.putIntArray("normal_inputs", normalInputs)
				if (!sneakInputs.isEmpty()) it.putIntArray("sneak_inputs", sneakInputs)
				if (!releaseInputs.isEmpty()) it.putIntArray("normal_release", releaseInputs)
				if (!sneakReleaseInputs.isEmpty()) it.putIntArray("sneak_inputs", sneakReleaseInputs)
			})
		}
	}
}