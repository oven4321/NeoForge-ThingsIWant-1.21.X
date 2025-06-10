package net.oven.potionflasks.component;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.oven.potionflasks.PotionFlasks;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(PotionFlasks.MODID);
}
