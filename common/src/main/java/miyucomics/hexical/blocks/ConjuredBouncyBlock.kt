package miyucomics.hexical.blocks

import miyucomics.hexical.generics.GenericConjuredBlock
import net.minecraft.entity.Entity
import net.minecraft.world.BlockView

class ConjuredBouncyBlock : GenericConjuredBlock<ConjuredBouncyBlockEntity>(baseMaterial(), ConjuredBouncyBlockEntity::init) {
	override fun onEntityLand(world: BlockView, entity: Entity) {
		val velocity = entity.velocity
		if (velocity.y < 0)
			entity.setVelocity(velocity.x, -velocity.y, velocity.z)
	}
}