package miyucomics.hexical.casting.patterns.dye

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.common.lib.HexItems
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.casting.iota.getTrueDye
import net.minecraft.item.ItemStack

class OpMimicDye : ConstMediaAction {
	override val argc = 1
	override val mediaCost = MediaConstants.DUST_UNIT
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val color = args.getTrueDye(0, argc)
		IXplatAbstractions.INSTANCE.setColorizer(ctx.caster, FrozenColorizer(ItemStack(HexItems.DYE_COLORIZERS[color]), ctx.caster.uuid))
		return listOf()
	}
}