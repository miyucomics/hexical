package miyucomics.hexical.mixin;

import at.petrak.hexcasting.xplat.IXplatAbstractions;
import miyucomics.hexical.casting.patterns.OpInternalizeHex;
import miyucomics.hexical.interfaces.PlayerEntityMinterface;
import miyucomics.hexical.state.EvokeState;
import miyucomics.hexical.utils.CastingUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityMinterface {
	@Unique
	private boolean hexical$archLampCastedThisTick = false;

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		PlayerEntity player = ((PlayerEntity) (Object) this);

		if (player.getWorld().isClient)
			return;

		if (EvokeState.isEvoking(player.getUuid()) && CastingUtils.isEnlightened((ServerPlayerEntity) player))
			if (EvokeState.getDuration(player.getUuid()) == 0)
				OpInternalizeHex.evoke((ServerPlayerEntity) player);

		hexical$archLampCastedThisTick = false;
	}

	public boolean getArchLampCastedThisTick() {
		return hexical$archLampCastedThisTick;
	}

	@Override
	public void archLampCasted() {
		hexical$archLampCastedThisTick = true;
	}
}