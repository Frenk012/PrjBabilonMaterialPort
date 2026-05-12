package com.rave.projectbabylonmaterials.client.tooltip;

import com.rave.projectbabylonmaterials.tooltip.EnchantmentDetailsTooltipData;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;

import java.util.List;

public final class EnchantmentDetailsClientTooltip implements ClientTooltipComponent {
    private static final int BASE_ICON_SIZE = 16;
    private static final int PADDING_X = 4;
    private static final int PADDING_Y = 4;
    private static final int ROW_GAP = 2;
    private static final int SECTION_GAP = 4;
    private static final int BOOK_TEXT_GAP = 4;
    private static final int ITEM_GAP = 2;
    private static final int COLUMN_GAP = 8;
    private static final int DESCRIPTION_COLOR = 0xC8C8C8;
    private static final int TITLE_COLOR = 0xFFD700;
    private static final int NAME_COLOR = 0x55FFFF;
    private static final int LABEL_COLOR = 0xAAAAAA;
    private static final ItemStack BOOK_ICON = new ItemStack(Items.ENCHANTED_BOOK);

    private final EnchantmentDetailsTooltipData data;

    public EnchantmentDetailsClientTooltip(EnchantmentDetailsTooltipData data) {
        this.data = data;
    }

    @Override
    public int getHeight() {
        LayoutMetrics metrics = buildLayoutMetrics();
        return metrics.totalHeight;
    }

    @Override
    public int getWidth(Font font) {
        LayoutMetrics metrics = buildLayoutMetrics();
        return metrics.totalWidth;
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        LayoutMetrics metrics = buildLayoutMetrics();
        int cursorY = y + PADDING_Y;

        for (int row = 0; row < metrics.rowCount; row++) {
            int rowY = cursorY;
            for (int column = 0; column < metrics.columnCount; column++) {
                int entryIndex = row * metrics.columnCount + column;
                if (entryIndex >= data.entries().size()) {
                    break;
                }

                int entryX = x + PADDING_X + column * (metrics.columnWidth + metrics.columnGap);
                renderEntry(guiGraphics, font, entryX, rowY, metrics.textScale, data.entries().get(entryIndex));
            }
            cursorY += metrics.rowHeights[row];
            if (row < metrics.rowCount - 1) {
                cursorY += metrics.sectionGap;
            }
        }
    }

    private void renderEntry(GuiGraphics guiGraphics, Font font, int x, int y, float textScale, EnchantmentDetailsTooltipData.Entry entry) {
        int lineHeight = scaledTextHeight(textScale);
        int rowGap = scaledSpacing(ROW_GAP, textScale);
        int bookSize = scaledBookSize(textScale);
        int bookGap = scaledSpacing(BOOK_TEXT_GAP, textScale);
        int cursorY = y;

        drawHeader(guiGraphics, font, entry.enchantmentName(), x, cursorY, textScale);
        cursorY += lineHeight + rowGap;

        renderScaledItem(guiGraphics, BOOK_ICON, x, cursorY, bookSize / (float) BASE_ICON_SIZE);
        drawScaledText(guiGraphics, font, entry.description(), x + bookSize + bookGap, cursorY + Math.max(0, (bookSize - lineHeight) / 2), DESCRIPTION_COLOR, textScale);
        cursorY += bookSize + rowGap;

        drawScaledText(guiGraphics, font, data.appliesToLabel(), x, cursorY, LABEL_COLOR, textScale);
        cursorY += lineHeight + rowGap;

        renderApplicableItems(guiGraphics, x, cursorY, entry.applicableItems(), textScale);
    }

    private LayoutMetrics buildLayoutMetrics() {
        float textScale = getTextScale();
        int columnCount = getColumnCount();
        int rowCount = (int) Math.ceil(data.entries().size() / (double) columnCount);
        int[] entryHeights = new int[data.entries().size()];
        int[] entryWidths = new int[data.entries().size()];
        int lineHeight = scaledTextHeight(textScale);
        int bookSize = scaledBookSize(textScale);
        int rowGap = scaledSpacing(ROW_GAP, textScale);
        int sectionGap = scaledSpacing(SECTION_GAP, textScale);
        int columnGap = columnCount > 1 ? scaledSpacing(COLUMN_GAP, textScale) : 0;

        int columnWidth = 0;
        for (int i = 0; i < data.entries().size(); i++) {
            EnchantmentDetailsTooltipData.Entry entry = data.entries().get(i);
            entryHeights[i] = lineHeight + rowGap + bookSize + rowGap + lineHeight + rowGap + scaledApplicableSize(entry.applicableItems(), textScale);
            entryWidths[i] = getEntryWidth(entry, textScale);
            columnWidth = Math.max(columnWidth, entryWidths[i]);
        }

        int[] rowHeights = new int[rowCount];
        for (int row = 0; row < rowCount; row++) {
            int maxHeight = 0;
            for (int column = 0; column < columnCount; column++) {
                int index = row * columnCount + column;
                if (index >= entryHeights.length) {
                    break;
                }
                maxHeight = Math.max(maxHeight, entryHeights[index]);
            }
            rowHeights[row] = maxHeight;
        }

        int totalHeight = PADDING_Y * 2;
        for (int row = 0; row < rowCount; row++) {
            totalHeight += rowHeights[row];
            if (row < rowCount - 1) {
                totalHeight += sectionGap;
            }
        }

        int totalWidth = PADDING_X * 2 + columnWidth * columnCount + columnGap * Math.max(0, columnCount - 1);
        return new LayoutMetrics(textScale, columnCount, rowCount, columnWidth, columnGap, sectionGap, rowHeights, totalWidth, totalHeight);
    }

