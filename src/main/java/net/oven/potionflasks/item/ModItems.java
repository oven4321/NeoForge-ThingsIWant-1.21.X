package net.oven.potionflasks.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.oven.potionflasks.PotionFlasks;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PotionFlasks.MODID);

    public static final DeferredItem<Item> EMPTY_FLASK = ITEMS.register("empty_flask",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> POTION_FLASK = ITEMS.register("potion_flask",
            () -> new PotionFlaskItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
