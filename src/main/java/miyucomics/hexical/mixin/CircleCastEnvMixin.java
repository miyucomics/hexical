package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.circles.CircleExecutionState;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import at.petrak.hexcasting.api.utils.HexUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.hexical.blocks.PedestalBlockEntity;
import miyucomics.hexical.registry.HexicalBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.function.Predicate;

@Mixin(value = CircleCastEnv.class, remap = false)
public abstract class CircleCastEnvMixin {
	@Shadow @Final protected CircleExecutionState execState;

	@Shadow public abstract Hand getCastingHand();

	@WrapMethod(method = "getPrimaryStacks")
	public List<CastingEnvironment.HeldItemInfo> addHands(Operation<List<CastingEnvironment.HeldItemInfo>> original) {
		ServerWorld world = ((CastingEnvironment) (Object) this).getWorld();
		BlockPos possibleImpetus = this.execState.impetusPos.up();
		if (world.getBlockState(possibleImpetus).isOf(HexicalBlocks.PEDESTAL_BLOCK)) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) world.getBlockEntity(possibleImpetus);
			assert pedestal != null;
			return List.of(new CastingEnvironment.HeldItemInfo(pedestal.getStack(0), HexUtils.otherHand(this.getCastingHand())));
		}
		return original.call();
	}

	@WrapMethod(method = "replaceItem")
	public boolean addHands(Predicate<ItemStack> stackOk, ItemStack replaceWith, @Nullable Hand hand, Operation<Boolean> original) {
		ServerWorld world = ((CastingEnvironment) (Object) this).getWorld();
		BlockPos possibleImpetus = this.execState.impetusPos.up();
		if (world.getBlockState(possibleImpetus).isOf(HexicalBlocks.PEDESTAL_BLOCK)) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) world.getBlockEntity(possibleImpetus);
			assert pedestal != null;
			ItemStack heldStack = pedestal.getStack(0);
			if (stackOk.test(heldStack)) {
				pedestal.setStack(0, replaceWith);
				return true;
			}
			return false;
		}
		return original.call(stackOk, replaceWith, hand);
	}
}