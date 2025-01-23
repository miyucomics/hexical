package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.circles.ICircleComponent;
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import java.util.EnumSet;

@Mixin(BeaconBlock.class)
public class BeaconBlockMixin implements ICircleComponent {
	@Override
	public ControlFlow acceptControlFlow(CastingImage imageIn, CircleCastEnv env, Direction enterDir, BlockPos pos, BlockState bs, ServerWorld world) {
		EnumSet<Direction> exits = possibleExitDirections(pos, bs, world);
		exits.remove(enterDir.getOpposite());
		return new ControlFlow.Continue(imageIn, exits.stream().map(direction -> exitPositionFromDirection(pos, direction)).toList());
	}

	@Override
	public boolean canEnterFromDirection(Direction enterDir, BlockPos pos, BlockState bs, ServerWorld world) {
		return enterDir != Direction.UP;
	}

	@Override
	public EnumSet<Direction> possibleExitDirections(BlockPos pos, BlockState bs, World world) {
		EnumSet<Direction> exits = EnumSet.allOf(Direction.class);
		exits.remove(Direction.UP);
		exits.remove(Direction.DOWN);
		return exits;
	}

	@Override
	public BlockState startEnergized(BlockPos pos, BlockState bs, World world) {
		return bs;
	}

	@Override
	public boolean isEnergized(BlockPos pos, BlockState bs, World world) {
		return false;
	}

	@Override
	public BlockState endEnergized(BlockPos pos, BlockState bs, World world) {
		return bs;
	}
}