package io.sandbox.equipment.enchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

public class AwakenEnchantment extends Enchantment {

  protected AwakenEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
    super(weight, type, slotTypes);
  }

  public void appendTooltip(List<Text> tooltip, int level) {
    // Default is to add the space above the name
    tooltip.add(Text.of(" "));
    tooltip.add(this.getName(level));
  }

  public Float applyDamageBeforeArmor(DamageSource source, LivingEntity victim, Float damage, int level) {
    return damage;
  }

  public int getMaxLevel() {
    return 3;
  }

  @Override
  public boolean isAvailableForEnchantedBookOffer() {
    return false;
  }

  @Override
  public boolean isAvailableForRandomSelection() {
    return false;
  }

  public static Float triggerDamageEffects(DamageSource source, LivingEntity victim, Float amount) {
    Float damage = amount;
    LivingEntity user = (LivingEntity)source.getSource();
    List<ItemStack> itemStacks = new ArrayList<>();
    if (user != null) {
      
      for (ItemStack itemStack : user.getItemsEquipped()) {
        itemStacks.add(itemStack);
      }
    }

    for (ItemStack stack : itemStacks) {
      if (!stack.isEmpty()) {
        NbtList nbtList = stack.getEnchantments();
  
        for (int i = 0; i < nbtList.size(); ++i) {
          NbtCompound nbtCompound = nbtList.getCompound(i);
          Optional<Enchantment> enchantOptional = Registries.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound));
          if (enchantOptional.isPresent()) {
            Enchantment enchantment = enchantOptional.get();
            if (enchantment instanceof AwakenEnchantment) {
              AwakenEnchantment enchant = (AwakenEnchantment)enchantment;
              damage = enchant.applyDamageBeforeArmor(source, victim, damage, EnchantmentHelper.getLevelFromNbt(nbtCompound));
            }
          }
        }
      }
    }

    return damage;
  }
}
