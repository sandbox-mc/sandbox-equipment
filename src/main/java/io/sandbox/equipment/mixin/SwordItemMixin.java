package io.sandbox.equipment.mixin;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;

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
