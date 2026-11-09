package dev.tentativerpg.client.npc;

final class Typewriter {
    static int revealed(int ticks) {
        return ticks / 2 + 1;
    }

    static int length(String text) {
        return text.codePointCount(0, text.length());
    }

    static String prefix(String text, int characters) {
        return text.substring(0, text.offsetByCodePoints(0, Math.min(characters, length(text))));
    }

    static boolean cursorVisible(int ticks) {
        return ticks % 12 < 6;
    }

    private Typewriter() {}
}
