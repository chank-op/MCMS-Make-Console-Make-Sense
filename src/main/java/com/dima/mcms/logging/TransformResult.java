package com.dima.mcms.logging;

import org.apache.logging.log4j.Level;

public class TransformResult {

    private final String message;
    private final boolean modified;
    private final Level level;

    private TransformResult(String message, boolean modified, Level level) {
        this.message = message;
        this.modified = modified;
        this.level = level;
    }

    public static TransformResult unchanged() {
        return new TransformResult(null, false, null);
    }

    public static TransformResult of(String message) {
        return new TransformResult(message, true, null);
    }

    public static TransformResult of(String message, Level level) {
        return new TransformResult(message, true, level);
    }

    public String getMessage() {
        return message;
    }

    public boolean isModified() {
        return modified;
    }

    public Level getLevel() {
        return level;
    }
}
