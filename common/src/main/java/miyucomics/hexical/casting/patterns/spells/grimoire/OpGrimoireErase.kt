package miyucomics.hexical.casting.patterns.spells.grimoire

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.MishapOthersName
import miyucomics.hexical.items.GrimoireItem
import net.minecraft.item.ItemStack

class OpGrimoireErase : SpellAction {
	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val pattern = args.getPattern(0, argc);
		val (stack, _) = ctx.getHeldItemToOperateOn { it.item is GrimoireItem }
		return Triple(Spell(pattern, stack), 0, listOf())
	}

	private data class Spell(val pattern: HexPattern, val stack: ItemStack) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			GrimoireItem.eraseInGrimoire(stack, pattern)
		}
	}
}