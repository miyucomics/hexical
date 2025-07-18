package miyucomics.hexical.mixin;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import miyucomics.hexical.features.evocation.PlayerAnimations;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin implements PlayerAnimations {
	@Unique
	private final ModifierLayer<IAnimation> modAnimationContainer = new ModifierLayer<>();

	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(ClientWorld clientWorld, GameProfile gameProfile, CallbackInfo ci) {
		PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayerEntity) (Object) this).addAnimLayer(50, modAnimationContainer);
	}

	@NotNull
	@Override
	public ModifierLayer<IAnimation> hexicalModAnimations() {
		return modAnimationContainer;
	}
}