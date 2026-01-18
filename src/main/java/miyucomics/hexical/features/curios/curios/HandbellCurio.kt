package miyucomics.hexical.features.curios.curios

import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.curios.CurioItem
import miyucomics.hexical.inits.HexicalSounds
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

object HandbellCurio : CurioItem() {
	@JvmField
	val CHANNEL = HexicalMain.id("handbell")

	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		if (world.isClient)
			return TypedActionResult.pass(user.getStackInHand(hand))
		playSound(world as ServerWorld, user as ServerPlayerEntity)
		return TypedActionResult.pass(user.getStackInHand(hand))
	}

	override fun postCharmCast(user: ServerPlayerEntity, item: ItemStack, hand: Hand, world: ServerWorld, stack: List<Iota>) {
		playSound(world, user)
	}

	private fun playSound(world: ServerWorld, user: ServerPlayerEntity) {
		world.players.forEach {
			world.sendToPlayerIfNearby(it, false, user.x, user.y, user.z, ServerPlayNetworking.createS2CPacket(CHANNEL, PacketByteBufs.create().apply { writeUuid(user.uuid) }))
		}
		world.playSound(null, user.x, user.y, user.z, HexicalSounds.HANDBELL_CHIMES, SoundCategory.MASTER, 1f, 0.8f + HexicalMain.RANDOM.nextFloat() * 0.3f)
	}
}