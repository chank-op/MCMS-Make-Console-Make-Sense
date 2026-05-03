package com.dima.mcms.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;

public class MCMSFilter extends AbstractFilter {

    private static final String MCMS_LOGGER_PREFIX = "MCMS";

    private final MessageTransformer transformer;
    private final Logger mcmsLogger;

    public MCMSFilter(MessageTransformer transformer) {
        super(Result.NEUTRAL, Result.NEUTRAL);
        this.transformer = transformer;
        this.mcmsLogger = LogManager.getLogger("MCMS");
    }

    @Override
    public Result filter(LogEvent event) {
        // Never intercept our own log output — prevents infinite loops
        if (event.getLoggerName() != null && event.getLoggerName().startsWith(MCMS_LOGGER_PREFIX)) {
            return Result.NEUTRAL;
        }

        Throwable thrown = event.getThrown();
        String message = event.getMessage().getFormattedMessage();

        if (thrown != null) {
            // Print a one-line summary before the full stack trace
            transformer.printExceptionSummary(thrown, message, event.getLevel());
            return Result.NEUTRAL; // still allow full trace to pass through to log file
        }

        TransformResult result = transformer.transform(message, event.getLevel());
        if (result.isModified()) {
            Level level = result.getLevel() != null ? result.getLevel() : event.getLevel();
            mcmsLogger.log(level, result.getMessage());
            return Result.DENY; // suppress the original cryptic message
        }

        return Result.NEUTRAL;
    }
}
