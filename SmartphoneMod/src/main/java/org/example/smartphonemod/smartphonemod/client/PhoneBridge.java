package org.example.smartphonemod.smartphonemod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class PhoneBridge {

    private PhoneBridge() {
    }

    public static void requestCall(String targetName, String phoneNumber) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        // TODO:
        // 나중에 여기서 서버로 전화 요청 패킷 전송
        // 예: ClientPacketDistributor.sendToServer(new CallRequestPayload(...));

        mc.player.displayClientMessage(
                Component.literal("[스마트폰] " + targetName + " (" + phoneNumber + ") 에게 전화 요청"),
                false
        );
    }

    public static void saveContact(String name, String phoneNumber) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        // TODO:
        // 나중에 여기서 서버로 연락처 저장 패킷 전송

        mc.player.displayClientMessage(
                Component.literal("[스마트폰] 연락처 저장: " + name + " / " + phoneNumber),
                false
        );
    }

    public static void sendMessage(String targetName, String phoneNumber, String message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        // TODO:
        // 나중에 여기서 서버로 메시지 전송 패킷 전송

        mc.player.displayClientMessage(
                Component.literal("[스마트폰] " + targetName + " 에게 메시지 전송: " + message),
                false
        );
    }
}