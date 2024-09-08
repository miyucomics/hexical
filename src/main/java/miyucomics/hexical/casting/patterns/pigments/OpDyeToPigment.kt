package miyucomics.hexical.casting.patterns.pigments

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.common.lib.HexItems
import miyucomics.hexical.casting.iota.PigmentIota
import miyucomics.hexical.casting.iota.getTrueDye
import net.minecraft.item.ItemStack

class OpDyeToPigment : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext) = listOf(PigmentIota(FrozenColorizer(ItemStack(HexItems.DYE_COLORIZERS[args.getTrueDye(0, argc)]), ctx.caster.uuid)))
}