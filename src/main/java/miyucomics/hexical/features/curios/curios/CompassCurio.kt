package miyucomics.hexical.features.curios.curios

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import miyucomics.hexical.features.curios.CurioItem
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.inits.InitHook
import net.minecraft.client.item.CompassAnglePredicateProvider
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.GlobalPos

object CompassCurio : CurioItem() {
	override fun postWrite(stack: ItemStack, iota: Iota) {
		if (iota is Vec3Iota)
			stack.orCreateNbt.putIntArray("needle", listOf(iota.vec3.x.toInt(), iota.vec3.y.toInt(), iota.vec3.z.toInt()))
		else
			stack.orCreateNbt.remove("needle")
	}
}