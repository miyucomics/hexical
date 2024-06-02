package miyucomics.hexical.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import miyucomics.hexical.client.FakeCameraEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mouse.class)
public class MouseMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@ModifyExpressionValue(method = "updateMouse", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;player:Lnet/minecraft/client/network/ClientPlayerEntity;"))
	private ClientPlayerEntity moveCamera(ClientPlayerEntity original) {
		if (client.getCameraEntity() instanceof FakeCameraEntity camera)
			return camera;
		return original;
	}
}