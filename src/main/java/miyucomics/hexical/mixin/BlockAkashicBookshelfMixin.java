package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicBookshelf;
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import miyucomics.hexical.inits.HexicalSounds;
import miyucomics.hexical.misc.CastingUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.sound.SoundCategory.BLOCKS;

@Mixin(BlockAkashicBookshelf.class)
public class BlockAkashicBookshelfMixin {
	@Inject(method = "onUse", at = @At("TAIL"))
	private void copyIota(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		if (world.isClient || player.isSneaking() || hand == Hand.OFF_HAND || !player.getMainHandStack().isEmpty())
			return;

		BlockEntity shelf = world.getBlockEntity(pos);
		if (!(shelf instanceof BlockEntityAkashicBookshelf))
			return;

		NbtCompound nbt = ((BlockEntityAkashicBookshelf) shelf).getIotaTag();
		if (nbt == null)
			return;

		CastingUtils.giveIota((ServerPlayerEntity) player, IotaType.deserialize(nbt, (ServerWorld) world));
		world.playSound(null, pos, HexicalSounds.SUDDEN_REALIZATION, BLOCKS, 1f, 1f);
		player.swingHand(hand, true);
	}
}