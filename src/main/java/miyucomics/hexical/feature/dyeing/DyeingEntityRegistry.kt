package miyucomics.hexical.feature.dyeing

import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.common.lib.HexItems
import miyucomics.hexical.feature.specklike.FigureSpecklike
import miyucomics.hexical.misc.InitHook
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.mob.ShulkerEntity
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.item.ItemStack
import java.util.*

object DyeingEntityRegistry : InitHook() {
	private val handlers = mutableListOf<DyeEntityHandler<*>>()
	private fun <T> notUncolored(): (T, DyeOption) -> Boolean = { _, dye -> dye != DyeOption.UNCOLORED }

	override fun init() {
		register(dyeHandler<ItemEntity>(
			color = { DyeingUtils.getDye(it.stack.item) },
			canDye = { entity, dye -> DyeingUtils.getResult(entity.stack, dye) != null },
			dye = { entity, dye -> entity.stack = DyeingUtils.getResult(entity.stack, dye)!! }
		))

		register(dyeHandler<CatEntity>(
			color = { DyeOption.fromDyeColor(it.collarColor) },
			canDye = notUncolored(),
			dye = { entity, dye -> entity.collarColor = dye.dye!! }
		))

		register(dyeHandler<WolfEntity>(
			color = { DyeOption.fromDyeColor(it.collarColor) },
			canDye = notUncolored(),
			dye = { entity, dye -> entity.collarColor = dye.dye!! }
		))

		register(dyeHandler<SheepEntity>(
			color = { DyeOption.fromDyeColor(it.color) },
			canDye = notUncolored(),
			dye = { entity, dye -> entity.color = dye.dye!! }
		))

		register(dyeHandler<ShulkerEntity>(
			color = { DyeOption.fromDyeColor(it.variant.orElse(null)) },
			dye = { entity, dye -> entity.variant = Optional.ofNullable(dye.dye) }
		))

		// TODO: figure out how to coerce a dye pigment back into a pigment efficiently
		register(dyeHandler<FigureSpecklike>(
			canDye = notUncolored(),
			dye = { e, dye -> e.setPigment(FrozenPigment(ItemStack(HexItems.DYE_PIGMENTS[dye.dye]!!), UUID.randomUUID())) }
		))
	}

	fun register(handler: DyeEntityHandler<*>) { handlers += handler }
	fun resolve(entity: Entity, dye: DyeOption) = handlers.firstOrNull { it.appliesTo(entity, dye) }
	fun getColor(entity: Entity) = handlers.firstNotNullOfOrNull { it.colorOf(entity) }
}