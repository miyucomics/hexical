package miyucomics.hexical.mixin;

import miyucomics.hexical.HexicalMain;
import miyucomics.hexical.inits.HexicalPotions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
	@Unique
	private static final Identifier HEARTS = HexicalMain.id("textures/gui/amethyst_hearts.png");

	@Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
	private void amethystHearts(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null)
			return;
		if (!player.hasStatusEffect(HexicalPotions.WOOLEYED_EFFECT))
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