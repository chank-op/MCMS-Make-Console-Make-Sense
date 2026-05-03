package com.dima.mcms.updater;

import com.dima.mcms.ConsoleStyle;
import com.dima.mcms.MCMSPlugin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class UpdateChecker {

    private static final String API_URL = "https://api.github.com/repos/%s/releases/latest";

    private final MCMSPlugin plugin;
    private final String repo;

    public UpdateChecker(MCMSPlugin plugin, String repo) {
        this.plugin = plugin;
        this.repo = repo;
    }

    public void checkAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::check);
    }

    private void check() {
        try {
            HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(API_URL, repo)))
                .header("Accept", "application/vnd.github.v3+json")
                .header("User-Agent", "MCMS-UpdateChecker")
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                plugin.getLogger().warning("[MCMS] Repo not found. Set 'github-repo' in config.yml.");
                return;
            }
            if (response.statusCode() != 200) {
                plugin.getLogger().warning("[MCMS] Update check failed (HTTP " + response.statusCode() + ").");
                return;
            }

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            String latestTag = json.get("tag_name").getAsString();
            String latestVersion = latestTag.startsWith("v") ? latestTag.substring(1) : latestTag;
            String currentVersion = plugin.getDescription().getVersion();

            if (isNewer(latestVersion, currentVersion)) {
                String url = "https://github.com/" + repo + "/releases/latest";
                printUpdateNotice(currentVersion, latestVersion, url);
            } else {
                plugin.getLogger().info("[MCMS] Up to date (v" + currentVersion + "). Nice.");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            plugin.getLogger().warning("[MCMS] Couldn't check for updates: " + e.getMessage());
        }
    }

    private void printUpdateNotice(String current, String latest, String url) {
        boolean color = ConsoleStyle.isSupported();

        if (color) {
            String B = ConsoleStyle.BOLD;
            String R = ConsoleStyle.RESET;
            String Y = ConsoleStyle.YELLOW;

            System.out.println();
            System.out.println(Y + "  ╔══════════════════════════════════════════════════════════╗" + R);
            System.out.println(Y + "  ║" + B + "  [MCMS] Update available!                               " + R + Y + "  ║" + R);
            System.out.println(Y + "  ║" + R + "                                                          " + Y + "║" + R);
            System.out.println(Y + "  ║" + R + "  You're running:  v" + padRight(current, 38) + Y + "║" + R);
            System.out.println(Y + "  ║" + R + "  Latest version:  v" + padRight(latest,  38) + Y + "║" + R);
            System.out.println(Y + "  ║" + R + "                                                          " + Y + "║" + R);
            System.out.println(Y + "  ║" + R + "  Your console isn't going to read itself, you know.     " + Y + "║" + R);
            System.out.println(Y + "  ║" + R + "  Go update. We'll wait.                                  " + Y + "║" + R);
            System.out.println(Y + "  ║" + R + "                                                          " + Y + "║" + R);
            System.out.println(Y + "  ║" + R + "  " + padRight(url, 56) + Y + "║" + R);
            System.out.println(Y + "  ╚══════════════════════════════════════════════════════════╝" + R);
            System.out.println();
        } else {
            System.out.println();
            System.out.println("  +----------------------------------------------------------+");
            System.out.println("  |      YO. MCMS HAS AN UPDATE. OPEN YOUR EYES.            |");
            System.out.println("  |                                                          |");
            System.out.println("  |  You're running:  v" + padRight(current, 39) + "|");
            System.out.println("  |  Latest version:  v" + padRight(latest,  39) + "|");
            System.out.println("  |                                                          |");
            System.out.println("  |  Your console isn't going to read itself, you know.     |");
            System.out.println("  |  Go update. We spent time on the new version. Please.   |");
            System.out.println("  |                                                          |");
            System.out.println("  |  " + padRight(url, 56) + "  |");
            System.out.println("  +----------------------------------------------------------+");
            System.out.println();
        }
    }

    private boolean isNewer(String latest, String current) {
        try {
            String[] l = latest.split("\\.");
            String[] c = current.split("\\.");
            int len = Math.max(l.length, c.length);
            for (int i = 0; i < len; i++) {
                int lv = i < l.length ? Integer.parseInt(l[i].replaceAll("[^0-9]", "")) : 0;
                int cv = i < c.length ? Integer.parseInt(c[i].replaceAll("[^0-9]", "")) : 0;
                if (lv != cv) return lv > cv;
            }
        } catch (NumberFormatException ignored) {}
        return false;
    }

    private String pad(String s, int n) {
        if (n <= 0) return "";
        return s + " ".repeat(Math.max(0, n - s.length()));
    }

    private String padRight(String s, int n) {
        if (s.length() >= n) return s.substring(0, n);
        return s + " ".repeat(n - s.length());
    }
}
