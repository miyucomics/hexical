package miyucomics.hexical.feature.pigment

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.item.PigmentItem
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.common.lib.HexItems
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.feature.dyeing.DyeIota
import miyucomics.hexical.feature.dyeing.getColoredDye
import miyucomics.hexpose.iotas.getItemType
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.DyeItem
import net.minecraft.item.ItemStack
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota
import ram.talia.moreiotas.api.getItemStack

object OpToPigment : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.castingEntity == null)
			throw MishapBadCaster()
		val caster = env.castingEntity as LivingEntity

		val colorizer = when (args[0]) {
			is DyeIota -> FrozenPigment(ItemStack(HexItems.DYE_PIGMENTS[args.getColoredDye(0, argc)]), caster.uuid)
			is EntityIota -> {
				when (val entity = args.getEntity(0, argc)) {
					is PlayerEntity -> IXplatAbstractions.INSTANCE.getPigment(entity)
					is ItemEntity -> {
						val item = args.getItemEntity(0, argc)
						env.assertEntityInRange(item)
						val stack = item.stack
						if (stack.item is PigmentItem)
							FrozenPigment(stack, caster.uuid)
						else
							null
					}
					else -> null
				}
			}
			is ItemTypeIota -> when (val item = args.getItemType(0, argc)) {
				is PigmentItem -> FrozenPigment(ItemStack(item), caster.uuid)
				is DyeItem -> FrozenPigment(ItemStack(HexItems.DYE_PIGMENTS[item.color]), caster.uuid)
				else -> null
			}
			is ItemStackIota -> {
				val stack = args.getItemStack(0, argc)
				when (stack.item) {
					is PigmentItem -> FrozenPigment(stack, caster.uuid)
					is DyeItem -> FrozenPigment(ItemStack(HexItems.DYE_PIGMENTS[(stack.item as DyeItem).color]), caster.uuid)
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