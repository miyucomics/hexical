package miyucomics.hexical.features.curios

import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.features.curios.curios.BaseCurio
import miyucomics.hexical.features.curios.curios.CompassCurio
import miyucomics.hexical.features.curios.curios.FluteCurio
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

abstract class CurioItem : Item(Settings().maxCount(1)) {
	open fun postUse(user: ServerPlayerEntity, stack: ItemStack, world: ServerWorld) {}
	open fun postWrite(stack: ItemStack, iota: Iota) {}

	companion object {
		private val specialCurios = mapOf(
			"compass" to CompassCurio,
			"flute" to FluteCurio
		)

		fun getCurioFromName(name: String): CurioItem {
			if (specialCurios.containsKey(name))
				return specialCurios[name]!!
			return BaseCurio()
		}
	}
}