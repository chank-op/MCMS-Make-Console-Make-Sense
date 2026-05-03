package com.dima.mcms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class StartupBanner {

    private static final Logger log = LogManager.getLogger("MCMS");

    private StartupBanner() {}

    public static void print(String version) {
        List<String> art = loadBanner();
        boolean color = ConsoleStyle.isSupported();

        log.info("");

        if (color) {
            String[] grad = ConsoleStyle.BANNER_GRADIENT;
            for (int i = 0; i < art.size(); i++) {
                log.info(ConsoleStyle.BOLD + grad[i % grad.length] + art.get(i) + ConsoleStyle.RESET);
            }
            log.info("");
            log.info("  " + ConsoleStyle.BOLD + ConsoleStyle.BRIGHT_CYAN + "Make Console Make Sense" + ConsoleStyle.RESET
                + ConsoleStyle.PURPLE + " v" + version + ConsoleStyle.RESET
                + ConsoleStyle.BRIGHT_GREEN + "  — active & judging every single error on your server" + ConsoleStyle.RESET);
        } else {
            for (String line : art) {
                log.info(line);
            }
            log.info("");
            log.info("  Make Console Make Sense v" + version + " — active");
        }

        log.info("");
    }

    public static void printShutdown() {
        if (!ConsoleStyle.isSupported()) {
            log.info("  [MCMS] Disabled. Console goes back to being unreadable. You're on your own.");
            return;
        }
        log.info("  " + ConsoleStyle.BOLD + ConsoleStyle.PURPLE + "[MCMS]" + ConsoleStyle.RESET
            + ConsoleStyle.CYAN + " Disabled. Console goes back to being unreadable. You're on your own." + ConsoleStyle.RESET);
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
