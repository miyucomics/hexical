package miyucomics.hexical.features.prestidigitation.interfaces

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.features.prestidigitation.handlers.PrestidigitationHandlerEntity
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
		register(object : PrestidigitationHandlerEntity<ArmorStandEntity>(ArmorStandEntity::class.java) {
			override fun affect(env: CastingEnvironment, armorStand: ArmorStandEntity) {
				armorStand.setShowArms(!armorStand.shouldShowArms())
				armorStand.playSound(SoundEvents.ENTITY_ARMOR_STAND_PLACE, 1f, 1f)
			}
		})

		register(object : PrestidigitationHandlerEntity<TntEntity>(TntEntity::class.java) {
			override fun affect(env: CastingEnvironment, tnt: TntEntity) {
				if (tnt.world.getBlockState(tnt.blockPos).isReplaceable) {
					tnt.world.setBlockState(tnt.blockPos, Blocks.TNT.defaultState)
					tnt.world.updateNeighborsAlways(tnt.blockPos, Blocks.TNT)
				}
				tnt.discard()
			}
		})

		register(object : PrestidigitationHandlerEntity<Shearable>(Shearable::class.java) {
			override fun canAffectEntity(env: CastingEnvironment, entity: Entity) = super.canAffectEntity(env, entity) && (entity as Shearable).isShearable
			override fun affect(env: CastingEnvironment, shearable: Shearable) {
				shearable.sheared(SoundCategory.MASTER)
			}
		})

		register(object : PrestidigitationHandlerEntity<SquidEntity>(SquidEntity::class.java) {
			override fun affect(env: CastingEnvironment, squid: SquidEntity) {
				squid.squirt()
			}
		})

		register(object : PrestidigitationHandlerEntity<PandaEntity>(PandaEntity::class.java) {
			override fun affect(env: CastingEnvironment, panda: PandaEntity) {
				panda.isSneezing = true
			}
		})

		register(object : PrestidigitationHandlerEntity<CreeperEntity>(CreeperEntity::class.java) {
			override fun affect(env: CastingEnvironment, creeper: CreeperEntity) {
				if (creeper.isIgnited) creeper.dataTracker.set(CreeperEntity.IGNITED, false)
				else creeper.ignite()
			}
		})

		register(object : PrestidigitationHandlerEntity<PufferfishEntity>(PufferfishEntity::class.java) {
			override fun affect(env: CastingEnvironment, pufferfish: PufferfishEntity) {
				if (pufferfish.puffState != 2) {
					pufferfish.playSound(SoundEvents.ENTITY_PUFFER_FISH_BLOW_UP, 1f, 1f)
					pufferfish.inflateTicks = 0
					pufferfish.deflateTicks = 0
					pufferfish.puffState = 2
				}
			}
		})
	}
}