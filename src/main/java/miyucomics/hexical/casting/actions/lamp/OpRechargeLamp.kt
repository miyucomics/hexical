package miyucomics.hexical.casting.actions.lamp

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveDoubleUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.inits.HexicalAdvancements
import miyucomics.hexical.interfaces.GenieLamp
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import kotlin.math.min

class OpRechargeLamp : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val stack = env.getHeldItemToOperateOn { stack -> stack.item is GenieLamp }
		if (stack == null)
			throw MishapBadOffhandItem.of(null, "lamp")

		val mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(stack.stack)!!
		val leftToFull = 200000 * MediaConstants.DUST_UNIT - mediaHolder.media
		val battery = min(leftToFull.toDouble(), args.getPositiveDoubleUnderInclusive(0, 200000.0, argc))

		return SpellAction.Result(Spell((battery * MediaConstants.DUST_UNIT).toInt(), stack.stack), MediaConstants.CRYSTAL_UNIT + (battery * MediaConstants.DUST_UNIT).toInt(), listOf())
	}

	private data class Spell(val battery: Int, val stack: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(stack)!!
			hexHolder.writeHex(hexHolder.getHex(env.world) ?: listOf(), null, IXplatAbstractions.INSTANCE.findMediaHolder(stack)!!.media + battery)

			if (env.castingEntity is ServerPlayerEntity)
				HexicalAdvancements.RELOAD_LAMP.trigger(env.castingEntity as ServerPlayerEntity)
		}
	}
}