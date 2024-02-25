package miyucomics.hexical.casting.patterns.spells

import at.petrak.hexcasting.api.mod.HexConfig
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapAlreadyBrainswept
import at.petrak.hexcasting.api.spell.mishaps.MishapBadItem
import at.petrak.hexcasting.api.utils.extractMedia
import at.petrak.hexcasting.api.utils.isMediaItem
import at.petrak.hexcasting.common.misc.Brainsweeping
import at.petrak.hexcasting.ktxt.tellWitnessesThatIWasMurdered
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.util.Hand

class OpMakeGenie : SpellAction {
	override val argc = 2

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val sacrifice = args.getVillager(0, argc)
		val battery = args.getItemEntity(1, argc)
		ctx.assertEntityInRange(sacrifice)
		if (Brainsweeping.isBrainswept(sacrifice))
			throw MishapAlreadyBrainswept(sacrifice)
		ctx.assertEntityInRange(battery)
		if (!isMediaItem(battery.stack) || extractMedia(battery.stack, drainForBatteries = true, simulate = true) <= 0)
			throw MishapBadItem.of(battery, "media_for_battery")
		val (_, hand) = ctx.getHeldItemToOperateOn { true }
		return Triple(Spell(sacrifice, battery, hand), 0, listOf())
	}

	private data class Spell(val sacrifice: VillagerEntity, val battery: ItemEntity, val hand: Hand) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			Brainsweeping.brainsweep(sacrifice)
			if (HexConfig.server().doVillagersTakeOffenseAtMindMurder())
				sacrifice.tellWitnessesThatIWasMurdered(ctx.caster)
		}
	}
}
