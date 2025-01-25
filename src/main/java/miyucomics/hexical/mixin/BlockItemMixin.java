package miyucomics.hexical.mixin;

import at.petrak.hexcasting.common.blocks.circles.BlockSlate;
import at.petrak.hexcasting.common.items.storage.ItemSlate;
import at.petrak.hexcasting.common.lib.HexBlocks;
import miyucomics.hexical.blocks.MultislateBlock;
import miyucomics.hexical.inits.HexicalBlocks;
import net.minecraft.advancement.criterion.CuredZombieVillagerCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Objects;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
	@Shadow public abstract Block getBlock();

	@Inject(method = "getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;", at = @At("HEAD"), cancellable = true)
	private void stackSlate(ItemPlacementContext context, CallbackInfoReturnable<BlockState> cir) {
		if (getBlock() == HexBlocks.SLATE && !ItemSlate.hasPattern(context.getStack())) {
			World world = context.getWorld();
			BlockPos blockPos = context.getBlockPos();
			BlockState currentBlockState = world.getBlockState(blockPos);

			if (currentBlockState.isOf(HexBlocks.SLATE)) {
				BlockState newBlockState = HexicalBlocks.MULTISLATE_BLOCK.getDefaultState();
				MultifaceGrowthBlock growth = (MultifaceGrowthBlock) newBlockState.getBlock();
				System.out.println(currentBlockState.get(BlockSlate.ATTACH_FACE));
				switch (currentBlockState.get(BlockSlate.ATTACH_FACE)) {
					case CEILING -> {
						BlockState finalBlock = growth.withDirection(newBlockState, context.getWorld(), blockPos, Direction.UP);
						cir.setReturnValue(Arrays.stream(context.getPlacementDirections()).map(direction -> finalBlock.with(MultifaceGrowthBlock.getProperty(direction), true)).filter(Objects::nonNull).findFirst().orElse(null));
					}
					case FLOOR -> {
						BlockState finalBlock = growth.withDirection(newBlockState, context.getWorld(), blockPos, Direction.DOWN);
						cir.setReturnValue(Arrays.stream(context.getPlacementDirections()).map(direction -> finalBlock.with(MultifaceGrowthBlock.getProperty(direction), true)).filter(Objects::nonNull).findFirst().orElse(null));
					}
					case WALL -> {
						BlockState finalBlock = growth.withDirection(newBlockState, context.getWorld(), blockPos, currentBlockState.get(BlockSlate.FACING).getOpposite());
						cir.setReturnValue(Arrays.stream(context.getPlacementDirections()).map(direction -> finalBlock.with(MultifaceGrowthBlock.getProperty(direction), true)).filter(Objects::nonNull).findFirst().orElse(null));
					}
				};
			} else if (currentBlockState.isOf(HexicalBlocks.MULTISLATE_BLOCK)) {
				cir.setReturnValue(Arrays.stream(context.getPlacementDirections()).map(direction -> currentBlockState.with(MultifaceGrowthBlock.getProperty(direction), true)).filter(Objects::nonNull).findFirst().orElse(null));
			}
		}
	}
}