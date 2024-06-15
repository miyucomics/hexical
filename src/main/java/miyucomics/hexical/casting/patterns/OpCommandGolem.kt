package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.spell.mishaps.MishapImmuneEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d

class OpCommandGolem : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val golem = args.getEntity(0, argc)
		ctx.assertEntityInRange(golem)
		if (golem !is IronGolemEntity)
			throw MishapBadEntity.of(golem, "iron_golem")
		if (!golem.isPlayerCreated)
			throw MishapImmuneEntity(golem)
		val target = args.getLivingEntityButNotArmorStand(0, argc)
		ctx.assertEntityInRange(target)
		return Triple(Spell(golem, target), MediaConstants.SHARD_UNIT, listOf(ParticleSpray.burst(golem.eyePos, 1.0)))
	}

	private data class Spell(val golem: IronGolemEntity, val target: LivingEntity) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			golem.angryAt = target.uuid
		}
	}
}