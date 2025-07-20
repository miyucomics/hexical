package miyucomics.hexical.features.curios.curios

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import miyucomics.hexical.features.curios.CurioItem
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand

object CompassCurio : CurioItem() {
	override fun postUse(user: ServerPlayerEntity, item: ItemStack, hand: Hand, world: ServerWorld, stack: List<Iota>) {
		val location = stack.lastOrNull()
		if (location !is Vec3Iota) {
			item.orCreateNbt.remove("needle")
			return
		}
		item.orCreateNbt.putIntArray("needle", listOf(location.vec3.x.toInt(), location.vec3.y.toInt(), location.vec3.z.toInt()))
	}
}