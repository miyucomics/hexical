package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.common.misc.Brainsweeping
import at.petrak.hexcasting.ktxt.tellWitnessesThatIWasMurdered
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.interfaces.GenieLamp
import miyucomics.hexical.registry.HexicalAdvancements
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.Entity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.passive.WanderingTraderEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.village.VillageGossipType
import net.minecraft.village.VillagerData

class OpOfferMind : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val sacrifice = args.getEntity(0, argc)
		val battery = args.getPositiveDoubleUnderInclusive(1, 200000.0, argc)
		ctx.assertEntityInRange(sacrifice)

		if (sacrifice is MobEntity && Brainsweeping.isBrainswept(sacrifice))
			throw MishapBadEntity.of(sacrifice, "has_brain")
		if (sacrifice is VillagerEntity && sacrifice.villagerData.level < 3)
			throw MishapBadEntity.of(sacrifice, "smart_villager")

		val stack = ctx.caster.getStackInHand(ctx.otherHand)
		if (stack.item is GenieLamp)
			return Triple(Spell(sacrifice, (battery * MediaConstants.DUST_UNIT).toInt()), MediaConstants.CRYSTAL_UNIT + (battery * MediaConstants.DUST_UNIT).toInt(), listOf(ParticleSpray.cloud(sacrifice.eyePos, 1.0)))

		throw MishapBadOffhandItem.of(stack, ctx.otherHand, "lamp")
	}

	companion object {
		private val TRANSFORMATIONS: Map<(Entity) -> Boolean, Item> = mapOf(
			{ sacrifice: Entity -> sacrifice is WanderingTraderEntity } to HexicalItems.WANDERING_LAMP_ITEM,
			{ sacrifice: Entity -> sacrifice is VillagerEntity && sacrifice.villagerData.level > 10 } to HexicalItems.ARCH_LAMP_ITEM
		)
	}

	private data class Spell(val sacrifice: Entity, val battery: Int) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val lamp = ctx.caster.getStackInHand(ctx.otherHand)
			var sacrificedDestroyed = false

			if (lamp.isOf(HexicalItems.HAND_LAMP_ITEM)) {
				for ((predicate, replacement) in TRANSFORMATIONS) {
					if (!predicate(sacrifice))
						continue
					ctx.caster.setStackInHand(ctx.otherHand, ItemStack(replacement))
					if (sacrifice is VillagerEntity)
						sacrifice.tellWitnessesThatIWasMurdered(ctx.caster)
					sacrifice.discard()
					sacrificedDestroyed = true
					break
				}
			}

			if (sacrifice is VillagerEntity) {
				if (!sacrificedDestroyed) {
					sacrifice.villagerData = sacrifice.villagerData.withLevel(sacrifice.villagerData.level - 1)
					sacrifice.experience = VillagerData.getLowerLevelExperience(sacrifice.villagerData.level)
					sacrifice.gossip.startGossip(ctx.caster.uuid, VillageGossipType.MAJOR_NEGATIVE, 20)
				}
			}

			val newLamp = ctx.caster.getStackInHand(ctx.otherHand)
			val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(newLamp)!!
			val mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(lamp)!!
			hexHolder.writeHex(hexHolder.getHex(ctx.world) ?: listOf(), mediaHolder.media + battery)
			HexicalAdvancements.RELOAD_LAMP.trigger(ctx.caster)
		}
	}
}