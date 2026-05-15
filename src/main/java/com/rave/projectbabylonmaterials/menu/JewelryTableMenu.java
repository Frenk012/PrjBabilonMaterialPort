package com.rave.projectbabylonmaterials.menu;

import com.rave.projectbabylonmaterials.block.entity.JewelryTableBlockEntity;
import com.rave.projectbabylonmaterials.init.PBMMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class JewelryTableMenu extends AbstractContainerMenu {
    private final Container container;

    public JewelryTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, buffer));
    }

    public JewelryTableMenu(int containerId, Inventory playerInventory, Container container) {
        super(PBMMenus.JEWELRY_TABLE_MENU.get(), containerId);

        checkContainerSize(container, 4);
        this.container = container;

        container.startOpen(playerInventory.player);

        this.addSlot(new Slot(container, JewelryTableBlockEntity.SLOT_LEFT_MATERIAL, 17, 21));
        this.addSlot(new Slot(container, JewelryTableBlockEntity.SLOT_RIGHT_MATERIAL, 17, 52));
        this.addSlot(new Slot(container, JewelryTableBlockEntity.SLOT_STONE, 75, 36));
        this.addSlot(new Slot(container, JewelryTableBlockEntity.SLOT_OUTPUT, 132, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    private static Container getBlockEntity(Inventory inventory, FriendlyByteBuf buffer) {
        Level level = inventory.player.level();
        BlockEntity blockEntity = level.getBlockEntity(buffer.readBlockPos());
        if (blockEntity instanceof Container container) {
            return container;
        }
        throw new IllegalStateException("Menu provider is not a container");
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = this.slots.get(index);
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copy = sourceStack.copy();

        int machineSlotCount = 4;
        int inventoryStart = machineSlotCount;
        int hotbarStart = inventoryStart + 27;
        int playerEnd = hotbarStart + 9;
        int inputStart = JewelryTableBlockEntity.SLOT_LEFT_MATERIAL;
        int inputEnd = JewelryTableBlockEntity.SLOT_STONE + 1;

        if (index < machineSlotCount) {
            if (!this.moveItemStackTo(sourceStack, inventoryStart, playerEnd, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(sourceStack, inputStart, inputEnd, false)) {
                if (index >= inventoryStart && index < hotbarStart) {
                    if (!this.moveItemStackTo(sourceStack, hotbarStart, playerEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= hotbarStart && index < playerEnd) {
                    if (!this.moveItemStackTo(sourceStack, inventoryStart, hotbarStart, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, 8 + column * 18, 142));
        }
    }
}