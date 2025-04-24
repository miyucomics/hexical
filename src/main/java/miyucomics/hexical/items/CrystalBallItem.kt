package miyucomics.hexical.items

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.utils.*
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.casting.environments.CrystalBallCastEnv
import miyucomics.hexical.casting.environments.CrystalBallException
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class CrystalBallItem : ItemPackagedHex(Settings().maxCount(1)) {
	var image: CastingImage = CastingImage()

	override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		val stack = player.getStackInHand(hand)

		if (stack.getBoolean("active")) {
			stack.putBoolean("active", false)
			stack.remove("continuation")
			stack.remove("image")
		} else {
			stack.putBoolean("active", true)
			val vm = CastingVM(CastingImage(), CrystalBallCastEnv(player as ServerPlayerEntity, stack, 3))
			try {
				vm.queueExecuteAndWrapIotas((stack.item as TchotchkeItem).getHex(stack, player.serverWorld)!!, player.serverWorld)
				stack.putBoolean("active", false)
				stack.remove("continuation")
				stack.remove("image")
			} catch (crystalBallData: CrystalBallException) {
				stack.putCompound("continuation", crystalBallData.continuation.serializeToNBT())
				stack.putCompound("image", crystalBallData.image.serializeToNbt())
			}
		}

		return TypedActionResult.success(stack)
	}

	override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, ticksLeft: Int, bl: Boolean) {
		if (!stack.getBoolean("active"))
			return

		val continuation = SpellContinuation.fromNBT(stack.getCompound("continuation")!!, world as ServerWorld)
		val image = CastingImage.loadFromNbt(stack.getCompound("image")!!, world)

		val vm = CastingVM(image, CrystalBallCastEnv(entity as ServerPlayerEntity, stack, 3, continuation))
		try {
			vm.queueExecuteAndWrapIotas((stack.item as TchotchkeItem).getHex(stack, world)!!, world)
			stack.putBoolean("active", false)
			stack.remove("continuation")
			stack.remove("image")
		} catch (crystalBallData: CrystalBallException) {
			stack.putCompound("continuation", crystalBallData.continuation.serializeToNBT())
			stack.putCompound("image", crystalBallData.image.serializeToNbt())
		}
	}

	override fun canDrawMediaFromInventory(stack: ItemStack?) = false
	override fun breakAfterDepletion() = false
	override fun cooldown() = 0
}