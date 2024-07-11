package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.iota.PatternIota;
import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicBookshelf;
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import at.petrak.hexcasting.common.items.ItemScroll;
import at.petrak.hexcasting.common.lib.HexItems;
import at.petrak.hexcasting.common.lib.HexSounds;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import miyucomics.hexical.items.LivingScrollItem;
import miyucomics.hexical.registry.HexicalItems;
import miyucomics.hexical.registry.HexicalSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static net.minecraft.sound.SoundCategory.BLOCKS;

@Mixin(BlockAkashicBookshelf.class)
public class BlockAkashicBookshelfMixin {
	@Inject(method = "onUse", at = @At(value = "TAIL"), cancellable = true)
	private void copyIota(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		if (world.isClient)
			return;
		if (player.isSneaking() || hand == Hand.OFF_HAND)
			return;
		ItemStack stack = player.getMainHandStack();
		if (stack.getItem() instanceof ItemScroll)
			return;
		BlockEntity shelf = world.getBlockEntity(pos);
		if (shelf instanceof BlockEntityAkashicBookshelf) {
			NbtCompound nbt = ((BlockEntityAkashicBookshelf) shelf).getIotaTag();
			if (nbt == null)
				return;
			if (stack.getItem() instanceof LivingScrollItem scroll) {
				scroll.writeDatum(stack, new PatternIota(Objects.requireNonNull(((BlockEntityAkashicBookshelf) shelf).getPattern())));
				world.playSound(player, pos, HexSounds.SCROLL_SCRIBBLE, SoundCategory.BLOCKS, 1f, 1f);
				cir.setReturnValue(ActionResult.success(true));
			}
			CastingHarness harness = IXplatAbstractions.INSTANCE.getHarness((ServerPlayerEntity) player, hand);
			harness.getStack().add(HexIotaTypes.deserialize(nbt, (ServerWorld) world));
			IXplatAbstractions.INSTANCE.setHarness((ServerPlayerEntity) player, harness);
			world.playSound(null, pos, HexicalSounds.INSTANCE.getSUDDEN_REALIZATION_EVENT(), BLOCKS, 1f, 1f);
			player.swingHand(hand, true);
		}
	}
}