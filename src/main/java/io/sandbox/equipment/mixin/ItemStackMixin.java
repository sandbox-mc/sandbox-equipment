package io.sandbox.equipment.mixin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import io.sandbox.equipment.enchantments.AwakenEnchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

@Mixin(ItemStack.class)
public class ItemStackMixin {

  /**
   * @author Brent
   * @reason To change enchant order to push AwakenEnchants to the bottom
   * @return void
   * @param tooltip
   * @param enchantments
   */
  @Overwrite()
  public static void appendEnchantments(List<Text> tooltip, NbtList enchantments) {
    // This shifts all AwakenEnchantments to the bottom
    Map<AwakenEnchantment, Integer> awakenEnchantments = new HashMap<>();
    for (int i = 0; i < enchantments.size(); ++i) {
      NbtCompound nbtCompound = enchantments.getCompound(i);
      Registries.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound)).ifPresent((e) -> {
        if (e instanceof AwakenEnchantment) {
          awakenEnchantments.put((AwakenEnchantment)e, EnchantmentHelper.getLevelFromNbt(nbtCompound));
        } else {
          tooltip.add(e.getName(EnchantmentHelper.getLevelFromNbt(nbtCompound)));
        }
      });
    }

    for (AwakenEnchantment enchant : awakenEnchantments.keySet()) {
      enchant.appendTooltip(tooltip, awakenEnchantments.get(enchant));
    }
  }
}