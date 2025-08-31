package miyucomics.hexical.features.mage_blocks

import miyucomics.hexical.HexicalMain
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.*
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockRenderView
import java.util.function.Function
import java.util.function.Supplier

class MageBlockModel : BakedModel, UnbakedModel, FabricBakedModel {
    private var sprite: Sprite? = null

    @Override
    override fun emitBlockQuads(world: BlockRenderView, state: BlockState, pos: BlockPos, random: Supplier<Random>, context: RenderContext) {
        if (world.getBlockEntity(pos) !is MageBlockEntity)
            return

        val targetState = (world.getBlockEntity(pos) as MageBlockEntity).disguise
        val targetModel = MinecraftClient.getInstance().blockRenderManager.getModel(targetState)
        if (targetModel is FabricBakedModel)
            targetModel.emitBlockQuads(world, targetState, pos, random, context);
    }

    override fun bake(baker: Baker, textureGetter: Function<SpriteIdentifier, Sprite>, rotationContainer: ModelBakeSettings, modelId: Identifier): BakedModel {
        this.sprite = textureGetter.apply(SPRITE_ID)
        return this
    }

    override fun getQuads(state: BlockState?, face: Direction?, random: Random) = emptyList<BakedQuad>()
    override fun getTransformation(): ModelTransformation = ModelTransformation.NONE
    override fun setParents(modelLoader: Function<Identifier, UnbakedModel>) {}
    override fun getOverrides(): ModelOverrideList = ModelOverrideList.EMPTY
    override fun getModelDependencies() = emptyList<Identifier>()
    override fun getParticleSprite() = this.sprite
    override fun useAmbientOcclusion() = false
    override fun isBuiltin() = false
    override fun isSideLit() = true
    override fun hasDepth() = false

    companion object {
        val SPRITE_ID = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier("block/amethyst_block"))
    }
}