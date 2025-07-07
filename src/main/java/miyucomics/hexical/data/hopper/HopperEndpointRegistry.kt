package miyucomics.hexical.data.hopper

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import miyucomics.hexical.data.hopper.targets.BlockInventoryEndpoint
import miyucomics.hexical.data.hopper.targets.DroppedItemEndpoint
import miyucomics.hexical.data.hopper.targets.ItemFrameEndpoint
import miyucomics.hexical.data.hopper.targets.PlayerInventoryEndpoint
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.inventory.Inventory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

object HopperEndpointRegistry {
	private val resolvers = mutableListOf<HopperEndpointResolver>()

	fun init() {
		register { iota, env ->
			val caster = env.castingEntity
			if (iota is EntityIota && iota.entity == caster && caster is ServerPlayerEntity)
				PlayerInventoryEndpoint(caster)
			else null
		}

		register { iota, env ->
			if (iota is EntityIota && iota.entity is ItemFrameEntity)
				ItemFrameEndpoint(iota.entity as ItemFrameEntity)
			else null
		}

		register { iota, env ->
			if (iota !is Vec3Iota)
				return@register null
			val inventory = env.world.getBlockEntity(BlockPos.ofFloored(iota.vec3))
			if (inventory is Inventory)
				return@register BlockInventoryEndpoint(inventory)
			DroppedItemEndpoint(iota.vec3, env.world)
		}
	}

	fun register(resolver: HopperEndpointResolver) {
		resolvers += resolver
	}

	fun resolve(iota: Iota, env: CastingEnvironment): HopperEndpoint? {
		return resolvers.firstNotNullOfOrNull { it.resolve(iota, env) }
	}
}