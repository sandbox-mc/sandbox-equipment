package io.sandbox.equipment.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class PoisonEnchantment extends AwakenEnchantment {

  protected PoisonEnchantment() {
    super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND });
  }

  @Override
  public boolean isAcceptableItem(ItemStack stack) {
    return this.type.isAcceptableItem(stack.getItem()) || stack.getItem() instanceof AxeItem;
  }

  @Override
  public void onTargetDamaged(LivingEntity user, Entity target, int level) {
    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 * 5 * level, level - 1));
  }
}
