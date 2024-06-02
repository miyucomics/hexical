package miyucomics.hexical.mixin;

import miyucomics.hexical.client.FakeCameraEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
	private void dontRenderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
		if (client.getCameraEntity() instanceof FakeCameraEntity)
			ci.cancel();
	}
}