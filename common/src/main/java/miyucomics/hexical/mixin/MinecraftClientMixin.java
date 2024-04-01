package miyucomics.hexical.mixin;

import at.petrak.hexcasting.common.lib.HexSounds;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import miyucomics.hexical.Hexical;
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
public class MinecraftClientMixin {
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
		int rankOfStaff = player.getStackInHand(hand).getOrCreateNbt().getInt("rank");
		if (rankOfStaff == 0)
			return;

		if (hexical$clicks.size() == rankOfStaff) {
			hexical$timer = 0;
			player.world.playSound(player, player.getX(), player.getY(), player.getZ(), HexSounds.CAST_THOTH, SoundCategory.PLAYERS, 1f, 1f);
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeInt(rankOfStaff);
			for (int i = 0; i < rankOfStaff; i++)
				buf.writeBoolean(hexical$clicks.get(i));
			NetworkManager.sendToServer(Hexical.CAST_CONJURED_STAFF_PACKET, buf);
			hexical$clicks.clear();
		}
	}

	@Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doAttack()Z", ordinal = 0), cancellable = true)
	public void onLeftClick(CallbackInfo info) {
		if (player == null || player.isSpectator())
			return;
		Hand hand = getConjuredStaff(player);
		if (hand == null)
			return;
		hexical$timer = hexical$COOLDOWN;
		hexical$clicks.add(false);
		player.swingHand(hand);
		player.world.playSound(player, player.getX(), player.getY(), player.getZ(), HexSounds.SPELL_CIRCLE_CAST, SoundCategory.PLAYERS, 1f, 1f);
		info.cancel();
	}

	@Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doItemUse()V", ordinal = 0), cancellable = true)
	public void onRightClick(CallbackInfo info) {
		if (player == null || player.isSpectator())
			return;
		Hand hand = getConjuredStaff(player);
		if (hand == null)
			return;
		hexical$timer = hexical$COOLDOWN;
		hexical$clicks.add(true);
		player.swingHand(hand);
		player.world.playSound(player, player.getX(), player.getY(), player.getZ(), HexSounds.SPELL_CIRCLE_CAST, SoundCategory.PLAYERS, 1f, 1f);
		info.cancel();
	}
}