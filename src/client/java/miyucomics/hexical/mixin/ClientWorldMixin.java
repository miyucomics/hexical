package miyucomics.hexical.mixin;

import at.petrak.hexcasting.common.lib.HexItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
	@Inject(method = "getBlockParticle", at = @At("HEAD"), cancellable = true)
	public void showLights(CallbackInfoReturnable<Block> cir) {
		if (MinecraftClient.getInstance().player.isHolding(HexItems.SCRYING_LENS))
			cir.setReturnValue(Blocks.LIGHT);
	}
}