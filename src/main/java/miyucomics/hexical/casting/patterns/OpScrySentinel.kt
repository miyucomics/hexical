package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapOthersName
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.casting.mishaps.NoSentinelMishap
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.registry.HexicalNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.Blocks
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class OpScrySentinel : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val sentinel = IXplatAbstractions.INSTANCE.getSentinel(ctx.caster)
		if (!sentinel.hasSentinel)
			throw NoSentinelMishap()
		ctx.assertVecInRange(sentinel.position)
		return Triple(Spell(sentinel.position), MediaConstants.DUST_UNIT * 3, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val buf = PacketByteBufs.create()
			buf.writeDouble(position.x)
			buf.writeDouble(position.y)
			buf.writeDouble(position.z)
			ServerPlayNetworking.send(ctx.caster, HexicalNetworking.SCRY_SENTINEL, buf)
		}
	}
}