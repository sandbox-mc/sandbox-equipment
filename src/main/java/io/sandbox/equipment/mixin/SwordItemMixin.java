package io.sandbox.equipment.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;

@Mixin(SwordItem.class)
public class SwordItemMixin extends ToolItem {

  public SwordItemMixin(ToolMaterial material, Settings settings) {
    super(material, settings);
  }

  // @Override
  // public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
  //   tooltip.add(Text.of(" "));
  //   tooltip.add(Text.translatable("item.equipment.custom_sword.tooltip"));
  //   tooltip.add(Text.of(" "));
  //   tooltip.add(Text.translatable("item.equipment.custom_sword.tooltip2"));
  // }
}
