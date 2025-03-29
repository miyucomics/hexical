package miyucomics.hexical.registry

import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.iotas.DyeIota
import miyucomics.hexical.casting.iotas.PigmentIota
import net.minecraft.registry.Registry

object HexicalIota {
	@JvmStatic
	fun init() {
		Registry.register(HexIotaTypes.REGISTRY, HexicalMain.id("dye"), DyeIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, HexicalMain.id("pigment"), PigmentIota.TYPE)
	}
}