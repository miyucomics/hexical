package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicBookshelf;
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import miyucomics.hexical.inits.HexicalSounds;
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

		CastingHarness harness = IXplatAbstractions.INSTANCE.getHarness((ServerPlayerEntity) player, hand);
		Iota iota = HexIotaTypes.deserialize(nbt, (ServerWorld) world);
		if (harness.getParenCount() == 0)
			harness.getStack().add(iota);
		else
			harness.getParenthesized().add(iota);

		IXplatAbstractions.INSTANCE.setHarness((ServerPlayerEntity) player, harness);
		world.playSound(null, pos, HexicalSounds.INSTANCE.getSUDDEN_REALIZATION(), BLOCKS, 1f, 1f);
		player.swingHand(hand, true);
	}
}