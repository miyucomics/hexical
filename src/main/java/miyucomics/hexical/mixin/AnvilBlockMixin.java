package miyucomics.hexical.mixin;

import at.petrak.hexcasting.common.lib.HexBlocks;
import at.petrak.hexcasting.common.lib.HexItems;
import miyucomics.hexical.inits.HexicalItems;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
	@Inject(method = "onLanding", at = @At("HEAD"))
	private void squashImpetus(World world, BlockPos blockPos, BlockState blockState, BlockState blockState2, FallingBlockEntity fallingBlockEntity, CallbackInfo ci) {
		world.getEntitiesByClass(ItemEntity.class, new Box(blockPos.add(-1, -1, -1), blockPos.add(1, 1, 1)), EntityPredicates.VALID_ENTITY).forEach(item -> {
			ItemStack newStack = item.getStack();

			if (item.getStack().isOf(HexBlocks.IMPETUS_LOOK.asItem())) {
				newStack = new ItemStack(HexicalItems.FLAT_LOOKING_IMPETUS, newStack.getCount());
			} else if (item.getStack().isOf(HexBlocks.IMPETUS_REDSTONE.asItem())) {
				newStack = new ItemStack(HexicalItems.FLAT_REDSTONE_IMPETUS, newStack.getCount());
			} else if (item.getStack().isOf(HexBlocks.IMPETUS_RIGHTCLICK.asItem())) {
				newStack = new ItemStack(HexicalItems.FLAT_RIGHT_CLICK_IMPETUS, newStack.getCount());
			}

			newStack.setNbt(item.getStack().getNbt());
			item.setStack(newStack);
		});
	}
}