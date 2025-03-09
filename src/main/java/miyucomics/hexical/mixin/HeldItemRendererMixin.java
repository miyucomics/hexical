package miyucomics.hexical.mixin;

import at.petrak.hexcasting.xplat.IXplatAbstractions;
import miyucomics.hexical.state.EvokeState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Arm;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HeldItemRenderer.class)
public class HeldItemRendererMixin {
	@Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "HEAD"))
	void spawnEvokingParticles(LivingEntity entity, ItemStack itemStack, ModelTransformationMode modelTransformationMode, boolean bl, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (entity instanceof ClientPlayerEntity player && EvokeState.isEvoking(entity.getUuid())) {
			ClientWorld world = (ClientWorld) entity.getWorld();
			Vector3f offset = matrixStack.peek().getPositionMatrix().transformPosition(new Vector3f(0, 0, 0));
			Vector3f position = offset.add((float) entity.getX(), (float) entity.getY(), (float) entity.getZ());
			int color = IXplatAbstractions.INSTANCE.getPigment(player).getColorProvider().getColor(world.getTime() * 10, player.getPos());
			float r = ColorHelper.Argb.getRed(color) / 255f;
			float g = ColorHelper.Argb.getGreen(color) / 255f;
			float b = ColorHelper.Argb.getBlue(color) / 255f;
			world.addParticle(ParticleTypes.ENTITY_EFFECT, position.x, position.y, position.z, r, g, b);
		}
	}
}