package org.example.smartphonemod.smartphonemod.client;

public final class PhoneIncomingCallState {

    private static boolean active = false;
    private static String callerName = "";
    private static String callerNumber = "";

    private PhoneIncomingCallState() {
    }

    public static void startIncoming(String name, String number) {
        active = true;
        callerName = name;
        callerNumber = number;
    }

    public static void clear() {
        active = false;
        callerName = "";
        callerNumber = "";
    }

    public static boolean isActive() {
        return active;
    }

    public static String getCallerName() {
        return callerName;
    }

    public static String getCallerNumber() {
        return callerNumber;
    }
}