package org.example.smartphonemod.smartphonemod.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.example.smartphonemod.smartphonemod.SmartphoneMod;

@EventBusSubscriber(modid = SmartphoneMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CallOverlayHud {

    private static final ResourceLocation LAYER_ID =
            ResourceLocation.fromNamespaceAndPath(SmartphoneMod.MODID, "call_overlay");

    private static final int WIDTH = 150;
    private static final int HEIGHT = 70;
    private static final int MARGIN_RIGHT = 12;
    private static final int MARGIN_BOTTOM = 12;

    private CallOverlayHud() {
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(LAYER_ID, CallOverlayHud::renderLayer);
    }

    private static void renderLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!PhoneCallState.isActive()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) {
            return;
        }

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int x = screenWidth - WIDTH - MARGIN_RIGHT;
        int y = screenHeight - HEIGHT - MARGIN_BOTTOM;

        String hangupKey = PhoneKeyHandler.END_CALL.getTranslatedKeyMessage().getString();

        guiGraphics.fill(x - 2, y - 2, x + WIDTH + 2, y + HEIGHT + 2, 0xFF101010);
        guiGraphics.fill(x, y, x + WIDTH, y + HEIGHT, 0xDD1E1E1E);

        guiGraphics.drawString(mc.font, "통화중", x + 10, y + 8, 0x00FFAA, false);
        guiGraphics.drawString(mc.font, PhoneCallState.getTargetName(), x + 10, y + 24, 0xFFFFFF, false);
        guiGraphics.drawString(mc.font, PhoneCallState.getTargetNumber(), x + 10, y + 36, 0xCCCCCC, false);
        guiGraphics.drawString(mc.font, PhoneCallState.getElapsedTimeText(), x + 10, y + 50, 0xFFFFFF, false);
        guiGraphics.drawString(mc.font, hangupKey + ": 종료", x + 78, y + 50, 0xFF8080, false);
    }
}