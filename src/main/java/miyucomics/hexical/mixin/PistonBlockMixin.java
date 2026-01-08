package miyucomics.hexical.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.hexical.features.zap.ZapManager;
import net.minecraft.block.PistonBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RedstoneView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PistonBlock.class)
public class PistonBlockMixin {
	@WrapMethod(method = "shouldExtend")
	private boolean addRedstonePower(RedstoneView world, BlockPos pos, Direction face, Operation<Boolean> original) {
		if (!(world instanceof ServerWorld))
			return original.call(world, pos, face);
		return original.call(world, pos, face) || ZapManager.hasMagicalPower((ServerWorld) world, pos);
	}
}