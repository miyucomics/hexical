package miyucomics.hexical.casting.patterns.lore

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.utils.getCompound
import miyucomics.hexpose.iotas.TextIota
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.text.Text

object OpItemLore : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val item = args.getItemEntity(0, argc)
		env.assertEntityInRange(item)
		if (args[1] is NullIota)
			return SpellAction.Result(Spell(item.stack, null), 0, listOf(ParticleSpray.cloud(item.pos, 1.0)))
		val lore = args.getList(1, argc).map {
			if (it !is TextIota)
				throw MishapInvalidIota.of(args[1], 0, "text_list")
			it.text
		}
		return SpellAction.Result(Spell(item.stack, lore), 0, listOf(ParticleSpray.cloud(item.pos, 1.0)))
	}

	private data class Spell(val stack: ItemStack, val lore: List<Text>?) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			if (lore == null) {
				stack.getCompound("display")?.remove("Lore")
				return
			}

			val display = stack.getOrCreateSubNbt("display")
			val loreList = NbtList()
			lore.forEach { loreList.add(NbtString.of(Text.Serializer.toJson(it))) }
			display.put("Lore", loreList)
		}
	}
}