package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.circles.CircleExecutionState;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.hexical.features.blocks.PedestalBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.function.Predicate;

@Mixin(value = CircleCastEnv.class, remap = false)
public abstract class CircleCastEnvMixin {
	@Shadow public abstract CircleExecutionState circleState();

	@WrapMethod(method = "getPrimaryStacks")
	public List<CastingEnvironment.HeldItemInfo> addHands(Operation<List<CastingEnvironment.HeldItemInfo>> original) {
		if (circleState().currentImage.getUserData().contains("impetusHand")) {
			PedestalBlockEntity pedestal = getPedestal();
			return List.of(new CastingEnvironment.HeldItemInfo(pedestal.getStack(0), Hand.OFF_HAND));
		}
		return original.call();
	}

	@WrapMethod(method = "replaceItem")
	public boolean addHands(Predicate<ItemStack> stackOk, ItemStack replaceWith, @Nullable Hand hand, Operation<Boolean> original) {
		if (circleState().currentImage.getUserData().contains("impetusHand")) {
			PedestalBlockEntity pedestal = getPedestal();
			ItemStack heldStack = pedestal.getStack(0);
			if (stackOk.test(heldStack)) {
				pedestal.setStack(0, replaceWith);
				return true;
			}
			return false;
		}
		return original.call(stackOk, replaceWith, hand);
	}

	@Unique
	private PedestalBlockEntity getPedestal() {
		int[] position = circleState().currentImage.getUserData().getIntArray("impetusHand");
		ServerWorld world = ((CastingEnvironment) (Object) this).getWorld();
		PedestalBlockEntity pedestal = (PedestalBlockEntity) world.getBlockEntity(new BlockPos(position[0], position[1], position[2]));
		assert pedestal != null;
		return pedestal;
	}
}