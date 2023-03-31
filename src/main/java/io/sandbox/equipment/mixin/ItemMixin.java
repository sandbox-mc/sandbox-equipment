package io.sandbox.equipment.mixin;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.sandbox.equipment.items.StoneOfEnhancementItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;

@Mixin(Item.class)
public class ItemMixin {
  @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo cbi) {
    NbtCompound nbt = stack.getOrCreateNbt();
    boolean isEnhanced = nbt.getBoolean(StoneOfEnhancementItem.ENHANCEMENT_COUNT);
    if (isEnhanced) {
      StoneOfEnhancementItem.appendAttributeTooltip(stack, world, tooltip, context);
    }
  }

  // @Inject(method = "onCraft", at = @At("HEAD"), cancellable = true)
  // public void onCraft(ItemStack stack, World world, PlayerEntity player, CallbackInfo cbi) {
  //   System.out.println("Crafted!!");
  // }
}
