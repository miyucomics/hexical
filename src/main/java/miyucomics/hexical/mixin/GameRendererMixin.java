package miyucomics.hexical.mixin;

import miyucomics.hexical.client.ShaderRenderer;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Inject(method = "render", at = @At("RETURN"))
	public void renderShader(float deltaTick, long l, boolean bl, CallbackInfo ci) {
		ShaderRenderer.render(deltaTick);
	}
}