package miyucomics.hexical.mixin;

import miyucomics.hexical.HexicalMain;
import miyucomics.hexical.features.patchouli.DyeingPage;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.ClientBookRegistry;

import java.util.Map;

@Mixin(value = ClientBookRegistry.class, remap = false)
public class ClientBookRegistryMixin {
	@Shadow
	@Final
	public Map<Identifier, Class<? extends BookPage>> pageTypes;

	@Inject(method = "addPageTypes", at = @At("HEAD"))
	public void showLights(CallbackInfo ci) {
		pageTypes.put(HexicalMain.id("dyeing"), DyeingPage.class);
	}
}