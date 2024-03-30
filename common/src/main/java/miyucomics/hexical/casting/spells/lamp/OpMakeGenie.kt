package miyucomics.hexical.casting.spells.lamp

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.mod.HexConfig
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapAlreadyBrainswept
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.utils.extractMedia
import at.petrak.hexcasting.common.misc.Brainsweeping
import at.petrak.hexcasting.ktxt.tellWitnessesThatIWasMurdered
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.items.LampItem
import miyucomics.hexical.registry.HexicalAdvancements
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand

class OpMakeGenie : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val sacrifice = args.getVillager(0, argc)
		val battery = args.getItemEntity(1, argc)
		ctx.assertEntityInRange(sacrifice)
		ctx.assertEntityInRange(battery)
		if (Brainsweeping.isBrainswept(sacrifice))
			throw MishapAlreadyBrainswept(sacrifice)
		val (handStack, hand) = ctx.getHeldItemToOperateOn {
			val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(it)
			it.item is LampItem && hexHolder != null && !hexHolder.hasHex()
		}
		val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(handStack)
		if (handStack.item !is LampItem)
			throw MishapBadOffhandItem.of(handStack, hand, "lamp")
		if (hexHolder == null || hexHolder.hasHex())
			throw MishapBadOffhandItem.of(handStack, hand, "lamp")
		if (sacrifice.villagerData.level < 3)
			throw MishapBadEntity.of(sacrifice, "lamp_flay")
		return Triple(Spell(sacrifice, battery, hand), MediaConstants.CRYSTAL_UNIT * 5, listOf(ParticleSpray.cloud(sacrifice.pos, 1.0), ParticleSpray.cloud(battery.pos, 1.0)))
	}

	private data class Spell(val sacrifice: VillagerEntity, val battery: ItemEntity, val hand: Hand) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val master = sacrifice.villagerData.level >= 5
			Brainsweeping.brainsweep(sacrifice)
			if (HexConfig.server().doVillagersTakeOffenseAtMindMurder())
				sacrifice.tellWitnessesThatIWasMurdered(ctx.caster)
			if (master)
				ctx.caster.setStackInHand(hand, ItemStack(HexicalItems.ARCH_LAMP_ITEM))
			HexicalAdvancements.RELOAD_LAMP.trigger(ctx.caster)
			val lamp = ctx.caster.getStackInHand(hand)
			val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(lamp)
			if (hexHolder != null && !hexHolder.hasHex() && battery.isAlive) {
				val media = extractMedia(battery.stack, drainForBatteries = true)
				if (media > 0)
					IXplatAbstractions.INSTANCE.findHexHolder(lamp)?.writeHex(listOf(), media)
				battery.kill()
			}
		}
	}
}