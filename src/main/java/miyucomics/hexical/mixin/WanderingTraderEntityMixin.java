package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import miyucomics.hexical.HexicalMain;
import miyucomics.hexical.inits.HexicalItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin extends MerchantEntity {
	public WanderingTraderEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "fillRecipes", at = @At("RETURN"))
	public void addNewTrades(CallbackInfo info) {
		TradeOfferList tradeOfferList = getOffers();
		if (tradeOfferList == null)
			return;
		if (random.nextFloat() < 0.5) {
			ItemStack trade = new ItemStack(HexicalItems.HAND_LAMP_ITEM);
			IXplatAbstractions.INSTANCE.findHexHolder(trade).writeHex(new ArrayList<>(), null, MediaConstants.DUST_UNIT * 320);
			IXplatAbstractions.INSTANCE.findMediaHolder(trade).withdrawMedia((int) (HexicalMain.RANDOM.nextFloat() * 160f) * MediaConstants.DUST_UNIT, false);
			tradeOfferList.add(new TradeOffer(new ItemStack(Items.EMERALD, 32), trade, 1, 1, 1));
		}
	}
}