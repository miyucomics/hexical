package miyucomics.hexical.blocks;

import at.petrak.hexcasting.common.blocks.BlockConjured;
import at.petrak.hexcasting.common.blocks.entity.BlockEntityConjured;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ConjuredBouncyBlock extends BlockConjured {
	public ConjuredBouncyBlock(Settings properties) {
		super(properties);
	}

	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		BlockEntity blockentity = world.getBlockEntity(pos);
		if (blockentity instanceof BlockEntityConjured tile)
			tile.landParticle(entity, 10);
	}

	@Override
	public void onEntityLand(BlockView world, Entity entity) {
		Vec3d velocity = entity.getVelocity();
		if (velocity.y < 0.0)
			entity.setVelocity(velocity.x, -velocity.y * 1.1, velocity.z);
	}
}