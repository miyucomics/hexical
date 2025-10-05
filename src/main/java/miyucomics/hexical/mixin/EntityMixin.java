package miyucomics.hexical.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.hexical.features.shield.ShieldEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(Entity.class)
public class EntityMixin {
	@WrapMethod(method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;")
	private static Vec3d collideWithShields(@Nullable Entity entity, Vec3d delta, Box aabb, World world, List<VoxelShape> collisions, Operation<Vec3d> original) {
		List<Box> nearbyShields = world.getEntitiesByClass(ShieldEntity.class, aabb.expand(delta.x, delta.y, delta.z), en -> !en.equals(entity)).stream().map(Entity::getBoundingBox).toList();
		if (nearbyShields.isEmpty())
			return original.call(entity, delta, aabb, world, collisions);

		Vec3d shortenedMovement = delta;

		double dy = shortenedMovement.y;
		for (Box shieldBox : nearbyShields) {
			if (shieldBox.intersects(aabb.stretch(0.0D, dy, 0.0D))) {
				if (dy > 0.0D) {
					dy = Math.min(dy, shieldBox.minY - aabb.maxY);
				} else if (dy < 0.0D) {
					dy = Math.max(dy, shieldBox.maxY - aabb.minY);
				}
			}
		}
		if (Math.abs(dy) < 1.0E-7D) { dy = 0.0D; }
		shortenedMovement = shortenedMovement.withAxis(Direction.Axis.Y, dy);

		double dx = shortenedMovement.x;
		Box aabbAfterY = aabb.offset(0.0D, shortenedMovement.y, 0.0D);
		for (Box shieldBox : nearbyShields) {
			if (shieldBox.intersects(aabbAfterY.stretch(dx, 0.0D, 0.0D))) {
				if (dx > 0.0D) {
					dx = Math.min(dx, shieldBox.minX - aabbAfterY.maxX);
				} else if (dx < 0.0D) {
					dx = Math.max(dx, shieldBox.maxX - aabbAfterY.minX);
				}
			}
		}
		if (Math.abs(dx) < 1.0E-7D) { dx = 0.0D; }
		shortenedMovement = shortenedMovement.withAxis(Direction.Axis.X, dx);

		double dz = shortenedMovement.z;
		Box aabbAfterXY = aabb.offset(shortenedMovement.x, shortenedMovement.y, 0.0D);
		for (Box shieldBox : nearbyShields) {
			if (shieldBox.intersects(aabbAfterXY.stretch(0.0D, 0.0D, dz))) {
				if (dz > 0.0D) {
					dz = Math.min(dz, shieldBox.minZ - aabbAfterXY.maxZ);
				} else if (dz < 0.0D) {
					dz = Math.max(dz, shieldBox.maxZ - aabbAfterXY.minZ);
				}
			}
		}
		if (Math.abs(dz) < 1.0E-7D) { dz = 0.0D; }
		shortenedMovement = shortenedMovement.withAxis(Direction.Axis.Z, dz);

		return original.call(entity, shortenedMovement, aabb, world, collisions);
	}
}