package miyucomics.hexical.features.toast

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPlayer
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.HexicalMain
import miyucomics.hexpose.iotas.getItemStack
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object OpToast : SpellAction {
	override val argc = 4
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val recipient = args.getPlayer(0, argc)
		return SpellAction.Result(Spell(recipient, args[1].display(), args[2].display(), args.getItemStack(3, argc)), if (recipient == env.castingEntity) 0 else MediaConstants.SHARD_UNIT, listOf())
	}

	private data class Spell(val recipient: ServerPlayerEntity, val title: Text, val description: Text, val stack: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			ServerPlayNetworking.send(recipient, HexicalMain.id("toast"), PacketByteBufs.create().apply {
				writeText(title)
				writeText(description)
				writeItemStack(stack)
			})
		}
	}
}