package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus;
import at.petrak.hexcasting.api.casting.circles.CircleExecutionState;
import at.petrak.hexcasting.api.misc.Result;
import com.llamalad7.mixinextras.sugar.Local;
import miyucomics.hexical.inits.HexicalBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(CircleExecutionState.class)
public class CircleExecutionStateMixin {
	@Inject(method = "createNew(Lat/petrak/hexcasting/api/casting/circles/BlockEntityAbstractImpetus;Lnet/minecraft/server/network/ServerPlayerEntity;)Lat/petrak/hexcasting/api/misc/Result;", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/circles/ICircleComponent;possibleExitDirections(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;)Ljava/util/EnumSet;", shift = At.Shift.AFTER))
	private static void infiniteVerticalAmbit(BlockEntityAbstractImpetus impetus, @Nullable ServerPlayerEntity caster, CallbackInfoReturnable<Result<CircleExecutionState, BlockPos>> cir, @Local BlockState block, @Local ArrayList<BlockPos> goodSeenPositions) {
		if (block.isOf(HexicalBlocks.WITHERED_SLATE)) {
			goodSeenPositions.add(new BlockPos(impetus.getPos().getX(), -1024, impetus.getPos().getY()));
			goodSeenPositions.add(new BlockPos(impetus.getPos().getX(), 1024, impetus.getPos().getY()));
		}
	}
}