package io.sandbox.equipment.enchantments;

import io.sandbox.equipment.Main;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;

public class EnchantLoader {
  public static Enchantment AMBUSH = new AmbushEnchantment();
  public static Enchantment POISON = new PoisonEnchantment();

  public static void init() {
    Registry.register(Registry.ENCHANTMENT, Main.id("ambush"), AMBUSH);
    Registry.register(Registry.ENCHANTMENT, Main.id("poison"), POISON);
  }
}
