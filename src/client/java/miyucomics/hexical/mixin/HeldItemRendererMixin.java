package miyucomics.hexical.mixin;

import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import miyucomics.hexical.features.curios.HandbellCurioPlayerModel;
import miyucomics.hexical.features.curios.curios.HandbellCurio;
import miyucomics.hexical.inits.HexicalItems;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
	@Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", shift = At.Shift.AFTER))
	void shakeBell(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (!item.isOf(HexicalItems.CURIO_HANDBELL))
			return;
		@SuppressWarnings("unchecked")
		float roll = ((ModifierLayer<HandbellCurioPlayerModel>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(HandbellCurio.CHANNEL)).getAnimation().getRoll(tickDelta);
		matrices.translate(0f, 0.25f, -0.25f);
		if (hand == Hand.MAIN_HAND)
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(roll));
		else
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-roll));
		matrices.translate(0f, -0.25f, 0f);
	}
}