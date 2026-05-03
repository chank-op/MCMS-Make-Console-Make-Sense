package com.dima.mcms;

public final class ConsoleStyle {

    // Standard colors
    public static final String RESET          = "[0m";
    public static final String BOLD           = "[1m";
    public static final String RED            = "[31m";
    public static final String GREEN          = "[32m";
    public static final String YELLOW         = "[33m";
    public static final String BLUE           = "[34m";
    public static final String PURPLE         = "[35m";
    public static final String CYAN           = "[36m";
    public static final String WHITE          = "[37m";

    // Bright variants
    public static final String BRIGHT_RED     = "[91m";
    public static final String BRIGHT_GREEN   = "[92m";
    public static final String BRIGHT_YELLOW  = "[93m";
    public static final String BRIGHT_BLUE    = "[94m";
    public static final String BRIGHT_PURPLE  = "[95m";
    public static final String BRIGHT_CYAN    = "[96m";
    public static final String BRIGHT_WHITE   = "[97m";

    // Gradient used across the ASCII art banner (top → bottom)
    public static final String[] BANNER_GRADIENT = {
        BRIGHT_PURPLE,
        BRIGHT_PURPLE,
        PURPLE,
        CYAN,
        BRIGHT_CYAN,
        BRIGHT_CYAN,
        CYAN,
        PURPLE,
        BRIGHT_PURPLE,
        BRIGHT_PURPLE,
        PURPLE,
    };

    private ConsoleStyle() {}

    /**
     * Returns true if the current console is likely to render ANSI escape codes.
     * Checks for Paper/Spigot's jline3 terminal first (most reliable), then falls
     * back to environment variable heuristics.
     */
    public static boolean isSupported() {
        // Paper and Spigot ship TerminalConsoleAppender (jline3) — ANSI always works there
        try {
            Class.forName("net.minecrell.terminalconsole.TerminalConsoleAppender");
            return true;
        } catch (ClassNotFoundException ignored) {}

        // Generic fallbacks
        if (System.console() != null) return true;

        String term = System.getenv("TERM");
        if (term != null && !term.equalsIgnoreCase("dumb")) return true;

        if (System.getenv("COLORTERM") != null) return true;
        if (System.getenv("WT_SESSION")  != null) return true; // Windows Terminal
        if (System.getenv("TERM_PROGRAM") != null) return true; // iTerm2, VS Code, etc.

        return false;
    }
}
