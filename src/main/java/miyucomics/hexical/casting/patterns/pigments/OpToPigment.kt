package miyucomics.hexical.casting.patterns.pigments

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.item.PigmentItem
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.common.lib.HexItems
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.casting.iota.*
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

class OpToPigment : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val colorizer = when (args[0]) {
			is DyeIota -> FrozenPigment(ItemStack(HexItems.DYE_PIGMENTS[args.getTrueDye(0, argc)]), env.castingEntity.uuid)
			is EntityIota -> {
				when (val entity = args.getEntity(0, argc)) {
					is PlayerEntity -> IXplatAbstractions.INSTANCE.getPigment(entity)
					is ItemEntity -> {
						val stack = args.getItemStack(0, argc)
						if (stack.item is PigmentItem)
							FrozenPigment(stack, env.castingEntity.uuid)
						else
							null
					}
					else -> null
				}
			}
			is IdentifierIota -> {
				val item = Registry.ITEM.get(args.getIdentifier(0, argc))
				if (item is ColorizerItem)
					FrozenPigment(ItemStack(item), env.caster.uuid)
				else
					null
			}
			else -> null
		}

		if (colorizer == null)
			throw MishapInvalidIota.of(args[0], 0, "to_pigment")

		return listOf(PigmentIota(colorizer))
	}
}