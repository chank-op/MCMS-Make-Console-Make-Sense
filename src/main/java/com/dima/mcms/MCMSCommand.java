package com.dima.mcms;

import com.dima.mcms.logging.MessageTransformer;
import com.dima.mcms.updater.UpdateChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MCMSCommand implements CommandExecutor {

    private final MCMSPlugin plugin;

    public MCMSCommand(MCMSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        MessageTransformer transformer = plugin.getTransformer();
        String sub = args.length > 0 ? args[0].toLowerCase() : "stats";

        switch (sub) {
            case "stats" -> {
                sender.sendMessage("§6[MCMS] Make Console Make Sense v" + plugin.getDescription().getVersion());
                sender.sendMessage("§7  Messages transformed: §a" + transformer.getTransformedCount());
                sender.sendMessage("§7  Exceptions summarized: §a" + transformer.getExceptionsSummarized());
                sender.sendMessage("§7  Status: §aActive");
            }
            case "update" -> {
                if (!sender.hasPermission("mcms.admin")) {
                    sender.sendMessage("§c[MCMS] You don't have permission to do that.");
                    return true;
                }
                String repo = plugin.getConfig().getString("github-repo", "");
                if (repo.isBlank() || repo.equals("chank-op/MCMS-Make-Console-Make-Sense")) {
                    sender.sendMessage("§c[MCMS] Set 'github-repo' in config.yml first.");
                    return true;
                }
                sender.sendMessage("§7[MCMS] Checking for updates... (check the console)");
                new UpdateChecker(plugin, repo).checkAsync();
            }
            case "reload" -> {
                if (!sender.hasPermission("mcms.admin")) {
                    sender.sendMessage("§c[MCMS] You don't have permission to do that.");
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage("§a[MCMS] Config reloaded.");
            }
            case "help" -> {
                sender.sendMessage("§6[MCMS] Commands:");
                sender.sendMessage("§7  /mcms stats   §8— §fshow transformation statistics");
                sender.sendMessage("§7  /mcms update  §8— §fcheck for and download updates");
                sender.sendMessage("§7  /mcms reload  §8— §freload config.yml");
                sender.sendMessage("§7  /mcms help    §8— §fshow this help");
            }
            default -> sender.sendMessage("§c[MCMS] Unknown subcommand. Use /mcms help.");
        }
        return true;
    }
}
