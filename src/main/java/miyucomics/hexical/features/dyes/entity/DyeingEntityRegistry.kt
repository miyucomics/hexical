package miyucomics.hexical.features.dyes.entity

import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.common.lib.HexItems
import miyucomics.hexical.features.dyes.DyeOption
import miyucomics.hexical.features.dyes.DyeingUtils
import miyucomics.hexical.features.specklikes.PigmentedSpecklike
import miyucomics.hexical.misc.InitHook
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.mob.ShulkerEntity
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import java.util.*

object DyeingEntityRegistry : InitHook() {
	private val handlers = mutableListOf<DyeEntityHandler<*>>()

	override fun init() {
		register(object : DyeEntityHandler<ItemEntity>(ItemEntity::class.java) {
			override fun canAffectEntity(entity: Entity, dye: DyeOption): Boolean {
				if (!super.canAffectEntity(entity, dye))
					return false
				val stack = (entity as ItemEntity).stack
				if (DyeingUtils.getRecipe(entity.world as ServerWorld, stack, dye) != null)
					return true
				if (stack.item is BlockItem && DyeingUtils.getRecipe(entity.world as ServerWorld, (stack.item as BlockItem).block.defaultState, dye) != null)
					return true
				return false
			}

			override fun affect(entity: ItemEntity, dye: DyeOption) {
				val stack = entity.stack.copyAndEmpty()
				val itemRecipe = DyeingUtils.getRecipe(entity.world as ServerWorld, stack, dye)
				if (itemRecipe != null) {
					entity.stack = itemRecipe.output.copyWithCount(stack.count)
					return
				}

				if (stack.item is BlockItem) {
					val recipe = DyeingUtils.getRecipe(entity.world as ServerWorld, (stack.item as BlockItem).block.defaultState, dye)!!
					entity.stack = ItemStack(recipe.output.block.asItem(), stack.count)
					return
				}
			}
		})

		register(object : DyeEntityHandler<CatEntity>(CatEntity::class.java) {
			override fun canAffectEntity(entity: Entity, dye: DyeOption) = dye != DyeOption.UNCOLORED
			override fun affect(entity: CatEntity, dye: DyeOption) { entity.collarColor = dye.dyeColor!! }
		})

		register(object : DyeEntityHandler<WolfEntity>(WolfEntity::class.java) {
			override fun canAffectEntity(entity: Entity, dye: DyeOption) = dye != DyeOption.UNCOLORED
			override fun affect(entity: WolfEntity, dye: DyeOption) { entity.collarColor = dye.dyeColor!! }
		})

		register(object : DyeEntityHandler<SheepEntity>(SheepEntity::class.java) {
			override fun canAffectEntity(entity: Entity, dye: DyeOption) = dye != DyeOption.UNCOLORED
			override fun affect(entity: SheepEntity, dye: DyeOption) { entity.color = dye.dyeColor!! }
		})

		register(object : DyeEntityHandler<PigmentedSpecklike>(PigmentedSpecklike::class.java) {
			override fun canAffectEntity(entity: Entity, dye: DyeOption) = dye != DyeOption.UNCOLORED
			override fun affect(entity: PigmentedSpecklike, dye: DyeOption) { entity.setPigment(FrozenPigment(ItemStack(HexItems.DYE_PIGMENTS[dye.dyeColor]!!), UUID.randomUUID())) }
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