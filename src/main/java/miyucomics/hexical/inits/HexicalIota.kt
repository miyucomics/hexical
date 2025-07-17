package miyucomics.hexical.inits

import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.dyes.DyeIota
import miyucomics.hexical.features.pigments.PigmentIota
import net.minecraft.registry.Registry

object HexicalIota {
	fun init() {
		Registry.register(HexIotaTypes.REGISTRY, HexicalMain.id("dye"), DyeIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, HexicalMain.id("pigment"), PigmentIota.TYPE)
	}
}