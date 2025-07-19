package miyucomics.hexical.features.lamps

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.ktxt.tellWitnessesThatIWasMurdered
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.item.ItemStack

object OpPromoteLamp : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val sacrifice = args.getEntity(0, argc)
		env.assertEntityInRange(sacrifice)
		if (sacrifice is MobEntity && IXplatAbstractions.INSTANCE.isBrainswept(sacrifice))
			throw MishapBadEntity.of(sacrifice, "has_brain")
		if (sacrifice !is VillagerEntity || sacrifice.villagerData.level < 3)
			throw MishapBadEntity.of(sacrifice, "smart_villager")

		val stack = env.getHeldItemToOperateOn { stack -> stack.isOf(HexicalItems.HAND_LAMP_ITEM) }
		if (stack == null)
			throw MishapBadOffhandItem.of(null, "hand_lamp")
		return SpellAction.Result(Spell(sacrifice, stack.stack), MediaConstants.CRYSTAL_UNIT * 10, listOf(ParticleSpray.cloud(sacrifice.eyePos, 3.0, 500)))
	}

	private data class Spell(val sacrifice: VillagerEntity, val original: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val archLamp = ItemStack(HexicalItems.ARCH_LAMP_ITEM)
			val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(archLamp)!!
			val mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(original)!!
			hexHolder.writeHex(hexHolder.getHex(env.world) ?: listOf(), null, mediaHolder.media)

			val item = ItemEntity(env.world, sacrifice.x, sacrifice.eyeY, sacrifice.z, archLamp, 0.0, 0.0, 0.0)
			item.setNoGravity(true)
			env.world.spawnEntity(item)

			original.decrement(1)
			sacrifice.tellWitnessesThatIWasMurdered(env.castingEntity!!)
			sacrifice.discard()
		}
	}
}