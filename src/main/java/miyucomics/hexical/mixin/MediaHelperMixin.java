package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.utils.MediaHelper;
import at.petrak.hexcasting.common.lib.HexItems;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import miyucomics.hexical.features.player.fields.WristpocketFieldKt;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MediaHelper.class)
public class MediaHelperMixin {
	@Inject(method = "scanPlayerForMediaStuff", at = @At("RETURN"))
	private static void useWristpocketPhial(ServerPlayerEntity player, CallbackInfoReturnable<List<ADMediaHolder>> cir) {
		ItemStack wristpocket = WristpocketFieldKt.getWristpocket(player);
		if (wristpocket.isOf(HexItems.BATTERY))
			cir.getReturnValue().add(0, IXplatAbstractions.INSTANCE.findMediaHolder(wristpocket));
	}
}