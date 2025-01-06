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
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.village.VillageGossipType
import net.minecraft.village.VillagerData
import kotlin.math.min

class OpOfferMind : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val sacrifice = args.getEntity(0, argc)
		env.assertEntityInRange(sacrifice)

		if (sacrifice is MobEntity && IXplatAbstractions.INSTANCE.isBrainswept(sacrifice))
			throw MishapBadEntity.of(sacrifice, "has_brain")
		if (sacrifice is VillagerEntity && sacrifice.villagerData.level < 3)
			throw MishapBadEntity.of(sacrifice, "smart_villager")

		val stack = env.getHeldItemToOperateOn { stack -> stack.item is GenieLamp }
		if (stack == null)
			throw MishapBadOffhandItem.of(null, "lamp")

		val mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(stack.stack)!!
		val leftToFull = 200000 * MediaConstants.DUST_UNIT - mediaHolder.media
		val battery = min(leftToFull.toDouble(), args.getPositiveDoubleUnderInclusive(1, 200000.0, argc))

		return SpellAction.Result(Spell(sacrifice, (battery * MediaConstants.DUST_UNIT).toInt(), stack.stack), MediaConstants.CRYSTAL_UNIT + (battery * MediaConstants.DUST_UNIT).toInt(), listOf(ParticleSpray.cloud(sacrifice.eyePos, 1.0)))
	}

	private data class Spell(val sacrifice: Entity, val battery: Int, val stack: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			if (sacrifice is VillagerEntity) {
				sacrifice.villagerData = sacrifice.villagerData.withLevel(sacrifice.villagerData.level - 1)
				sacrifice.experience = VillagerData.getLowerLevelExperience(sacrifice.villagerData.level)
			}

			val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(stack)!!
			hexHolder.writeHex(hexHolder.getHex(env.world) ?: listOf(), null, IXplatAbstractions.INSTANCE.findMediaHolder(stack)!!.media + battery)

			if (env.castingEntity is ServerPlayerEntity)
				HexicalAdvancements.RELOAD_LAMP.trigger(env.castingEntity as ServerPlayerEntity)
		}
	}
}