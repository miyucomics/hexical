package miyucomics.hexical.registry

import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.IotaType
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.iota.DyeIota
import miyucomics.hexical.iota.IdentifierIota
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object HexicalIota {
	private val TYPES: MutableMap<Identifier, IotaType<*>> = HashMap()
	val IDENTIFIER_IOTA = type("identifier", IdentifierIota.TYPE)
	val DYE_IOTA = type("dye", DyeIota.TYPE)

	@JvmStatic
	fun init() {
		for ((key, value) in TYPES)
			Registry.register(HexIotaTypes.REGISTRY, key, value)
	}

	private fun <U : Iota, T : IotaType<U>> type(name: String, type: T): T {
		val old = TYPES.put(HexicalMain.id(name), type)
		require(old == null) { "Typo? Duplicate id $name" }
		return type
	}
}