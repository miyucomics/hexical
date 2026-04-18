package miyucomics.hexical.feature.genie_lamp

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.getPositiveDouble
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.init.HexicalAdvancements
import miyucomics.hexical.init.HexicalItems
import miyucomics.hexical.misc.CastingUtils
import miyucomics.hexical.misc.HexSerialization
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import kotlin.math.min

object OpWish : SpellAction {
	private const val MAX_CAPACITY = 100000 * MediaConstants.DUST_UNIT

	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = env.getHeldItemToOperateOn { it.isOf(HexicalItems.HAND_LAMP_ITEM) || it.isOf(HexicalItems.ARCH_LAMP_ITEM) } ?: throw MishapBadOffhandItem.of(null, "lamp")
		CastingUtils.assertNoTruename(args[0], env)
		val deposit = min(MAX_CAPACITY - CastingUtils.getMedia(stack.stack.orCreateNbt), (args.getPositiveDouble(1, 2) * MediaConstants.DUST_UNIT).toLong())
		return SpellAction.Result(Spell(stack.stack, args.getList(0, argc).toList(), deposit), deposit, listOf())
	}

	private data class Spell(val stack: ItemStack, val patterns: List<Iota>, val deposit: Long) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val nbt = stack.orCreateNbt
			nbt.putList("hex", HexSerialization.serializeHex(patterns))
			nbt.putLong("media", nbt.getLong("media") + deposit)
			nbt.putLong("max_media", nbt.getLong("media"))
			if (env.castingEntity is ServerPlayerEntity)
				HexicalAdvancements.WISH.trigger(env.castingEntity as ServerPlayerEntity)
		}
	}
}