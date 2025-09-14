package miyucomics.hexical.mixin;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "setBlockEntityNbt", at = @At("RETURN"))
    private static void stripPedestalUUID(ItemStack stack, BlockEntityType<?> blockEntityType, NbtCompound tag, CallbackInfo ci) {
        if (stack.hasNbt() && stack.getNbt().contains("BlockEntityTag")) {
            NbtCompound beTag = stack.getSubNbt("BlockEntityTag");
            if (beTag != null && beTag.contains("persistent_uuid"))
                beTag.remove("persistent_uuid");
        }
    }
}