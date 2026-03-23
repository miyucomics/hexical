package miyucomics.hexical.mixin;

import miyucomics.hexical.HexicalMain;
import miyucomics.hexical.features.charms.CharmUtilities;
import miyucomics.hexical.features.periwinkle.WooleyedEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InGameHud.class, remap = false)
public abstract class InGameHudMixin {
	@Shadow
	private int scaledHeight;
	@Unique
	private static final Identifier CHARM_MEDIA = HexicalMain.id("textures/gui/charm_bar.png");
	@Unique
	private static final Identifier HEARTS = HexicalMain.id("textures/gui/amethyst_hearts.png");

	@Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
	private void renderCharmMedia(DrawContext context, int x, CallbackInfo ci) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null)
			return;

		ItemStack stack = null;
		if (CharmUtilities.isStackCharmed(player.getStackInHand(Hand.OFF_HAND)))
			stack = player.getStackInHand(Hand.OFF_HAND);
		if (CharmUtilities.isStackCharmed(player.getStackInHand(Hand.MAIN_HAND)))
			stack = player.getStackInHand(Hand.MAIN_HAND);
		if (stack == null)
			return;

		int y = this.scaledHeight - 29;
		float progress = (float) CharmUtilities.getMedia(stack) / CharmUtilities.getMaxMedia(stack);
		context.drawTexture(CHARM_MEDIA, x, y, 0, 0, 183, 5);
		context.drawTexture(CHARM_MEDIA, x, y, 0, 5, (int) (progress * 183.0F), 5);

		ci.cancel();
	}

	@Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
	private void amethystHearts(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null)
			return;
		if (!player.hasStatusEffect(WooleyedEffect.INSTANCE))
			return;
		if (type == InGameHud.HeartType.NORMAL) {
			context.drawTexture(HEARTS, x, y, halfHeart ? 9 : 0, v, 9, 9);
			ci.cancel();
		} else if (type == InGameHud.HeartType.CONTAINER) {
			context.drawTexture(HEARTS, x, y, 18, v, 9, 9);
			ci.cancel();
		}
	}
}