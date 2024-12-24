package miyucomics.hexical.inits

import miyucomics.hexical.entities.SpikeEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity

object HexicalDamageTypes {
	fun magicMissile(projectile: PersistentProjectileEntity, attacker: Entity?): DamageSource = ProjectileDamageSource("magic_missile", projectile, attacker).setProjectile().setUnblockable().setUsesMagic().setBypassesArmor().setBypassesProtection()
	fun spike(spike: SpikeEntity, attacker: PlayerEntity?): DamageSource = ProjectileDamageSource("spike", spike, attacker)
}