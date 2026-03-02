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
	private void copyIota(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockHitResult pHit, CallbackInfoReturnable<ActionResult> cir) {
		if (pLevel.isClient || pPlayer.isSneaking() || pHand == Hand.OFF_HAND || !pPlayer.getMainHandStack().isEmpty())
			return;

		BlockEntity shelf = pLevel.getBlockEntity(pPos);
		if (!(shelf instanceof BlockEntityAkashicBookshelf))
			return;

		NbtCompound nbt = ((BlockEntityAkashicBookshelf) shelf).getIotaTag();
		if (nbt == null)
			return;

		CastingUtils.giveIota((ServerPlayerEntity) pPlayer, IotaType.deserialize(nbt, (ServerWorld) pLevel));
		pLevel.playSound(null, pPos, HexicalSounds.SUDDEN_REALIZATION, BLOCKS, 1f, 1f);
		pPlayer.swingHand(pHand, true);
	}
}