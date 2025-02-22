package miyucomics.hexical.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin {
	@Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
	void spawnEvokingParticles(LivingEntity entity, ItemStack stack, ModelTransformationMode modelTransformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
//		if (entity instanceof ClientPlayerEntity player && EvokeState.isEvoking(entity.getUuid())) {
//			ClientWorld world = (ClientWorld) entity.getWorld();
//
//			matrices.push();
//			((HeldItemFeatureRenderer<?, ?>) (Object) this).getContextModel().setArmAngle(arm, matrices);
//			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f));
//			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
//			boolean bl = arm == Arm.LEFT;
//			matrices.translate((float)(bl ? -1 : 1) / 16.0f, 0.125f, -0.625f);
//
//			Vector3f position = matrices.peek().getPositionMatrix().transformPosition(new Vector3f(0, 0, 0)).add((float) entity.getX(), (float) entity.getY(), (float) entity.getZ());
//
//			int color = IXplatAbstractions.INSTANCE.getPigment(player).getColorProvider().getColor(world.getTime() * 10, player.getPos());
//			float r = ColorHelper.Argb.getRed(color) / 255f;
//			float g = ColorHelper.Argb.getGreen(color) / 255f;
//			float b = ColorHelper.Argb.getBlue(color) / 255f;
//			world.addParticle(ParticleTypes.ENTITY_EFFECT, position.x, position.y, position.z, r, g, b);
//
//			matrices.pop();
//		}
	}
}