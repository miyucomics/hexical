package miyucomics.hexical.features.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.features.prestidigitation.handlers.PrestidigitationBlockBooleans
import miyucomics.hexical.features.prestidigitation.handlers.PrestidigitationBlockTransformations
import miyucomics.hexical.features.prestidigitation.handlers.PrestidigitationHandlersBlock
import miyucomics.hexical.features.prestidigitation.handlers.PrestidigitationHandlersEntity
import miyucomics.hexical.features.prestidigitation.interfaces.PrestidigitationHandler
import miyucomics.hexical.features.prestidigitation.interfaces.PrestidigitationHandlerBlock
import miyucomics.hexical.features.prestidigitation.interfaces.PrestidigitationHandlerEntity
import miyucomics.hexical.misc.InitHook
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos

object PrestidigitationHandlers : InitHook() {
	private val handlers = mutableListOf<PrestidigitationHandler>()

	override fun init() {
		PrestidigitationHandlersBlock.init(::register)
		PrestidigitationHandlersEntity.init(::register)
		PrestidigitationBlockBooleans.init(::register)
		PrestidigitationBlockTransformations.init(::register)
	}

	fun register(handler: PrestidigitationHandler) { handlers += handler }
	fun resolve(env: CastingEnvironment, pos: BlockPos): PrestidigitationHandlerBlock? = handlers.firstOrNull { it is PrestidigitationHandlerBlock && it.canAffectBlock(env, pos) } as? PrestidigitationHandlerBlock
	fun resolve(env: CastingEnvironment, entity: Entity): PrestidigitationHandlerEntity<*>? = handlers.firstOrNull { it is PrestidigitationHandlerEntity<*> && it.canAffectEntity(env, entity) } as? PrestidigitationHandlerEntity<*>
}