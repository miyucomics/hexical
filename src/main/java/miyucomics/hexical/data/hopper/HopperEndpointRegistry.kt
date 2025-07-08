package miyucomics.hexical.data.hopper

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import miyucomics.hexical.data.hopper.targets.*
import net.minecraft.command.argument.BlockPosArgumentType.blockPos
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import kotlin.math.abs

object HopperEndpointRegistry {
	private val resolvers = mutableListOf<HopperEndpointResolver>()

	fun init() {
		register { iota, env ->
			val caster = env.castingEntity
			if (iota is EntityIota && iota.entity == caster && caster is ServerPlayerEntity)
				return@register InventoryEndpoint(caster.inventory)
			null
		}

		register { iota, env ->
			if (iota is EntityIota && iota.entity is ItemEntity) {
				env.assertEntityInRange(iota.entity)
				return@register DroppedItemEndpoint(iota.entity as ItemEntity)
			}
			null
		}

		register { iota, env ->
			if (iota is EntityIota && iota.entity is ItemFrameEntity) {
				env.assertEntityInRange(iota.entity)
				return@register ItemFrameEndpoint(iota.entity as ItemFrameEntity)
			}
			null
		}

		register { iota, env ->
			if (iota !is Vec3Iota)
				return@register null
			val vec = iota.vec3
			val blockPos = BlockPos.ofFloored(vec)
			env.assertPosInRange(blockPos)
			val inventory = env.world.getBlockEntity(blockPos)
			if (inventory is SidedInventory) {
				val dx = vec.x - (blockPos.x + 0.5)
				val dy = vec.y - (blockPos.y + 0.5)
				val dz = vec.z - (blockPos.z + 0.5)
				val ax = abs(dx)
				val ay = abs(dy)
				val az = abs(dz)
				val threshold = 0.05
				val direction =
					if (ax < threshold && ay < threshold && az < threshold)
						null
					else if (ax >= ay && ax >= az)
						if (dx > 0) Direction.EAST else Direction.WEST
					else if (ay >= ax && ay >= az)
						if (dy > 0) Direction.UP else Direction.DOWN
					else
						if (dz > 0) Direction.SOUTH else Direction.NORTH

				return@register if (direction != null)
					SidedInventoryEndpoint(inventory, direction)
				else
					InventoryEndpoint(inventory)
			}
			null
		}

		register { iota, env ->
			if (iota !is Vec3Iota)
				return@register null
			val blockPos = BlockPos.ofFloored(iota.vec3)
			env.assertPosInRange(blockPos)
			val inventory = env.world.getBlockEntity(blockPos)
			if (inventory is Inventory)
				return@register InventoryEndpoint(inventory)
			null
		}

		register { iota, env ->
			if (iota is Vec3Iota) {
				env.assertVecInRange(iota.vec3)
				return@register DispenseEndpoint(iota.vec3, env.world)
			}
			null
		}

		register { iota, env ->
			if (iota is NullIota && env.castingEntity is PlayerEntity)
				return@register InventoryEndpoint((env.castingEntity as PlayerEntity).enderChestInventory)
			null
		}
	}

	fun register(resolver: HopperEndpointResolver) {
		resolvers += resolver
	}

	fun resolve(iota: Iota, env: CastingEnvironment): HopperEndpoint? {
		return resolvers.firstNotNullOfOrNull { it.resolve(iota, env) }
	}
}