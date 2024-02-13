package miyucomics.hexical.casting.patterns.spells

import at.petrak.hexcasting.api.misc.DiscoveryHandlers
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapLocationTooFarAway
import net.minecraft.item.Items
import net.minecraft.util.math.Vec3d

class OpChorusBlink : SpellAction {
	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val pos = args.getVec3(0, argc);
		if (pos.lengthSquared() > 256)
			throw MishapLocationTooFarAway(pos, "text.hexical.congrats.player")
		return Triple(Spell(pos), 2 * MediaConstants.DUST_UNIT, listOf(ParticleSpray.burst(pos, 1.0)))
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			for (stack in DiscoveryHandlers.collectItemSlots(ctx)) {
				if (stack.item == Items.CHORUS_FRUIT && !stack.isEmpty) {
					stack.decrement(1)
					ctx.caster.teleport(position.x, position.y, position.z)
					break
				}
			}
		}
	}
}