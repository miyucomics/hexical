package miyucomics.hexical.casting.spells

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
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d

class OpChorusBlink : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val pos = args.getVec3(0, argc)
		if (pos.lengthSquared() > 256)
			throw MishapLocationTooFarAway(pos)
		return Triple(Spell(pos), MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			for (stack in DiscoveryHandlers.collectItemSlots(ctx)) {
				if (stack.item != Items.CHORUS_FRUIT)
					continue
				if (stack.isEmpty)
					continue
				stack.decrement(1)
				val caster: ServerPlayerEntity = ctx.caster
				caster.teleport(caster.pos.x + position.x, caster.pos.y + position.y, caster.pos.z + position.z)
				return
			}
		}
	}
}