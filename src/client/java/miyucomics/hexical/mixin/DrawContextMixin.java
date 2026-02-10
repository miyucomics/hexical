package miyucomics.hexical.mixin;

import miyucomics.hexical.features.charms.CharmUtilities;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {
	@Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
	public void renderCharmedItemMedia(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
		if (!CharmUtilities.isStackCharmed(stack))
			return;
		DrawContext graphics = (DrawContext) (Object) this;
		float progress = (float) CharmUtilities.getMedia(stack) / CharmUtilities.getMaxMedia(stack);
		int color = CharmUtilities.getBarColor(stack);
		graphics.fill(RenderLayer.getGuiOverlay(), x + 2, y + 2, x + 3, y + 13, ColorHelper.Argb.lerp(0.5f, 0xff_000000, color));
		graphics.fill(RenderLayer.getGuiOverlay(), x + 2, y + 2, x + 3, y + Math.round(progress * 10), color);
	}
}