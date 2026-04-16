package miyucomics.hexical.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.hexical.feature.redstone_pin.RedstonePinManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RedstoneView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RedstoneView.class)
public interface RedstoneViewMixin {
	@WrapMethod(method = "isReceivingRedstonePower")
	private boolean addRedstonePower(BlockPos pos, Operation<Boolean> original) {
		if (!(this instanceof ServerWorld world))
			return original.call(pos);
		return original.call(pos) || RedstonePinManager.hasMagicalPower(world, pos);
	}

	@WrapMethod(method = "getReceivedRedstonePower")
	private int addStrongPower(BlockPos pos, Operation<Integer> original) {
		if (!(this instanceof ServerWorld world))
			return original.call(pos);
		if (!RedstonePinManager.hasMagicalPower(world, pos))
			return original.call(pos);
		return RedstonePinManager.getMagicalPower(world, pos);
	}
}