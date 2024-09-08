package miyucomics.hexical.casting.patterns.pigments

import at.petrak.hexcasting.api.item.ColorizerItem
import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import at.petrak.hexcasting.common.lib.HexItems
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.casting.iota.DyeIota
import miyucomics.hexical.casting.iota.PigmentIota
import miyucomics.hexical.casting.iota.getTrueDye
import miyucomics.hexical.casting.patterns.getters.getItemStack
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

class OpToPigment : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val colorizer = when (args[0]) {
			is DyeIota -> FrozenColorizer(ItemStack(HexItems.DYE_COLORIZERS[args.getTrueDye(0, argc)]), ctx.caster.uuid)
			is EntityIota -> {
				when (val entity = args.getEntity(0, argc)) {
					is PlayerEntity -> IXplatAbstractions.INSTANCE.getColorizer(entity)
					is ItemEntity -> {
						val stack = args.getItemStack(0, argc)
						if (stack.item is ColorizerItem)
							FrozenColorizer(stack, ctx.caster.uuid)
						else
							null
					}
					else -> null
				}
			}
			else -> null
		}

		if (colorizer == null)
			throw MishapInvalidIota.of(args[0], 0, "to_pigment")

		return listOf(PigmentIota(colorizer))
	}
}