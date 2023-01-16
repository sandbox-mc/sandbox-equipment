package io.sandbox.equipment.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class AmbushEnchantment extends AwakenEnchantment {

  protected AmbushEnchantment() {
    super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND });
  }
  
  @Override
  public boolean isAcceptableItem(ItemStack stack) {
    return this.type.isAcceptableItem(stack.getItem()) || stack.getItem() instanceof AxeItem;
  }

  @Override
  public Float applyDamageBeforeArmor(DamageSource source, LivingEntity victim, Float damage, int level) {
    LivingEntity victimTarget = victim.getAttacking();
    System.out.println("Amount: " + damage);
    if (victimTarget == null || (victimTarget != null &&  victimTarget.getUuid() != source.getAttacker().getUuid())) {
      return damage * (1 + 0.15F * level);
    }

    return damage;
  }
}
