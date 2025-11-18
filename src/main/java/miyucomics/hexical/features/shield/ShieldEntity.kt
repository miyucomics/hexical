package miyucomics.hexical.features.shield

import miyucomics.hexical.features.specklikes.FigureSpecklike
import net.minecraft.entity.EntityType
import net.minecraft.world.World

class ShieldEntity(entityType: EntityType<out ShieldEntity>, world: World) : FigureSpecklike(entityType, world) {}