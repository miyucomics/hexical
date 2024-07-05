package miyucomics.hexical.blocks

import net.minecraft.block.*
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class MediaJarBlock : TransparentBlock(Settings.of(Material.GLASS).emissiveLighting { _, _, _ -> true }.luminance { _ -> 15 }.sounds(BlockSoundGroup.GLASS).nonOpaque().solidBlock { _, _, _ -> false }) {
	override fun getRenderType(state: BlockState?) = BlockRenderType.MODEL
	override fun getAmbientOcclusionLightLevel(state: BlockState?, world: BlockView?, pos: BlockPos?) = 1f
	override fun getCameraCollisionShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape = VoxelShapes.empty()
	override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape = VoxelShapes.cuboid(4.0 / 16, 0.0, 4.0 / 16, 12.0 / 16, 10.0 / 16, 12.0 / 16)
}