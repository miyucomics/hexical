package miyucomics.hexical.blocks

import miyucomics.hexical.registry.HexicalStatusEffects
import net.minecraft.block.*
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class MediaJarBlock : TransparentBlock(Settings.of(Material.GLASS).emissiveLighting { _, _, _ -> true }.luminance { _ -> 15 }.sounds(BlockSoundGroup.GLASS).nonOpaque().solidBlock { _, _, _ -> false }) {
	override fun getRenderType(state: BlockState?) = BlockRenderType.MODEL
	override fun getAmbientOcclusionLightLevel(state: BlockState?, world: BlockView?, pos: BlockPos?) = 1f
	override fun getCameraCollisionShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape = VoxelShapes.empty()
	override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape = VoxelShapes.cuboid(4.0 / 16, 0.0, 4.0 / 16, 12.0 / 16, 10.0 / 16, 12.0 / 16)

	override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
		if (world.isClient)
			return ActionResult.SUCCESS
		player.swingHand(hand)
		world.playSoundFromEntity(null, player, SoundEvents.ITEM_HONEY_BOTTLE_DRINK, SoundCategory.PLAYERS, 1f, 1f)
		player.addStatusEffect(StatusEffectInstance(HexicalStatusEffects.MEDIA_VISION_STATUS_EFFECT, 2000, 0, false, false, true))
		return ActionResult.SUCCESS
	}
}