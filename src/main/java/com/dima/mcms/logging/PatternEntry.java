package com.dima.mcms.logging;

import org.apache.logging.log4j.Level;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternEntry {

    private final Pattern pattern;
    private final Function<Matcher, String> replacement;
    private final Level levelOverride;

    public PatternEntry(String regex, Function<Matcher, String> replacement) {
        this(regex, replacement, null);
    }

    public PatternEntry(String regex, Function<Matcher, String> replacement, Level levelOverride) {
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        this.replacement = replacement;
        this.levelOverride = levelOverride;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Function<Matcher, String> getReplacement() {
        return replacement;
    }

    public Level getLevelOverride() {
        return levelOverride;
    }
}
