package com.rave.projectbabylonmaterials.init;

import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.block.entity.JewelryTableBlockEntity;
import com.rave.projectbabylonmaterials.block.entity.MagicalInfuserBlockEntity;
import com.rave.projectbabylonmaterials.block.entity.RefinementTableBlockEntity;
import com.rave.projectbabylonmaterials.block.entity.ReforgeTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class PBMBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ProjectBabylonMaterials.MODID);

    public static final RegistryObject<BlockEntityType<MagicalInfuserBlockEntity>> MAGICAL_INFUSER_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("magical_infuser", () ->
                    BlockEntityType.Builder.of(MagicalInfuserBlockEntity::new, PBMBlocks.MAGICAL_INFUSER_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<JewelryTableBlockEntity>> JEWELRY_TABLE_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("jewelry_table", () ->
                    BlockEntityType.Builder.of(JewelryTableBlockEntity::new, PBMBlocks.JEWERLY_TABLE_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<ReforgeTableBlockEntity>> REFORGE_TABLE_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("reforge_table", () ->
                    BlockEntityType.Builder.of(ReforgeTableBlockEntity::new, PBMBlocks.REFORGE_TABLE_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<RefinementTableBlockEntity>> REFINEMENT_TABLE_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("refinement_table", () ->
                    BlockEntityType.Builder.of(RefinementTableBlockEntity::new, PBMBlocks.REFINEMENT_TABLE_BLOCK.get()).build(null));

    private PBMBlockEntities() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
