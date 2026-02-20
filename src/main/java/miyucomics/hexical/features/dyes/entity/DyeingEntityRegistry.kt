package miyucomics.hexical.features.dyes.entity

import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.common.lib.HexItems
import miyucomics.hexical.features.dyes.DyeOption
import miyucomics.hexical.features.dyes.DyeingUtils
import miyucomics.hexical.features.specklikes.FigureSpecklike
import miyucomics.hexical.misc.InitHook
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.mob.ShulkerEntity
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import java.util.*

object DyeingEntityRegistry : InitHook() {
	private val handlers = mutableListOf<DyeEntityHandler<*>>()

	override fun init() {
		register(object : DyeEntityHandler<ItemEntity>(ItemEntity::class.java) {
			override fun extraConditions(entity: ItemEntity, dye: DyeOption) = DyeingUtils.getResult(entity.stack, dye) != null

			override fun affect(entity: ItemEntity, dye: DyeOption) {
				val stack = entity.stack.copyAndEmpty()
				entity.stack = DyeingUtils.getResult(stack, dye)
			}
		})

		register(object : DyeEntityHandler<ItemEntity>(ItemEntity::class.java) {
			override fun extraConditions(entity: ItemEntity, dye: DyeOption): Boolean {
				val stack = entity.stack
				return stack.item is BlockItem && DyeingUtils.getResult((stack.item as BlockItem).block, dye) != null
			}

			override fun affect(entity: ItemEntity, dye: DyeOption) {
				val stack = entity.stack
				val result = DyeingUtils.getResult((stack.item as BlockItem).block, dye)!!
				entity.stack = ItemStack(result.asItem(), stack.count)
			}
		})

		register(object : DyeEntityHandler<CatEntity>(CatEntity::class.java) {
			override fun extraConditions(entity: CatEntity, dye: DyeOption): Boolean = dye != DyeOption.UNCOLORED
			override fun affect(entity: CatEntity, dye: DyeOption) { entity.collarColor = dye.dyeColor!! }
		})

		register(object : DyeEntityHandler<WolfEntity>(WolfEntity::class.java) {
			override fun extraConditions(entity: WolfEntity, dye: DyeOption): Boolean = dye != DyeOption.UNCOLORED
			override fun affect(entity: WolfEntity, dye: DyeOption) { entity.collarColor = dye.dyeColor!! }
		})

		register(object : DyeEntityHandler<SheepEntity>(SheepEntity::class.java) {
			override fun extraConditions(entity: SheepEntity, dye: DyeOption): Boolean = dye != DyeOption.UNCOLORED
			override fun affect(entity: SheepEntity, dye: DyeOption) { entity.color = dye.dyeColor!! }
		})

		register(object : DyeEntityHandler<FigureSpecklike>(FigureSpecklike::class.java) {
			override fun extraConditions(entity: FigureSpecklike, dye: DyeOption): Boolean = dye != DyeOption.UNCOLORED
			override fun affect(entity: FigureSpecklike, dye: DyeOption) { entity.setPigment(FrozenPigment(ItemStack(HexItems.DYE_PIGMENTS[dye.dyeColor]!!), UUID.randomUUID())) }
		})

		register(object : DyeEntityHandler<ShulkerEntity>(ShulkerEntity::class.java) {
			override fun affect(entity: ShulkerEntity, dye: DyeOption) { entity.variant = dye.dyeColor?.let { Optional.of(it) } ?: Optional.empty() }
		})
	}

	fun register(handler: DyeEntityHandler<*>) {
		handlers += handler
	}

	fun resolve(entity: Entity, dye: DyeOption) = handlers.firstOrNull { it.canAffectEntity(entity, dye) }
}