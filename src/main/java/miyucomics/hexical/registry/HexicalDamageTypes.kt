package miyucomics.hexical.registry

import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.ProjectileDamageSource
import net.minecraft.entity.projectile.PersistentProjectileEntity

object HexicalDamageTypes {
	fun magicMissile(projectile: PersistentProjectileEntity, attacker: Entity?): DamageSource {
		return ProjectileDamageSource("magic_missile", projectile, attacker).setProjectile().setUnblockable().setUsesMagic().setBypassesArmor().setBypassesProtection()
	}
}