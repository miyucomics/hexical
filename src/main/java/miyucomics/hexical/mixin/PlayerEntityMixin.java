package miyucomics.hexical.mixin;

import at.petrak.hexcasting.xplat.IXplatAbstractions;
import miyucomics.hexical.casting.patterns.evocation.OpSetEvocation;
import miyucomics.hexical.data.ArchLampState;
import miyucomics.hexical.data.EvokeState;
import miyucomics.hexical.data.LesserSentinelState;
import miyucomics.hexical.interfaces.PlayerEntityMinterface;
import miyucomics.hexical.utils.CastingUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityMinterface {
	@Unique
	private boolean hexical$archLampCastedThisTick = false;
	@Unique
	private ArchLampState hexical$archLampState = new ArchLampState();
	@Unique
	private NbtCompound hexical$evocation = new NbtCompound();
	@Unique
	private ItemStack hexical$wristpocket = ItemStack.EMPTY;
	@Unique
	private LesserSentinelState hexical$lesserSentinels = new LesserSentinelState();

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		PlayerEntity player = ((PlayerEntity) (Object) this);

		if (player.getWorld().isClient && EvokeState.isEvoking(player.getUuid())) {
			float rot = player.bodyYaw * ((float) Math.PI / 180) + MathHelper.cos((float) player.age * 0.6662f) * 0.25f;
			float cos = MathHelper.cos(rot);
			float sin = MathHelper.sin(rot);
			int color = IXplatAbstractions.INSTANCE.getPigment(player).getColorProvider().getColor(player.getWorld().getTime() * 10, player.getPos());
			float r = ColorHelper.Argb.getRed(color) / 255f;
			float g = ColorHelper.Argb.getGreen(color) / 255f;
			float b = ColorHelper.Argb.getBlue(color) / 255f;
			player.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, player.getX() + (double) cos * 0.6, player.getY() + 1.8, player.getZ() + (double) sin * 0.6, r, g, b);
			player.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, player.getX() - (double) cos * 0.6, player.getY() + 1.8, player.getZ() - (double) sin * 0.6, r, g, b);
		}

		if (player.getWorld().isClient)
			return;

		if (EvokeState.isEvoking(player.getUuid()) && CastingUtils.isEnlightened((ServerPlayerEntity) player))
			if (EvokeState.getDuration(player.getUuid()) == 0)
				OpSetEvocation.evoke((ServerPlayerEntity) player);

		hexical$archLampCastedThisTick = false;
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
	void reaadPlayerData(NbtCompound compound, CallbackInfo ci) {
		if (compound.contains("arch_lamp"))
			hexical$archLampState = ArchLampState.createFromNbt(compound.getCompound("arch_lamp"));

		if (compound.contains("evocation"))
			hexical$evocation = compound.getCompound("evocation");

		if (compound.contains("lesser_sentinels"))
			hexical$lesserSentinels = LesserSentinelState.createFromNbt(compound.getList("lesser_sentinels", NbtElement.COMPOUND_TYPE));

		hexical$wristpocket = ItemStack.fromNbt(compound.getCompound("wristpocket"));
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
	void writePlayerData(NbtCompound compound, CallbackInfo ci) {
		compound.put("arch_lamp", hexical$archLampState.toNbt());
		compound.put("lesser_sentinels", hexical$lesserSentinels.toNbt());

		if (hexical$evocation != null)
			compound.put("evocation", hexical$evocation);

		NbtCompound wristpocket = new NbtCompound();
		hexical$wristpocket.writeNbt(wristpocket);
		compound.put("wristpocket", wristpocket);
	}

	public boolean getArchLampCastedThisTick() {
		return hexical$archLampCastedThisTick;
	}

	@Override
	public void archLampCasted() {
		hexical$archLampCastedThisTick = true;
	}

	@Override
	public @NotNull ArchLampState getArchLampState() {
		return hexical$archLampState;
	}

	@Override
	public @NotNull ItemStack getWristpocket() {
		return hexical$wristpocket;
	}

	@Override
	public void setWristpocket(@NotNull ItemStack stack) {
		hexical$wristpocket = stack;
	}

	@Override
	public @NotNull NbtCompound getEvocation() {
		return hexical$evocation;
	}

	@Override
	public void setEvocation(@NotNull NbtCompound hex) {
		hexical$evocation = hex;
	}

	@Override
	public @NotNull LesserSentinelState getLesserSentinels() {
		return hexical$lesserSentinels;
	}

	@Override
	public void setLesserSentinels(@NotNull LesserSentinelState state) {
		hexical$lesserSentinels = state;
	}
}