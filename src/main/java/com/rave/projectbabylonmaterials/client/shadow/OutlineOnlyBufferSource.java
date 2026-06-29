package com.rave.projectbabylonmaterials.client.shadow;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderType;

public final class OutlineOnlyBufferSource implements MultiBufferSource {
    private static final VertexConsumer NO_OP_VERTEX_CONSUMER = new NoOpVertexConsumer();

    private final OutlineBufferSource outlineBufferSource;

    public OutlineOnlyBufferSource(OutlineBufferSource outlineBufferSource) {
        this.outlineBufferSource = outlineBufferSource;
    }

    @Override
    public VertexConsumer getBuffer(RenderType renderType) {
        return renderType.outline()
                .<VertexConsumer>map(outlineBufferSource::getBuffer)
                .orElse(NO_OP_VERTEX_CONSUMER);
    }

    private static final class NoOpVertexConsumer implements VertexConsumer {
        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            return this;
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha) {
            return this;
        }

        @Override
        public VertexConsumer uv(float u, float v) {
            return this;
        }

        @Override
        public VertexConsumer overlayCoords(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer uv2(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            return this;
        }

        @Override
        public void endVertex() {
        }

        @Override
        public void defaultColor(int red, int green, int blue, int alpha) {
        }

        @Override
        public void unsetDefaultColor() {
        }
    }
}