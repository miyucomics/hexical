package miyucomics.hexical.features.prestidigitation.handlers

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.features.prestidigitation.interfaces.PrestidigitationHandler
import miyucomics.hexical.features.prestidigitation.interfaces.PrestidigitationHandlerEntity
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.Shearable
import net.minecraft.entity.TntEntity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.mob.CreeperEntity
import net.minecraft.entity.passive.PandaEntity
import net.minecraft.entity.passive.PufferfishEntity
import net.minecraft.entity.passive.SquidEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents

object PrestidigitationHandlersEntity {
	fun init(register: (PrestidigitationHandler) -> Unit) {
		register(PrestidigitationHandlerEntity.simple(ArmorStandEntity::class.java) { stand ->
			stand.setShowArms(!stand.shouldShowArms())
			stand.playSound(SoundEvents.ENTITY_ARMOR_STAND_PLACE, 1f, 1f)
		})

		register(PrestidigitationHandlerEntity.simple(TntEntity::class.java) { tnt ->
			if (tnt.world.getBlockState(tnt.blockPos).isReplaceable) {
				tnt.world.setBlockState(tnt.blockPos, Blocks.TNT.defaultState)
				tnt.world.updateNeighborsAlways(tnt.blockPos, Blocks.TNT)
			}
			tnt.discard()
		})

		register(PrestidigitationHandlerEntity.simple(CreeperEntity::class.java) { creeper ->
			if (creeper.isIgnited) creeper.dataTracker.set(CreeperEntity.IGNITED, false)
			else creeper.ignite()
		})

		register(PrestidigitationHandlerEntity.simple(PufferfishEntity::class.java) { pufferfish ->
			if (pufferfish.puffState != 2) {
				pufferfish.playSound(SoundEvents.ENTITY_PUFFER_FISH_BLOW_UP, 1f, 1f)
				pufferfish.inflateTicks = 0
				pufferfish.deflateTicks = 0
				pufferfish.puffState = 2
			}
		})

		register(PrestidigitationHandlerEntity.simple(PandaEntity::class.java) { it.isSneezing = true })
		register(PrestidigitationHandlerEntity.simple(SquidEntity::class.java, SquidEntity::squirt))

		register(object : PrestidigitationHandlerEntity<Shearable>(Shearable::class.java) {
			override fun canAffectEntity(env: CastingEnvironment, entity: Entity) = super.canAffectEntity(env, entity) && (entity as Shearable).isShearable
			override fun affect(env: CastingEnvironment, shearable: Shearable) {
				shearable.sheared(SoundCategory.MASTER)
			}
		})
	}
}