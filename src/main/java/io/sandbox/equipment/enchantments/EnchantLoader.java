package io.sandbox.equipment.enchantments;

import io.sandbox.equipment.Main;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EnchantLoader {
  public static Enchantment AMBUSH = new AmbushEnchantment();
  public static Enchantment POISON = new PoisonEnchantment();

  public static void init() {
    Registry.register(Registries.ENCHANTMENT, Main.id("ambush"), AMBUSH);
    Registry.register(Registries.ENCHANTMENT, Main.id("poison"), POISON);
  }
}
