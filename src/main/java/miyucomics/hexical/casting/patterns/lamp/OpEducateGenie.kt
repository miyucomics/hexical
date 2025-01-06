package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.inits.HexicalAdvancements
import miyucomics.hexical.interfaces.GenieLamp
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

class OpEducateGenie : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val patterns = args.getList(0, argc).toList()
		CastingUtils.assertNoTruename(args[0], env)
		val stack = env.getHeldItemToOperateOn { stack -> stack.item is GenieLamp }
		if (stack == null)
			throw MishapBadOffhandItem.of(null, "lamp")
		return SpellAction.Result(Spell(patterns, stack.stack), 0, listOf())
	}

	private data class Spell(val patterns: List<Iota>, val stack: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			IXplatAbstractions.INSTANCE.findHexHolder(stack)?.writeHex(patterns, null, IXplatAbstractions.INSTANCE.findMediaHolder(stack)?.media!!)
			if (env.castingEntity is ServerPlayerEntity)
				HexicalAdvancements.EDUCATE_GENIE.trigger(env.castingEntity as ServerPlayerEntity)
		}
	}
}