package miyucomics.hexical.features.curios.curios

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.lib.HexSounds
import miyucomics.hexical.features.curios.CurioItem
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand

class BaseCurio : CurioItem() {
	override fun postCharmCast(user: ServerPlayerEntity, item: ItemStack, hand: Hand, world: ServerWorld, stack: List<Iota>) {
		user.swingHand(hand)
		world.playSound(null, user.x, user.y, user.z, HexSounds.CAST_HERMES, SoundCategory.MASTER, 0.25f, 1f)
	}
}