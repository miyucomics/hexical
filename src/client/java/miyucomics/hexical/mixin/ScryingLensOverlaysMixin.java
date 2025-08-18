package miyucomics.hexical.mixin;

import at.petrak.hexcasting.client.render.ScryingLensOverlays;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import miyucomics.hexical.inits.HexicalBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScryingLensOverlays.class)
public class ScryingLensOverlaysMixin {
	@WrapOperation(method = "lambda$addScryingLensStuff$7", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
	private static boolean renderDefaultRedstoneLens(BlockState instance, Block block, Operation<Boolean> original) {
		return original.call(instance, block) || instance.isOf(HexicalBlocks.MAGE_BLOCK);
	}
}