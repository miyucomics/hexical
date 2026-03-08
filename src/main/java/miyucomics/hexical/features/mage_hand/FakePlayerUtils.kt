package miyucomics.hexical.features.mage_hand

import com.mojang.authlib.GameProfile
import net.fabricmc.fabric.api.entity.FakePlayer
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityPose
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

object FakePlayerUtils {
	private val blockAgenda: List<(ServerWorld, FakePlayer, BlockState, BlockHitResult) -> ActionResult> = listOf(
		{ world, player, _, _ ->
			val result = player.getStackInHand(Hand.MAIN_HAND).use(world, player, Hand.MAIN_HAND)
			if (result.result.isAccepted)
				player.giveItemStack(result.value)
			result.result
		},
		{ world, player, _, hitresult -> UseBlockCallback.EVENT.invoker().interact(player, world, Hand.MAIN_HAND, hitresult) },
		{ _, player, _, hitresult -> player.getStackInHand(Hand.MAIN_HAND).useOnBlock(ItemUsageContext(player, Hand.MAIN_HAND, hitresult)) },
		{ world, player, blockstate, hitresult -> blockstate.onUse(world, player, Hand.MAIN_HAND, hitresult) },
	)

	private val entityAgenda: List<(ServerWorld, FakePlayer, Entity) -> ActionResult> = listOf(
		{ world, player, _ ->
			val result = player.getStackInHand(Hand.MAIN_HAND).use(world, player, Hand.MAIN_HAND)
			if (result.result.isAccepted)
				player.giveItemStack(result.value)
			result.result
		},
		{ world, player, target -> UseEntityCallback.EVENT.invoker().interact(player, world, Hand.MAIN_HAND, target, null) },
		{ _, player, target -> if (target is LivingEntity) player.getStackInHand(Hand.MAIN_HAND).useOnEntity(player, target, Hand.MAIN_HAND) else ActionResult.PASS },
		{ _, player, target -> target.interact(player, Hand.MAIN_HAND) },
	)

	private fun createFakePlayer(world: ServerWorld, x: Double, y: Double, z: Double, stack: ItemStack, owner: ServerPlayerEntity?, sneak: Boolean): FakePlayer {
		val fakePlayer = if (owner == null) FakePlayer.get(world) else FakePlayer.get(world, GameProfile(owner.uuid, "[Hexical Mage Hand]"))
		fakePlayer.setPos(x, y - fakePlayer.getEyeHeight(if (sneak) EntityPose.CROUCHING else EntityPose.STANDING), z)
		fakePlayer.isSneaking = sneak
		fakePlayer.setStackInHand(Hand.MAIN_HAND, stack)
		return fakePlayer
	}

	fun useItemAt(world: ServerWorld, stack: ItemStack, owner: ServerPlayerEntity?, position: BlockPos, sneak: Boolean): List<ItemStack> {
		val center = Vec3d.ofCenter(position)
		val fakePlayer = createFakePlayer(world, center.x, center.y, center.z, stack, owner, sneak)
		val hitresult = BlockHitResult(center, Direction.UP, position, false)
		val block = world.getBlockState(position)
		for (task in blockAgenda) {
			val result = task(world, fakePlayer, block, hitresult)
			if (result.isAccepted)
				break
		}

		return fakePlayer.inventory.main + fakePlayer.inventory.armor + fakePlayer.inventory.offHand
	}

	fun useItemOnEntity(world: ServerWorld, stack: ItemStack, owner: ServerPlayerEntity?, entity: Entity, sneak: Boolean): List<ItemStack> {
		val fakePlayer = createFakePlayer(world, entity.x, entity.y, entity.z, stack, owner, sneak)
		for (task in entityAgenda) {
			val result = task(world, fakePlayer, entity)
			if (result.isAccepted)
				break
		}

		return fakePlayer.inventory.main + fakePlayer.inventory.armor + fakePlayer.inventory.offHand
	}
}