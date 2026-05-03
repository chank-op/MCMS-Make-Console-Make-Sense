package com.dima.mcms;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class StartupBanner {

    private StartupBanner() {}

    public static void print(String version) {
        List<String> art = loadBanner();
        boolean color = ConsoleStyle.isSupported();

        System.out.println();

        if (color) {
            String[] grad = ConsoleStyle.BANNER_GRADIENT;
            for (int i = 0; i < art.size(); i++) {
                System.out.println(ConsoleStyle.BOLD + grad[i % grad.length] + art.get(i) + ConsoleStyle.RESET);
            }
            System.out.println();
            System.out.println(
                "  " + ConsoleStyle.BOLD + ConsoleStyle.BRIGHT_CYAN + "Make Console Make Sense" + ConsoleStyle.RESET
                + ConsoleStyle.PURPLE + " v" + version + ConsoleStyle.RESET
                + ConsoleStyle.BRIGHT_GREEN + "  — active & judging every single error on your server" + ConsoleStyle.RESET
            );
        } else {
            for (String line : art) {
                System.out.println(line);
            }
            System.out.println();
            System.out.println("  Make Console Make Sense v" + version + " — active");
        }

        System.out.println();
    }

    public static void printShutdown() {
        if (!ConsoleStyle.isSupported()) {
            System.out.println("  [MCMS] Disabled. Console goes back to being unreadable. You're on your own.");
            return;
        }
        System.out.println(
            "  " + ConsoleStyle.BOLD + ConsoleStyle.PURPLE + "[MCMS]" + ConsoleStyle.RESET
            + ConsoleStyle.CYAN + " Disabled. Console goes back to being unreadable. You're on your own." + ConsoleStyle.RESET
        );
    }

    private static List<String> loadBanner() {
        List<String> lines = new ArrayList<>();
        try (InputStream is = StartupBanner.class.getResourceAsStream("/banner.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            lines.add("  MCMS — Make Console Make Sense");
        }
        return lines;
    }
}
