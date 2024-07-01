package miyucomics.hexical.mixin;

import at.petrak.hexcasting.xplat.IXplatAbstractions;
import miyucomics.hexical.HexicalMain;
import miyucomics.hexical.casting.patterns.OpInternalizeHex;
import miyucomics.hexical.interfaces.PlayerEntityMinterface;
import miyucomics.hexical.state.EvokeState;
import miyucomics.hexical.utils.CastingUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
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
		if (player.world.isClient && EvokeState.INSTANCE.getActive().getOrDefault(player.getUuid(), false)) {
			float rot = player.bodyYaw * ((float) Math.PI / 180) + MathHelper.cos((float) player.age * 0.6662f) * 0.25f;
			float cos = MathHelper.cos(rot);
			float sin = MathHelper.sin(rot);
			int color = IXplatAbstractions.INSTANCE.getColorizer(player).getColor(player.world.getTime() * 10, player.getPos());
			float r = ColorHelper.Argb.getRed(color) / 255f;
			float g = ColorHelper.Argb.getGreen(color) / 255f;
			float b = ColorHelper.Argb.getBlue(color) / 255f;
			player.world.addParticle(ParticleTypes.ENTITY_EFFECT, player.getX() + (double) cos * 0.6, player.getY() + 1.8, player.getZ() + (double) sin * 0.6, r, g, b);
			player.world.addParticle(ParticleTypes.ENTITY_EFFECT, player.getX() - (double) cos * 0.6, player.getY() + 1.8, player.getZ() - (double) sin * 0.6, r, g, b);
		}

		if (player.world.isClient)
			return;

		hexical$archLampCastedThisTick = false;

		if (EvokeState.INSTANCE.getActive().getOrDefault(player.getUuid(), false) && CastingUtils.isEnlightened((ServerPlayerEntity) player)) {
			if (EvokeState.INSTANCE.getDuration().getOrDefault(player.getUuid(), 0).equals(HexicalMain.EVOKE_DURATION))
				OpInternalizeHex.Companion.evoke((ServerPlayerEntity) player);
		}
	}

	public boolean getArchLampCastedThisTick() {
		return hexical$archLampCastedThisTick;
	}

	@Override
	public void lampCastedThisTick() {
		hexical$archLampCastedThisTick = true;
	}
}