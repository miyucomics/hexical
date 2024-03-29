package miyucomics.hexical.casting.operators.grimoire

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPattern
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import miyucomics.hexical.items.GrimoireItem
import net.minecraft.item.ItemStack

class OpGrimoireErase : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val pattern = args.getPattern(0, argc)
		val (stack, _) = ctx.getHeldItemToOperateOn { it.item is GrimoireItem }
		return Triple(Spell(pattern, stack), 0, listOf())
	}

	private data class Spell(val pattern: HexPattern, val stack: ItemStack) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			GrimoireItem.erase(stack, pattern)
		}
	}
}