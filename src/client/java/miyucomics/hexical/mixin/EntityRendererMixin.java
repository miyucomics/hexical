package miyucomics.hexical.mixin;

import at.petrak.hexcasting.common.lib.HexAttributes;
import miyucomics.hexical.features.mute.MuteRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
	@Shadow @Final protected EntityRenderDispatcher dispatcher;

	@Inject(method = "render", at = @At("HEAD"))
	void renderMuteIcon(Entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertices, int light, CallbackInfo ci) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player.getAttributeValue(HexAttributes.SCRY_SIGHT) <= 0.0)
			return;

		double distance = this.dispatcher.getSquaredDistanceToCamera(entity);
		if (distance <= 4096.0)
			MuteRenderer.render(entity, matrices, vertices, this.dispatcher);
	}
}