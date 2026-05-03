package com.dima.mcms;

import com.dima.mcms.logging.MCMSFilter;
import com.dima.mcms.logging.MessageTransformer;
import com.dima.mcms.updater.UpdateChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.bukkit.plugin.java.JavaPlugin;

public class MCMSPlugin extends JavaPlugin {

    private static MCMSPlugin instance;
    private MCMSFilter filter;
    private MessageTransformer transformer;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        Logger mcmsLogger = LogManager.getLogger("MCMS");
        transformer = new MessageTransformer(mcmsLogger);
        filter = new MCMSFilter(transformer);

        installFilter();

        StartupBanner.print(getDescription().getVersion());
        getCommand("mcms").setExecutor(new MCMSCommand(this));

        if (getConfig().getBoolean("check-for-updates", true)) {
            String repo = getConfig().getString("github-repo", "");
            if (!repo.isBlank() && !repo.equals("chank-op/MCMS-Make-Console-Make-Sense")) {
                new UpdateChecker(this, repo).checkAsync();
            } else {
                getLogger().warning("[MCMS] Update checks are on but 'github-repo' is not set in config.yml.");
            }
        }
    }

    @Override
    public void onDisable() {
        removeFilter();
        StartupBanner.printShutdown();
    }

    private void installFilter() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.getRootLogger().addFilter(filter);
    }

    private void removeFilter() {
        filter.deactivate();
    }

    public MessageTransformer getTransformer() {
        return transformer;
    }

    public static MCMSPlugin getInstance() {
        return instance;
    }
}
