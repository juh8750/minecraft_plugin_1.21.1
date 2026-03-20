package org.example.smartphonemod.smartphonemod.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PhoneChatState {

    public record ChatMessage(boolean mine, String text, long timestamp) {
    }

    private static final Map<String, List<ChatMessage>> ROOMS = new HashMap<>();

    private PhoneChatState() {
    }

    public static List<ChatMessage> getRoom(String key) {
        return ROOMS.computeIfAbsent(key, k -> new ArrayList<>());
    }

    public static void addMyMessage(String key, String text) {
        getRoom(key).add(new ChatMessage(true, text, System.currentTimeMillis()));
    }

    public static void addOtherMessage(String key, String text) {
        getRoom(key).add(new ChatMessage(false, text, System.currentTimeMillis()));
    }
}