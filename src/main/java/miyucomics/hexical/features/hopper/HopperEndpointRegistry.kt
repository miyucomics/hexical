package miyucomics.hexical.features.hopper

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import miyucomics.hexical.features.hopper.targets.*
import miyucomics.hexical.misc.InitHook
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.vehicle.ChestBoatEntity
import net.minecraft.entity.vehicle.ChestMinecartEntity
import net.minecraft.entity.vehicle.HopperMinecartEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import kotlin.math.abs

object HopperEndpointRegistry : InitHook() {
	private val resolvers = mutableListOf<HopperEndpointResolver>()

	override fun init() {
		register { iota, env, slot ->
			val caster = env.castingEntity
			if (iota is EntityIota && iota.entity == caster && caster is ServerPlayerEntity && slot == -1)
				return@register WristpocketEndpoint(caster)
			null
		}

		register { iota, env, slot ->
			val caster = env.castingEntity
			if (iota is EntityIota && iota.entity == caster && caster is ServerPlayerEntity)
				return@register getSlottedInventory(caster.inventory, slot, iota)
			null
		}

		registerInventoryEntity<ArmorStandEntity> { ArmorStandInventory(it.armorItems as DefaultedList<ItemStack>, it.handItems as DefaultedList<ItemStack>) }
		registerInventoryEntity<ChestBoatEntity> { ListBackedInventory(it.inventory) }
		registerInventoryEntity<ChestMinecartEntity> { ListBackedInventory(it.inventory, size = 9) }
		registerInventoryEntity<HopperMinecartEntity> { ListBackedInventory(it.inventory, size = 9) }
		registerEntityEndpoint<ItemEntity> { DroppedItemEndpoint(it) }
		registerEntityEndpoint<ItemFrameEntity> { ItemFrameEndpoint(it) }

		register { iota, env, slot ->
			if (iota !is Vec3Iota)
				return@register null
			val vec = iota.vec3
			val blockPos = BlockPos.ofFloored(vec)
			env.assertPosInRange(blockPos)
			val inventory = env.world.getBlockEntity(blockPos)
			if (inventory is SidedInventory) {
				if (slot != null)
					return@register SlottedInventoryEndpoint(inventory, slot, iota)
				val dx = vec.x - (blockPos.x + 0.5)
				val dy = vec.y - (blockPos.y + 0.5)
				val dz = vec.z - (blockPos.z + 0.5)
				val ax = abs(dx)
				val ay = abs(dy)
				val az = abs(dz)
				val threshold = 0.05
				val direction = when {
					ax < threshold && ay < threshold && az < threshold -> null
					ax >= ay && ax >= az -> if (dx > 0) Direction.EAST else Direction.WEST
					ay >= ax && ay >= az -> if (dy > 0) Direction.UP else Direction.DOWN
					else -> if (dz > 0) Direction.SOUTH else Direction.NORTH
				}

				return@register if (direction != null)
					SidedInventoryEndpoint(inventory, direction)
				else
					InventoryEndpoint(inventory)
			}
			null
		}

		register { iota, env, slot ->
			if (iota !is Vec3Iota)
				return@register null
			val blockPos = BlockPos.ofFloored(iota.vec3)
			env.assertPosInRange(blockPos)
			val inventory = env.world.getBlockEntity(blockPos)
			if (inventory is Inventory)
				return@register getSlottedInventory(inventory, slot, iota)
			null
		}

		register { iota, env, slot ->
			if (iota is Vec3Iota) {
				env.assertVecInRange(iota.vec3)
				return@register DispenseEndpoint(iota.vec3, env.world)
			}
			null
		}

		register { iota, env, slot ->
			if (iota is NullIota && env.castingEntity is PlayerEntity)
				return@register getSlottedInventory((env.castingEntity as PlayerEntity).enderChestInventory, slot, iota)
			null
		}
	}

	fun register(resolver: HopperEndpointResolver) {
		resolvers += resolver
	}

	fun resolve(iota: Iota, env: CastingEnvironment, slot: Int?): HopperEndpoint? {
		return resolvers.firstNotNullOfOrNull { it.resolve(iota, env, slot) }
	}

	fun getSlottedInventory(inventory: Inventory, slot: Int?, iota: Iota): HopperEndpoint {
		if (slot == null)
			return InventoryEndpoint(inventory)
		return SlottedInventoryEndpoint(inventory, slot, iota)
	}

	private inline fun <reified T : Entity> registerInventoryEntity(crossinline getInventory: (T) -> Inventory) {
		register { iota, env, slot ->
			val entity = (iota as? EntityIota)?.entity as? T ?: return@register null
			env.assertEntityInRange(entity)
			getSlottedInventory(getInventory(entity), slot, iota)
		}
	}

	private inline fun <reified T : Entity> registerEntityEndpoint(crossinline endpoint: (T) -> HopperEndpoint) {
		register { iota, env, _ ->
			val entity = (iota as? EntityIota)?.entity as? T ?: return@register null
			env.assertEntityInRange(entity)
			endpoint(entity)
		}
	}
}