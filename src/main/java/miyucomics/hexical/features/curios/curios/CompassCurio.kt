package miyucomics.hexical.features.curios.curios

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import miyucomics.hexical.features.curios.CurioItem
import net.minecraft.item.ItemStack

object CompassCurio : CurioItem() {
	override fun postWrite(stack: ItemStack, iota: Iota) {
		if (iota is Vec3Iota)
			stack.orCreateNbt.putIntArray("needle", listOf(iota.vec3.x.toInt(), iota.vec3.y.toInt(), iota.vec3.z.toInt()))
		else
			stack.orCreateNbt.remove("needle")
	}
}