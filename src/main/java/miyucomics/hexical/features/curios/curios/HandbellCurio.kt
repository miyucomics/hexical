package miyucomics.hexical.features.curios.curios

import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.curios.CurioItem
import miyucomics.hexical.inits.HexicalSounds
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

object HandbellCurio : CurioItem() {
	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		world.playSound(null, user.x, user.y, user.z, HexicalSounds.HANDBELL_CHIMES, SoundCategory.MASTER, 1f, 0.5f + HexicalMain.RANDOM.nextFloat() * 0.7f)
		return TypedActionResult.success(user.getStackInHand(hand))
	}

	override fun postUse(user: ServerPlayerEntity, item: ItemStack, hand: Hand, world: ServerWorld, stack: List<Iota>) {
		world.playSound(null, user.x, user.y, user.z, HexicalSounds.HANDBELL_CHIMES, SoundCategory.MASTER, 1f, 0.9f + HexicalMain.RANDOM.nextFloat() * 0.2f)
	}
}