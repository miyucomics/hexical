package miyucomics.hexical.mixin;

import at.petrak.hexcasting.common.lib.HexSounds;
import miyucomics.hexical.inits.HexicalNetworking;
import miyucomics.hexical.interfaces.MinecraftClientMinterface;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static miyucomics.hexical.items.ConjuredStaffItemKt.getConjuredStaff;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements MinecraftClientMinterface {
	@Unique
	private static final int hexical$COOLDOWN = 20;
	@Unique
	private final List<Boolean> hexical$clicks = new ArrayList<>();
	@Unique
	private int hexical$timer = 0;
	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo info) {
		if (player == null)
			return;

		if (hexical$timer < 0 && !hexical$clicks.isEmpty()) {
			player.world.playSound(player, player.getX(), player.getY(), player.getZ(), HexSounds.FAIL_PATTERN, SoundCategory.PLAYERS, 1f, 1f);
			hexical$clicks.clear();
			return;
		}
		hexical$timer--;

		Hand hand = getConjuredStaff(player);
		if (hand == null)
			return;
		int rank = player.getStackInHand(hand).getOrCreateNbt().getInt("rank");
		if (rank == 0)
			return;

		if (hexical$clicks.size() >= rank) {
			hexical$timer = 0;
			PacketByteBuf buf = PacketByteBufs.create();
			player.world.playSound(player, player.getX(), player.getY(), player.getZ(), HexSounds.CAST_HERMES, SoundCategory.PLAYERS, 0.25f, 1f);
			buf.writeInt(rank);
			for (int i = 0; i < rank; i++)
				buf.writeBoolean(hexical$clicks.get(i));
			ClientPlayNetworking.send(HexicalNetworking.CONJURED_STAFF_CHANNEL, buf);
			hexical$clicks.clear();
		}
	}

	@Override
	public void leftClick() {
		if (player == null || player.isSpectator())
			return;
		Hand hand = getConjuredStaff(player);
		if (hand == null)
			return;
		player.swingHand(hand);
		hexical$clicks.add(false);
		hexical$timer = hexical$COOLDOWN;
		player.world.playSound(player, player.getX(), player.getY(), player.getZ(), HexSounds.ADD_LINE, SoundCategory.PLAYERS, 1f, 1f);
	}

	@Override
	public void rightClick() {
		if (player == null || player.isSpectator())
			return;
		Hand hand = getConjuredStaff(player);
		if (hand == null)
			return;
		player.swingHand(hand);
		hexical$clicks.add(true);
		hexical$timer = hexical$COOLDOWN;
		player.world.playSound(player, player.getX(), player.getY(), player.getZ(), HexSounds.ADD_LINE, SoundCategory.PLAYERS, 1f, 1f);
	}
}