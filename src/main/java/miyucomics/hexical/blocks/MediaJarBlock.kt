package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.isMediaItem
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.inits.HexicalEffects
import miyucomics.hexical.inits.HexicalRecipe
import miyucomics.hexical.inits.HexicalSounds
import miyucomics.hexical.recipe.TransmutingRecipe
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.block.*
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import kotlin.math.min

class MediaJarBlock : TransparentBlock(
	Settings
		.create()
		.emissiveLighting { _, _, _ -> true }
		.luminance { _ -> 15 }
		.sounds(BlockSoundGroup.GLASS)
		.nonOpaque()
		.solidBlock { _, _, _ -> false }
), BlockEntityProvider {
	override fun getRenderType(state: BlockState) = BlockRenderType.MODEL
	override fun getAmbientOcclusionLightLevel(state: BlockState, world: BlockView, pos: BlockPos) = 1f
	override fun getCameraCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape = VoxelShapes.empty()
	override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape = VoxelShapes.cuboid(4.0 / 16, 0.0, 4.0 / 16, 12.0 / 16, 10.0 / 16, 12.0 / 16)
	override fun createBlockEntity(pos: BlockPos, state: BlockState) = MediaJarBlockEntity(pos, state)

	override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
		val jarData = world.getBlockEntity(pos) as MediaJarBlockEntity
		val stack = player.getStackInHand(hand)
		player.swingHand(hand)

		if (isMediaItem(stack) && jarData.getMedia() < MAX_CAPACITY) {
			val mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(stack.split(1))!!
			jarData.setMedia(min(jarData.getMedia() + mediaHolder.media, MAX_CAPACITY))
			mediaHolder.withdrawMedia(mediaHolder.media, false)
			world.playSoundFromEntity(null, player, HexicalSounds.AMETHYST_MELT, SoundCategory.BLOCKS, 1f, 1f)
			return ActionResult.SUCCESS
		}

		val recipe = getRecipe(stack, world)
		if (recipe != null && stack.count >= recipe.inputCount && jarData.withdrawMedia(recipe.cost)) {
			stack.decrement(recipe.inputCount)
			recipe.output.forEach { reward -> player.giveItemStack(reward.copy()) }
			world.playSound(null, pos, HexicalSounds.ITEM_DUNKS, SoundCategory.BLOCKS, 1f, 1f)
			return ActionResult.SUCCESS
		}

		if (jarData.withdrawMedia(MediaConstants.CRYSTAL_UNIT)) {
			world.playSoundFromEntity(null, player, HexicalSounds.PLAYER_SLURP, SoundCategory.PLAYERS, 1f, 1f)
			if (world.isClient)
				return ActionResult.SUCCESS
			player.addStatusEffect(if (CastingUtils.isEnlightened(player as ServerPlayerEntity)) StatusEffectInstance(HexicalEffects.WOOLEYED_EFFECT, 1200, 0) else StatusEffectInstance(StatusEffects.POISON, 200, 0))
			return ActionResult.SUCCESS
		}

		return ActionResult.FAIL
	}

	companion object {
		const val MAX_CAPACITY = MediaConstants.CRYSTAL_UNIT * 128

		private fun getRecipe(input: ItemStack, world: World): TransmutingRecipe? {
			world.recipeManager.listAllOfType(HexicalRecipe.TRANSMUTING_RECIPE).forEach { recipe ->
				if (recipe.matches(SimpleInventory(input), world) && input.count >= recipe.inputCount)
					return recipe
			}
			return null
		}
	}
}