package miyucomics.hexical.features.charms

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.getPositiveDouble
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.misc.CastingUtils
import miyucomics.hexical.misc.HexSerialization
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

object OpCharmItem : SpellAction {
	override val argc = 4
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = env.getHeldItemToOperateOn(CharmUtilities::isStackCharmed)?.stack ?: throw MishapBadOffhandItem.of(null, "uncharmed")
		val hex = args.getList(0, argc).toList()
		CastingUtils.assertNoTruename(args[0], env)
		val battery = args.getPositiveDouble(1, argc)

		val normalInputs = args.getList(3, argc).map { (it as? DoubleIota)?.double?.toInt() ?: throw MishapInvalidIota.of(args[2], 1, "number_list") }
		val sneakInputs = args.getList(4, argc).map { (it as? DoubleIota)?.double?.toInt() ?: throw MishapInvalidIota.of(args[3], 0, "number_list") }

		return SpellAction.Result(
			Spell(stack, hex, (battery * MediaConstants.DUST_UNIT).toLong(), normalInputs, sneakInputs),
			(3 * MediaConstants.CRYSTAL_UNIT + MediaConstants.DUST_UNIT * battery.toInt()) * stack.count,
			listOf()
		)
	}

	private data class Spell(val stack: ItemStack, val hex: List<Iota>, val battery: Long, val normalInputs: List<Int>, val sneakInputs: List<Int>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			stack.orCreateNbt.putCompound("charmed", NbtCompound().apply {
				putLong("media", battery)
				putLong("max_media", battery)
				putList("hex", HexSerialization.serializeHex(hex))
				putIntArray("normal_inputs", normalInputs)
				putIntArray("sneak_inputs", sneakInputs)
			})
		}
	}
}