package miyucomics.hexical.inits

import miyucomics.hexical.HexicalMain
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

object HexicalDamageTypes {
	val MAGIC_MISSILE: RegistryKey<DamageType> = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, HexicalMain.id("magic_missile"))
	val SPIKE: RegistryKey<DamageType> = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, HexicalMain.id("spike"))
}