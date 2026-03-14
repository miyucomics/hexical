package miyucomics.hexical.features.mage_hand

import com.mojang.authlib.GameProfile
import net.fabricmc.fabric.api.entity.FakePlayer
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.entity.EntityPose
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import kotlin.math.atan2
import kotlin.math.sqrt

object FakeInteractionUtils {
	private fun createFakePlayer(world: ServerWorld, pos: Vec3d, lookDir: Vec3d, stack: ItemStack, owner: ServerPlayerEntity?, sneak: Boolean): FakePlayer {
		val fakePlayer = if (owner == null)
			FakePlayer.get(world)
		else
			FakePlayer.get(world, GameProfile(owner.uuid, "[Hexical Mage Hand]"))

		val pose = if (sneak) EntityPose.CROUCHING else EntityPose.STANDING
		fakePlayer.setPos(pos.x, pos.y - fakePlayer.getEyeHeight(pose), pos.z)
		fakePlayer.isSneaking = sneak

		val horizon = sqrt(lookDir.x * lookDir.x + lookDir.z * lookDir.z)
		val yaw = Math.toDegrees(atan2(-lookDir.x, lookDir.z)).toFloat()
		val pitch = Math.toDegrees(atan2(-lookDir.y, horizon)).toFloat()
		fakePlayer.yaw = yaw
		fakePlayer.headYaw = yaw
		fakePlayer.pitch = pitch

		fakePlayer.setStackInHand(Hand.MAIN_HAND, stack)
		return fakePlayer
	}

	fun rightClickAt(world: ServerWorld, stack: ItemStack, owner: ServerPlayerEntity?, position: Vec3d, rotation: Vec3d, reach: Double = 4.5, sneak: Boolean = false): List<ItemStack> {
		val fakePlayer = createFakePlayer(world, position, rotation.normalize(), stack, owner, sneak)
		val raycastEnd = position.add(rotation.multiply(reach))
		val box = Box(position, raycastEnd).expand(1.0)

		val blockHit = world.raycast(RaycastContext(position, raycastEnd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, fakePlayer))
		val entityHit = ProjectileUtil.raycast(fakePlayer, position, raycastEnd, box, { entity -> !entity.isSpectator && entity.canHit() }, blockHit?.pos?.squaredDistanceTo(position) ?: (reach * reach))
		val hitResult = entityHit ?: blockHit ?: BlockHitResult.createMissed(raycastEnd, Direction.getFacing(rotation.x, rotation.y, rotation.z), BlockPos.ofFloored(raycastEnd))

		var actionResult = ActionResult.PASS
		when (hitResult) {
			is EntityHitResult -> {
				val target = hitResult.entity
				actionResult = UseEntityCallback.EVENT.invoker().interact(fakePlayer, world, Hand.MAIN_HAND, target, hitResult)
				if (actionResult == ActionResult.PASS) actionResult = target.interactAt(fakePlayer, hitResult.pos, Hand.MAIN_HAND)
				if (actionResult == ActionResult.PASS) actionResult = fakePlayer.interact(target, Hand.MAIN_HAND)
			}
			is BlockHitResult if hitResult.type != HitResult.Type.MISS -> {
				actionResult = UseBlockCallback.EVENT.invoker().interact(fakePlayer, world, Hand.MAIN_HAND, hitResult)
				if (actionResult == ActionResult.PASS) {
					val blockState = world.getBlockState(hitResult.blockPos)
					if (!fakePlayer.isSneaking || fakePlayer.mainHandStack.isEmpty) actionResult = blockState.onUse(world, fakePlayer, Hand.MAIN_HAND, hitResult)
					if (actionResult == ActionResult.PASS) {
						actionResult = fakePlayer.getStackInHand(Hand.MAIN_HAND).useOnBlock(ItemUsageContext(fakePlayer, Hand.MAIN_HAND, hitResult))
					}
				}
			}
		}

		if (actionResult == ActionResult.PASS) {
			val itemResult = fakePlayer.getStackInHand(Hand.MAIN_HAND).use(world, fakePlayer, Hand.MAIN_HAND)
			if (itemResult.result.isAccepted)
				fakePlayer.setStackInHand(Hand.MAIN_HAND, itemResult.value)
		}

		return fakePlayer.inventory.main + fakePlayer.inventory.armor + fakePlayer.inventory.offHand
	}
}