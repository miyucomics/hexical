package miyucomics.hexical.features.lamps

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.getPositiveDouble
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.inits.HexicalAdvancements
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.misc.CastingUtils
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import kotlin.math.min

@Suppress("OverrideOnly")
object OpWish : SpellAction {
	private const val MAX_LAMP_CAPACITY = 100000 * MediaConstants.DUST_UNIT

	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = env.getHeldItemToOperateOn { stack -> stack.isOf(HexicalItems.HAND_LAMP_ITEM) || stack.isOf(HexicalItems.ARCH_LAMP_ITEM) }
		if (stack == null)
			throw MishapBadOffhandItem.of(null, "lamp")

		CastingUtils.assertNoTruename(args[0], env)
		val mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(stack.stack)!!
		val deposit = min(MAX_LAMP_CAPACITY - mediaHolder.media, (args.getPositiveDouble(1, 2) * MediaConstants.DUST_UNIT).toLong())

		return SpellAction.Result(Spell(stack.stack, args.getList(0, argc).toList(), deposit), deposit, listOf())
	}

	private data class Spell(val stack: ItemStack, val patterns: List<Iota>, val deposit: Long) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			IXplatAbstractions.INSTANCE.findHexHolder(stack)?.writeHex(patterns, null, IXplatAbstractions.INSTANCE.findMediaHolder(stack)!!.media + deposit)
			if (env.castingEntity is ServerPlayerEntity)
				HexicalAdvancements.EDUCATE_GENIE.trigger(env.castingEntity as ServerPlayerEntity)
		}
	}
}