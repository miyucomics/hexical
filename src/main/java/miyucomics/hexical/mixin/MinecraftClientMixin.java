package miyucomics.hexical.mixin;

import at.petrak.hexcasting.common.lib.HexSounds;
import miyucomics.hexical.interfaces.MinecraftClientMinterface;
import miyucomics.hexical.registry.HexicalNetworking;
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

import static miyucomics.hexical.items.TchotchkeItemKt.getTchotchke;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements MinecraftClientMinterface {
	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Override
	public void leftClick() {
		tchotchke(false);
	}

	@Override
	public void rightClick() {
		tchotchke(true);
	}

	private void tchotchke(boolean input) {
		if (player == null || player.isSpectator())
			return;
		Hand hand = getTchotchke(player);
		if (hand == null)
			return;
		player.swingHand(hand);
		player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), HexSounds.ADD_TO_PATTERN, SoundCategory.PLAYERS, 1f, 1f);

		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(input);
		ClientPlayNetworking.send(HexicalNetworking.TCHOTCHKE_CHANNEL, buf);
	}
}