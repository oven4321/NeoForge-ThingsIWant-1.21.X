package net.oven.potionflasks.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PotionFlaskItem extends Item {
    public PotionFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack itemstack = super.getDefaultInstance();
        itemstack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        return itemstack;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving) {
        Player player = entityLiving instanceof Player ? (Player) entityLiving : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
        }

        // apply the effect to the player
        if (!level.isClientSide) {
            PotionContents potioncontents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            potioncontents.forEachEffect(effectInstance -> {
                if (effectInstance.getEffect().value().isInstantenous()) {
                    effectInstance.getEffect().value().applyInstantenousEffect(player, player, entityLiving, effectInstance.getAmplifier(), 1.0);
                } else {
                    entityLiving.addEffect(effectInstance);
                }
            });
        }


        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);
        }

        if (player == null || !player.hasInfiniteMaterials()) {
            if (stack.isEmpty()) {
                return new ItemStack(ModItems.EMPTY_FLASK.get());
            }

            if (player != null) {
                player.getInventory().add(new ItemStack(ModItems.EMPTY_FLASK.get()));
            }
        }
        entityLiving.gameEvent(GameEvent.DRINK);
        return stack;
    }



    //dink :)
    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public @NotNull String getDescriptionId(ItemStack stack) {
        return Potion.getName(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion(), this.getDescriptionId() + ".effect.");
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);
        if (potioncontents != null) {
            potioncontents.addPotionTooltip(tooltipComponents::add, 1.0F, context.tickRate());
        }
    }
}
