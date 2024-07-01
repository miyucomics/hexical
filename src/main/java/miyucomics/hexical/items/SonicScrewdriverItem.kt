package miyucomics.hexical.items

import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.item.MediaHolderItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.data.PrestidigitationData
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

class SonicScrewdriverItem : Item(Settings().maxCount(1).group(HexicalItems.HEXICAL_GROUP)), MediaHolderItem, IotaHolderItem {
	private val range = 32.0

	override fun appendStacks(group: ItemGroup?, stacks: DefaultedList<ItemStack>?) {
		if (this.isIn(group)) {
			val stack = ItemStack(HexicalItems.SONIC_SCREWDRIVER_ITEM)
			val holder = IXplatAbstractions.INSTANCE.findMediaHolder(stack)
			holder!!.media = MediaConstants.DUST_UNIT * 512
			stacks!!.add(stack)
		}
	}

	override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
		user!!.setCurrentHand(hand)
		return TypedActionResult.success(user.getStackInHand(hand))
	}
	override fun getUseAction(stack: ItemStack) = UseAction.BOW
	override fun getMaxUseTime(stack: ItemStack) = 20 * 2
	override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
		if (world.isClient)
			return super.finishUsing(stack, world, user)
		if (user !is PlayerEntity)
			return super.finishUsing(stack, world, user)

		val hit = user.raycast(range, 1.0f, false)
		when (hit.type) {
			null -> {}
			HitResult.Type.MISS -> {}
			HitResult.Type.BLOCK -> {
				val blockHit = hit as BlockHitResult
				val position = blockHit.blockPos
				val effect = PrestidigitationData.blockEffect(world.getBlockState(position).block)?: return super.finishUsing(stack, world, user)
				val cost = effect.getCost()
				val battery = getMedia(stack)
				if (cost <= battery) {
					effect.effectBlock(user as ServerPlayerEntity, position)
					setMedia(stack, battery - cost)
				}
			}
			HitResult.Type.ENTITY -> {
				val entityHit = hit as EntityHitResult
				val entity = entityHit.entity
				val effect = PrestidigitationData.entityEffect(entity)?: return super.finishUsing(stack, world, user)
				val cost = effect.getCost()
				val battery = getMedia(stack)
				if (cost <= battery) {
					effect.effectEntity(user as ServerPlayerEntity, entity)
					setMedia(stack, battery - cost)
				}
			}
		}

		return super.finishUsing(stack, world, user)
	}

	override fun canRecharge(stack: ItemStack?) = true
	override fun getMedia(stack: ItemStack?) = stack!!.orCreateNbt.getInt("media")
	override fun getMaxMedia(stack: ItemStack?) = MediaConstants.CRYSTAL_UNIT * 512
	override fun setMedia(stack: ItemStack?, media: Int) = stack!!.orCreateNbt.putInt("media", media)
	override fun canProvideMedia(stack: ItemStack?) = false

	override fun readIotaTag(stack: ItemStack?) = null
	override fun canWrite(stack: ItemStack?, iota: Iota?): Boolean {
		TODO("Not yet implemented")
	}
	override fun writeDatum(stack: ItemStack?, iota: Iota?) {
		TODO("Not yet implemented")
	}
}