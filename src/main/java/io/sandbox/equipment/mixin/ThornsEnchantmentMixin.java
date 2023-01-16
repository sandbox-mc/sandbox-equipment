package io.sandbox.equipment.mixin;

import java.util.Map.Entry;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ThornsEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.sandbox.equipment.Main;
import io.sandbox.equipment.configTypes.ThornsConfig;

@Mixin(ThornsEnchantment.class)
public class ThornsEnchantmentMixin {
	private static final ThornsConfig CONFIG = Main.getThornsConfig();
	private static final Item[] CHESTPLATES = {
		Items.IRON_CHESTPLATE,
		Items.DIAMOND_CHESTPLATE,
		Items.NETHERITE_CHESTPLATE,
		Items.LEATHER_CHESTPLATE,
		Items.GOLDEN_CHESTPLATE,
		Items.CHAINMAIL_CHESTPLATE
	};

	@Inject(method = "getMaxLevel", at = @At("HEAD"), cancellable = true)
	private void getMaxLevelMixin(CallbackInfoReturnable<Integer> cbir) {
		if (!CONFIG.enabled) { return; }

		cbir.setReturnValue(CONFIG.maxLevel);
	}

	@Inject(method = "onUserDamaged", at = @At("HEAD"), cancellable = true)
	private void onUserDamagedMixin(LivingEntity user, Entity attacker, int level, CallbackInfo cbi) {
		if (!CONFIG.enabled) { return; }

		if (level == 0) {
			cbi.cancel();
			return;
		}

		if (attacker != null) {
			attacker.damage(DamageSource.thorns(user), level * CONFIG.damagePerLevel);
		}

		Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.chooseEquipmentWith(Enchantments.THORNS, user);
		if (entry != null) {
			((ItemStack)entry.getValue()).damage(level, user, (entity) -> {
				 entity.sendEquipmentBreakStatus((EquipmentSlot)entry.getKey());
			});
	 	}

		cbi.cancel();
	}

	@Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
	private void isAcceptableItemMixin(ItemStack stack, CallbackInfoReturnable<Boolean> cbir) {
		if (!CONFIG.enabled) { return; }

		if (!(stack.getItem() instanceof ArmorItem)) {
			cbir.setReturnValue(false);
			return;
		}

		for (int i = 0; i < CHESTPLATES.length; i++) {
			if (CHESTPLATES[i] == stack.getItem()) {
				cbir.setReturnValue(true);
				return;
			}
		}

		cbir.setReturnValue(false);
	}
}
