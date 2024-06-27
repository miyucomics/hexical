package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.mod.HexConfig
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapAlreadyBrainswept
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.common.misc.Brainsweeping
import at.petrak.hexcasting.ktxt.tellWitnessesThatIWasMurdered
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.items.ArchLampItem
import miyucomics.hexical.registry.HexicalAdvancements
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.village.VillageGossipType
import net.minecraft.village.VillagerData

class OpReloadLamp : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val sacrifice = args.getVillager(0, argc)
		val battery = args.getDouble(1, argc)

		ctx.assertEntityInRange(sacrifice)
		if (Brainsweeping.isBrainswept(sacrifice))
			throw MishapBadEntity.of(sacrifice, "has_brain")
		if (sacrifice.villagerData.level < 3)
			throw MishapBadEntity.of(sacrifice, "smart_villager")

		val stack = ctx.caster.getStackInHand(ctx.otherHand)
		if (stack.isOf(HexicalItems.LAMP_ITEM) || stack.isOf(HexicalItems.ARCH_LAMP_ITEM))
			return Triple(Spell(sacrifice, (battery * MediaConstants.DUST_UNIT).toInt()), MediaConstants.CRYSTAL_UNIT + (battery * MediaConstants.DUST_UNIT).toInt(), listOf(ParticleSpray.cloud(sacrifice.eyePos, 1.0)))

		throw MishapBadOffhandItem.of(stack, ctx.otherHand, "lamp")
	}

	private data class Spell(val sacrifice: VillagerEntity, val battery: Int) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val lamp = ctx.caster.getStackInHand(ctx.otherHand)
			if (sacrifice.villagerData.level >= 5 && lamp.item !is ArchLampItem) {
				ctx.caster.setStackInHand(ctx.otherHand, ItemStack(HexicalItems.ARCH_LAMP_ITEM))
				Brainsweeping.brainsweep(sacrifice)
				if (HexConfig.server().doVillagersTakeOffenseAtMindMurder())
					sacrifice.tellWitnessesThatIWasMurdered(ctx.caster)
			} else {
				sacrifice.villagerData = sacrifice.villagerData.withLevel(sacrifice.villagerData.level - 1)
				sacrifice.experience = VillagerData.getLowerLevelExperience(sacrifice.villagerData.level)
				sacrifice.gossip.startGossip(ctx.caster.uuid, VillageGossipType.MAJOR_NEGATIVE, 20)
			}
			val newLamp = ctx.caster.getStackInHand(ctx.otherHand)
			val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(newLamp)!!
			val mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(lamp)!!
			hexHolder.writeHex(hexHolder.getHex(ctx.world)?: listOf(), mediaHolder.media + battery)
			HexicalAdvancements.RELOAD_LAMP.trigger(ctx.caster)
		}
	}
}