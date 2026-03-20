package org.example.smartphonemod.smartphonemod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.example.smartphonemod.smartphonemod.SmartphoneMod;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = SmartphoneMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class PhoneKeyHandler {

    private PhoneKeyHandler() {
    }

    public static final KeyMapping OPEN_PHONE = new KeyMapping(
            "key.smartphonemod.open_phone",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "key.categories.smartphonemod"
    );

    public static final KeyMapping END_CALL = new KeyMapping(
            "key.smartphonemod.end_call",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "key.categories.smartphonemod"
    );

    public static final KeyMapping ACCEPT_CALL = new KeyMapping(
            "key.smartphonemod.accept_call",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            "key.categories.smartphonemod"
    );

    public static final KeyMapping DECLINE_CALL = new KeyMapping(
            "key.smartphonemod.decline_call",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "key.categories.smartphonemod"
    );

    public static final KeyMapping TEST_INCOMING_CALL = new KeyMapping(
            "key.smartphonemod.test_incoming_call",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "key.categories.smartphonemod"
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_PHONE);
        event.register(END_CALL);
        event.register(ACCEPT_CALL);
        event.register(DECLINE_CALL);
        event.register(TEST_INCOMING_CALL);
    }

    @EventBusSubscriber(modid = SmartphoneMod.MODID, value = Dist.CLIENT)
    public static final class GameEvents {
        private GameEvents() {
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();

            if (mc.player == null) {
                return;
            }

            while (OPEN_PHONE.consumeClick()) {
                if (mc.screen instanceof PhoneScreen) {
                    mc.setScreen(null);
                } else {
                    mc.setScreen(new PhoneScreen());
                }
            }

            while (END_CALL.consumeClick()) {
                if (PhoneCallState.isActive()) {
                    PhoneCallState.endCall();
                    mc.player.displayClientMessage(Component.literal("[스마트폰] 통화를 종료했습니다."), false);
                }
            }

            while (ACCEPT_CALL.consumeClick()) {
                if (PhoneIncomingCallState.isActive()) {
                    PhoneCallState.startCall(
                            PhoneIncomingCallState.getCallerName(),
                            PhoneIncomingCallState.getCallerNumber()
                    );
                    mc.player.displayClientMessage(Component.literal("[스마트폰] 전화를 받았습니다."), false);
                    PhoneIncomingCallState.clear();
                }
            }

            while (DECLINE_CALL.consumeClick()) {
                if (PhoneIncomingCallState.isActive()) {
                    mc.player.displayClientMessage(Component.literal("[스마트폰] 전화를 거절했습니다."), false);
                    PhoneIncomingCallState.clear();
                }
            }

            while (TEST_INCOMING_CALL.consumeClick()) {
                if (!PhoneIncomingCallState.isActive() && !PhoneCallState.isActive()) {
                    PhoneIncomingCallState.startIncoming("Alex", "010-0000-0002");
                    mc.player.displayClientMessage(Component.literal("[스마트폰] 테스트 수신 전화가 도착했습니다."), false);
                }
            }
        }
    }
}