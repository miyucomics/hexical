package miyucomics.hexical.entities

import miyucomics.hexical.HexicalMain
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.ProjectileEntityRenderer
import net.minecraft.util.Identifier

@Environment(value = EnvType.CLIENT)
class MagicMissileRenderer(context: EntityRendererFactory.Context) : ProjectileEntityRenderer<MagicMissileEntity>(context) {
	override fun getTexture(entity: MagicMissileEntity?) = TEXTURE
	companion object {
		val TEXTURE: Identifier = Identifier(HexicalMain.MOD_ID, "textures/entity/magic_missile.png")
	}
}