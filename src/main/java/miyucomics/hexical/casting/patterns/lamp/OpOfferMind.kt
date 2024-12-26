package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getPositiveDoubleUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.ktxt.tellWitnessesThatIWasMurdered
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.inits.HexicalAdvancements
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.interfaces.GenieLamp
import net.minecraft.entity.Entity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.village.VillageGossipType
import net.minecraft.village.VillagerData
import kotlin.math.min

class OpOfferMind : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val caster = env.castingEntity
		if (caster !is ServerPlayerEntity)
			throw MishapBadCaster()

		val sacrifice = args.getEntity(0, argc)
		env.assertEntityInRange(sacrifice)

		if (sacrifice is MobEntity && IXplatAbstractions.INSTANCE.isBrainswept(sacrifice))
			throw MishapBadEntity.of(sacrifice, "has_brain")
		if (sacrifice is VillagerEntity && sacrifice.villagerData.level < 3)
			throw MishapBadEntity.of(sacrifice, "smart_villager")

		val stack = caster.getStackInHand(env.otherHand)
		val mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(stack)!!
		val leftToFull = 200000 * MediaConstants.DUST_UNIT - mediaHolder.media
		val battery = min(leftToFull.toDouble(), args.getPositiveDoubleUnderInclusive(1, 200000.0, argc))

		if (stack.item is GenieLamp)
			return SpellAction.Result(Spell(sacrifice, (battery * MediaConstants.DUST_UNIT).toInt()), MediaConstants.CRYSTAL_UNIT + (battery * MediaConstants.DUST_UNIT).toInt(), listOf(ParticleSpray.cloud(sacrifice.eyePos, 1.0)))

		throw MishapBadOffhandItem.of(stack, "lamp")
	}

	companion object {
		private val TRANSFORMATIONS: Map<(Entity) -> Boolean, Item> = mapOf(
			{ sacrifice: Entity -> sacrifice is VillagerEntity && sacrifice.villagerData.level > 10 } to HexicalItems.ARCH_LAMP_ITEM
		)
	}

	private data class Spell(val sacrifice: Entity, val battery: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val caster = env.castingEntity as ServerPlayerEntity

			val lamp = caster.getStackInHand(env.otherHand)
			var sacrificedDestroyed = false

			if (lamp.isOf(HexicalItems.HAND_LAMP_ITEM)) {
				for ((predicate, replacement) in TRANSFORMATIONS) {
					if (!predicate(sacrifice))
						continue
					caster.setStackInHand(env.otherHand, ItemStack(replacement))
					if (sacrifice is VillagerEntity)
						sacrifice.tellWitnessesThatIWasMurdered(caster)
					sacrifice.discard()
					sacrificedDestroyed = true
					break
				}
			}

			if (sacrifice is VillagerEntity) {
				if (!sacrificedDestroyed) {
					sacrifice.villagerData = sacrifice.villagerData.withLevel(sacrifice.villagerData.level - 1)
					sacrifice.experience = VillagerData.getLowerLevelExperience(sacrifice.villagerData.level)
					sacrifice.gossip.startGossip(caster.uuid, VillageGossipType.MAJOR_NEGATIVE, 20)
				}
			}

			val newLamp = caster.getStackInHand(env.otherHand)
			val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(newLamp)!!
			val mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(lamp)!!
			hexHolder.writeHex(hexHolder.getHex(env.world) ?: listOf(), null, mediaHolder.media + battery)
			HexicalAdvancements.RELOAD_LAMP.trigger(caster)
		}
	}
}