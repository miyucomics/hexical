package miyucomics.hexical.features.curios

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.inits.Hook
import net.minecraft.client.item.CompassAnglePredicateProvider
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.GlobalPos

object CompassCurioSpinner : Hook() {
	override fun registerCallbacks() {
		ModelPredicateProviderRegistry.register(HexicalItems.CURIO_COMPASS, Identifier("angle"), CompassAnglePredicateProvider(
			CompassAnglePredicateProvider.CompassTarget { world: ClientWorld, stack: ItemStack, player: Entity ->
				if (!stack.hasNbt() || !stack.nbt?.contains("needle")!!)
					return@CompassTarget null
				val needle = stack.nbt!!.getIntArray("needle")
				return@CompassTarget GlobalPos.create(player.world.registryKey, BlockPos(needle[0], needle[1], needle[2]))
			}
		))
	}

	fun saveVectorForTheClient(stack: ItemStack, iota: Iota) {
		if (stack.isOf(HexicalItems.CURIO_COMPASS) && iota is Vec3Iota)
			stack.orCreateNbt.putIntArray("needle", listOf(iota.vec3.x.toInt(), iota.vec3.y.toInt(), iota.vec3.z.toInt()))
	}
}