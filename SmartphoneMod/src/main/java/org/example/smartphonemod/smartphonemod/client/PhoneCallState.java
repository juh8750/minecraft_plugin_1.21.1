package org.example.smartphonemod.smartphonemod.client;

public final class PhoneCallState {

    private static boolean active = false;
    private static String targetName = "";
    private static String targetNumber = "";
    private static long startedAtMillis = 0L;

    private PhoneCallState() {
    }

    public static void startCall(String name, String number) {
        active = true;
        targetName = name;
        targetNumber = number;
        startedAtMillis = System.currentTimeMillis();
    }

    public static void endCall() {
        active = false;
        targetName = "";
        targetNumber = "";
        startedAtMillis = 0L;
    }

    public static boolean isActive() {
        return active;
    }

    public static String getTargetName() {
        return targetName;
    }

    public static String getTargetNumber() {
        return targetNumber;
    }

    public static String getElapsedTimeText() {
        if (!active || startedAtMillis <= 0L) {
            return "00:00";
        }

        long elapsedSeconds = (System.currentTimeMillis() - startedAtMillis) / 1000L;
        long minutes = elapsedSeconds / 60L;
        long seconds = elapsedSeconds % 60L;

        return String.format("%02d:%02d", minutes, seconds);
    }
}