package miyucomics.hexical.casting.spells.lamp

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.item.ItemStack

class OpEducateGenie : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val patterns = args.getList(0, argc).toList()
		val (stack, _) = ctx.getHeldItemToOperateOn { IXplatAbstractions.INSTANCE.findHexHolder(it) != null }
		return Triple(Spell(patterns, stack), 0, listOf())
	}

	private data class Spell(val patterns: List<Iota>, val stack: ItemStack) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			IXplatAbstractions.INSTANCE.findHexHolder(stack)?.writeHex(patterns, IXplatAbstractions.INSTANCE.findMediaHolder(stack)?.media!!)
		}
	}
}