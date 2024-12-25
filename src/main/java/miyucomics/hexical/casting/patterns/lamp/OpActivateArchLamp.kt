package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.DiscoveryHandlers
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.item.ItemStack

class OpActivateArchLamp : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		for (stack in DiscoveryHandlers.collectItemSlots(env)) {
			if (stack.isEmpty)
				continue
			if (!stack.isOf(HexicalItems.ARCH_LAMP_ITEM))
				continue
			if (!stack.orCreateNbt.getBoolean("active"))
				return SpellAction.Result(Spell(stack), MediaConstants.DUST_UNIT, listOf())
		}
		return SpellAction.Result(Spell(null), 0, listOf())
	}

	private class Spell(val lampToActivate: ItemStack?) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			CastingUtils.getActiveArchLamp(env.caster)?.orCreateNbt?.putBoolean("active", false)
			if (lampToActivate == null)
				return
			lampToActivate.orCreateNbt.putBoolean("active", true)
		}
	}
}