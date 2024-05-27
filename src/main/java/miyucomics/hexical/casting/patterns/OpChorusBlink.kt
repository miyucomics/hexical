package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.DiscoveryHandlers
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapLocationTooFarAway
import miyucomics.hexical.casting.mishaps.NeedsChorusFruitMishap
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.math.Vec3d

class OpChorusBlink : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val pos = args.getVec3(0, argc)
		if (pos.lengthSquared() > 256)
			throw MishapLocationTooFarAway(pos)
		for (stack in DiscoveryHandlers.collectItemSlots(ctx)) {
			if (stack.isEmpty)
				continue
			if (!stack.isOf(Items.CHORUS_FRUIT))
				continue
			return Triple(Spell(pos, stack), MediaConstants.DUST_UNIT, listOf())
		}
		throw NeedsChorusFruitMishap()
	}

	private data class Spell(val position: Vec3d, val stack: ItemStack) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			stack.decrement(1)
			ctx.caster.teleport(ctx.caster.pos.x + position.x, ctx.caster.pos.y + position.y, ctx.caster.pos.z + position.z)
		}
	}
}