package miyucomics.hexical.features.curios.curios

import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.features.curios.CurioItem
import miyucomics.hexical.inits.HexicalSounds
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand

object StaffCurio : CurioItem() {
	override fun postCharmCast(user: ServerPlayerEntity, item: ItemStack, hand: Hand, world: ServerWorld, stack: List<Iota>) {
		user.world.playSound(null, user.x, user.y, user.z, HexicalSounds.MAGIC_WOOSHES, SoundCategory.PLAYERS, 1f, 1f)
	}
}