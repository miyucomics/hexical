package miyucomics.hexical.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import miyucomics.hexical.features.curios.curios.FluteCurioItemModel;
import miyucomics.hexical.inits.HexicalItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	@Shadow @Final private ItemModels models;

	@WrapMethod(method = "getModel")
	private BakedModel injectModel(ItemStack itemStack, World world, LivingEntity livingEntity, int i, Operation<BakedModel> original) {
		if (itemStack.isOf(HexicalItems.CURIO_FLUTE))
			return this.models.getModelManager().getModel(FluteCurioItemModel.heldFluteModel);
		return original.call(itemStack, world, livingEntity, i);
	}

	@Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"))
	private void renderFlatFlute(ItemStack stack, ModelTransformationMode mode, boolean bl, MatrixStack matrices, VertexConsumerProvider vertices, int i, int j, BakedModel model, CallbackInfo ci, @Local(argsOnly = true) LocalRef<BakedModel> modelReference) {
		if ((mode == ModelTransformationMode.GUI || mode == ModelTransformationMode.GROUND || mode == ModelTransformationMode.FIXED) && stack.isOf(HexicalItems.CURIO_FLUTE))
			modelReference.set(this.models.getModelManager().getModel(FluteCurioItemModel.fluteModel));
	}
}