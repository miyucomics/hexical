package miyucomics.hexical.casting.patterns.spells

import at.petrak.hexcasting.api.misc.DiscoveryHandlers
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.MishapLocationTooFarAway
import at.petrak.hexcasting.api.spell.mishaps.MishapOthersName
import miyucomics.hexical.items.GrimoireItem
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d

class OpInscribeInGrimoire : SpellAction {
	override val argc = 2

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val pattern = args.getPattern(0, argc);
		val hex = args.getList(1, argc).toList();
		val (stack, _) = ctx.getHeldItemToOperateOn { it.item is GrimoireItem }
		val trueName = MishapOthersName.getTrueNameFromArgs(hex, ctx.caster)
		if (trueName != null)
			throw MishapOthersName(trueName)
		return Triple(Spell(pattern, hex, stack), MediaConstants.SHARD_UNIT, listOf())
	}

	private data class Spell(val pattern: HexPattern, val hex: List<Iota>, val stack: ItemStack) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			GrimoireItem.writeToGrimoire(stack, pattern, hex)
		}
	}
}