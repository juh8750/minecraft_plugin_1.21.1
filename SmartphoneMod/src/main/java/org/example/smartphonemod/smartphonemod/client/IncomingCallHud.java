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
public final class IncomingCallHud {

    private static final ResourceLocation LAYER_ID =
            ResourceLocation.fromNamespaceAndPath(SmartphoneMod.MODID, "incoming_call_overlay");

    private static final int WIDTH = 180;
    private static final int HEIGHT = 84;

    private IncomingCallHud() {
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(LAYER_ID, IncomingCallHud::renderLayer);
    }

    private static void renderLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!PhoneIncomingCallState.isActive()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) {
            return;
        }

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int x = (screenWidth - WIDTH) / 2;
        int y = 24;

        String acceptKey = PhoneKeyHandler.ACCEPT_CALL.getTranslatedKeyMessage().getString();
        String declineKey = PhoneKeyHandler.DECLINE_CALL.getTranslatedKeyMessage().getString();

        guiGraphics.fill(x - 2, y - 2, x + WIDTH + 2, y + HEIGHT + 2, 0xFF101010);
        guiGraphics.fill(x, y, x + WIDTH, y + HEIGHT, 0xEE202020);

        guiGraphics.drawString(mc.font, "수신 전화", x + 10, y + 10, 0x00FFAA, false);
        guiGraphics.drawString(mc.font, PhoneIncomingCallState.getCallerName(), x + 10, y + 28, 0xFFFFFF, false);
        guiGraphics.drawString(mc.font, PhoneIncomingCallState.getCallerNumber(), x + 10, y + 42, 0xCCCCCC, false);
        guiGraphics.drawString(mc.font, acceptKey + ": 받기", x + 10, y + 62, 0x80FF80, false);
        guiGraphics.drawString(mc.font, declineKey + ": 거절", x + 90, y + 62, 0xFF8080, false);
    }
}