    private int getEntryWidth(EnchantmentDetailsTooltipData.Entry entry, float textScale) {
        Font font = net.minecraft.client.Minecraft.getInstance().font;
        int bookSize = scaledBookSize(textScale);
        int bookGap = scaledSpacing(BOOK_TEXT_GAP, textScale);
        int width = 0;
        width = Math.max(width, scaledTextWidth(font, createHeader(entry.enchantmentName()), textScale));
        width = Math.max(width, bookSize + bookGap + scaledTextWidth(font, entry.description(), textScale));
        width = Math.max(width, scaledTextWidth(font, data.appliesToLabel(), textScale));
        width = Math.max(width, getApplicableRowWidth(entry.applicableItems(), textScale));
        return width;
    }

    private int getApplicableRowWidth(List<ItemStack> items, float textScale) {
        int scaledSize = scaledApplicableSize(items, textScale);
        return items.size() * scaledSize + Math.max(0, items.size() - 1) * scaledSpacing(ITEM_GAP, textScale);
    }

    private int scaledApplicableSize(List<ItemStack> items, float textScale) {
        return Math.max(7, Math.round(BASE_ICON_SIZE * getApplicableScale(items.size(), textScale)));
    }

    private float getApplicableScale(int itemCount, float textScale) {
        if (itemCount <= 4) {
            return textScale * 0.85F;
        }
        if (itemCount <= 6) {
            return textScale * 0.72F;
        }
        return textScale * 0.60F;
    }

    private float getTextScale() {
        int enchantCount = data.entries().size();
        if (enchantCount <= 2) {
            return 1.0F;
        }
        if (enchantCount <= 4) {
            return 0.82F;
        }
        if (enchantCount <= 6) {
            return 0.72F;
        }
        return 0.62F;
    }

    private int getColumnCount() {
        return data.entries().size() >= 3 ? 2 : 1;
    }

    private int scaledTextWidth(Font font, Component component, float scale) {
        return Math.max(1, Math.round(font.width(component) * scale));
    }

    private int scaledTextHeight(float scale) {
        return Math.max(7, Math.round(9 * scale));
    }

    private int scaledBookSize(float scale) {
        return Math.max(10, Math.round(BASE_ICON_SIZE * scale));
    }

    private int scaledSpacing(int value, float scale) {
        return Math.max(1, Math.round(value * scale));
    }

    private void renderApplicableItems(GuiGraphics guiGraphics, int x, int y, List<ItemStack> items, float textScale) {
        float scale = getApplicableScale(items.size(), textScale);
        int scaledSize = Math.max(7, Math.round(BASE_ICON_SIZE * scale));
        int gap = Math.max(1, Math.round(ITEM_GAP * textScale));
        int currentX = x;
        for (ItemStack stack : items) {
            renderScaledItem(guiGraphics, stack, currentX, y, scale);
            currentX += scaledSize + gap;
        }
    }

    private void renderScaledItem(GuiGraphics guiGraphics, ItemStack stack, int x, int y, float scale) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.renderItem(stack, 0, 0);
        guiGraphics.pose().popPose();
    }

    private void drawHeader(GuiGraphics guiGraphics, Font font, Component enchantmentName, int x, int y, float scale) {
        Component titlePart = data.title();
        String separator = " - ";
        int titleWidth = scaledTextWidth(font, titlePart, scale);
        int separatorWidth = Math.max(1, Math.round(font.width(separator) * scale));
        drawScaledText(guiGraphics, font, titlePart, x, y, TITLE_COLOR, scale);
        drawScaledString(guiGraphics, font, separator, x + titleWidth, y, LABEL_COLOR, scale);
        drawScaledText(guiGraphics, font, enchantmentName, x + titleWidth + separatorWidth, y, NAME_COLOR, scale);
    }

    private Component createHeader(Component enchantmentName) {
        return data.title().copy().append(Component.literal(" - ")).append(enchantmentName.copy());
    }

    private void drawScaledText(GuiGraphics guiGraphics, Font font, Component component, int x, int y, int color, float scale) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.drawString(font, component, 0, 0, color, false);
        guiGraphics.pose().popPose();
    }

    private void drawScaledString(GuiGraphics guiGraphics, Font font, String text, int x, int y, int color, float scale) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.drawString(font, text, 0, 0, color, false);
        guiGraphics.pose().popPose();
    }

    private record LayoutMetrics(float textScale, int columnCount, int rowCount, int columnWidth, int columnGap,
                                 int sectionGap, int[] rowHeights, int totalWidth, int totalHeight) {
    }
}
