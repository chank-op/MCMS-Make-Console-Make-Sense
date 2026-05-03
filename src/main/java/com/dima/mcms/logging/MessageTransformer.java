package com.dima.mcms.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;

public class MessageTransformer {

    private static final Map<String, String> FUNNY_EXCEPTION_NAMES = Map.ofEntries(
        Map.entry("NullPointerException",              "Something was null that really shouldn't have been. Classic developer blunder."),
        Map.entry("StackOverflowError",                "A method called itself until the call stack exploded. Infinite recursion. It's turtles all the way down."),
        Map.entry("OutOfMemoryError",                  "THE SERVER IS OUT OF MEMORY! Allocate more RAM or start deleting things!"),
        Map.entry("ClassNotFoundException",            "A class was supposed to be here but skipped out. Missing dependency or typo."),
        Map.entry("ClassCastException",                "Code tried to treat one type as another. A cat is not a dog. An Integer is not a String."),
        Map.entry("ArrayIndexOutOfBoundsException",    "Tried to access an array slot that doesn't exist. Off-by-one error strikes again."),
        Map.entry("IndexOutOfBoundsException",         "Tried to access an element outside the collection's bounds. Somebody miscounted."),
        Map.entry("StringIndexOutOfBoundsException",   "The string was shorter than expected. Someone thought it was longer."),
        Map.entry("ConcurrentModificationException",   "Two threads modified the same collection simultaneously. Chaos. Use synchronization!"),
        Map.entry("ArithmeticException",               "Math exploded. Probably divided by zero somewhere."),
        Map.entry("IllegalArgumentException",          "A method received an argument it absolutely did not want."),
        Map.entry("IllegalStateException",             "Code reached a state it wasn't prepared for. Having an existential crisis."),
        Map.entry("UnsupportedOperationException",     "Someone called a method that exists but doesn't actually do anything. Bold."),
        Map.entry("NumberFormatException",             "Tried to parse a number from something that is very much not a number."),
        Map.entry("NoClassDefFoundError",              "Class existed at compile time but is GONE at runtime. Jar conflict or missing dependency."),
        Map.entry("NoSuchMethodException",             "Tried to call a method that doesn't exist. Version mismatch, probably."),
        Map.entry("NoSuchFieldException",              "Tried to access a field that doesn't exist. Reflection code is out of date."),
        Map.entry("AssertionError",                    "Code hit a condition it considered impossible. Plot twist: it's possible."),
        Map.entry("ExceptionInInitializerError",       "A class exploded during static initialization. Dead before it even started."),
        Map.entry("VerifyError",                       "JVM rejected a class as invalid. Corrupted jar or wrong Java version."),
        Map.entry("InvocationTargetException",         "A method called via reflection blew up. Check the cause below for the real error."),
        Map.entry("IOException",                       "Something went wrong reading or writing data."),
        Map.entry("FileNotFoundException",             "A file was expected here but has gone missing. Deleted? Wrong path?"),
        Map.entry("SocketException",                   "Network socket error — probably a player disconnecting badly."),
        Map.entry("SocketTimeoutException",            "Connection timed out waiting for a response that never came."),
        Map.entry("DecoderException",                  "Received a malformed network packet. Lag, bug, or hacked client."),
        Map.entry("RuntimeException",                  "An unspecified runtime error. Check the stack trace for details."),
        Map.entry("Exception",                         "A generic exception was thrown. Not very helpful, I know. Check the stack trace.")
    );

    private final List<PatternEntry> patterns;
    private final Logger mcmsLogger;

    private final AtomicLong transformedCount = new AtomicLong(0);
    private final AtomicLong exceptionsSummarized = new AtomicLong(0);

    public MessageTransformer(Logger mcmsLogger) {
        this.patterns = KnownPatterns.build();
        this.mcmsLogger = mcmsLogger;
    }

    public TransformResult transform(String message, Level originalLevel) {
        for (PatternEntry entry : patterns) {
            Matcher matcher = entry.getPattern().matcher(message);
            if (matcher.find()) {
                String transformed = entry.getReplacement().apply(matcher);
                Level level = entry.getLevelOverride() != null ? entry.getLevelOverride() : originalLevel;
                transformedCount.incrementAndGet();
                return TransformResult.of(transformed, level);
            }
        }
        return TransformResult.unchanged();
    }

    /**
     * Prints a human-readable (and slightly sarcastic) summary of an exception.
     * The original full stack trace is still allowed through to the log file.
     */
    public void printExceptionSummary(Throwable thrown, String logMessage, Level level) {
        exceptionsSummarized.incrementAndGet();

        Throwable root = getRootCause(thrown);
        String simpleName = root.getClass().getSimpleName();
        String funnyDesc = FUNNY_EXCEPTION_NAMES.getOrDefault(simpleName,
            "An exception of type '" + simpleName + "' was thrown. Someone's having a bad time.");
        String exceptionMsg = root.getMessage() != null ? root.getMessage() : "(no message)";

        StackTraceElement frame = findMostRelevantFrame(root);
        String location = frame != null
            ? frame.getFileName() + ":" + frame.getLineNumber() + " in " + frame.getClassName() + "." + frame.getMethodName() + "()"
            : "unknown location";

        mcmsLogger.log(level, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        if (!logMessage.isBlank()) {
            mcmsLogger.log(level, "[MCMS] CRASH CONTEXT: " + logMessage);
        }
        mcmsLogger.log(level, "[MCMS] WHAT: " + funnyDesc);
        mcmsLogger.log(level, "[MCMS] TYPE: " + root.getClass().getName());
        mcmsLogger.log(level, "[MCMS] MSG:  " + exceptionMsg);
        mcmsLogger.log(level, "[MCMS] WHERE: " + location);
        mcmsLogger.log(level, "[MCMS] Full stack trace follows:");
        mcmsLogger.log(level, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    public long getTransformedCount() {
        return transformedCount.get();
    }

    public long getExceptionsSummarized() {
        return exceptionsSummarized.get();
    }

    private Throwable getRootCause(Throwable t) {
        Throwable cause = t;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }

    private StackTraceElement findMostRelevantFrame(Throwable t) {
        for (StackTraceElement frame : t.getStackTrace()) {
            String cls = frame.getClassName();
            if (!cls.startsWith("java.")
                && !cls.startsWith("javax.")
                && !cls.startsWith("sun.")
                && !cls.startsWith("com.sun.")
                && !cls.startsWith("io.netty.")
                && !cls.startsWith("org.bukkit.")
                && !cls.startsWith("net.minecraft.")
                && !cls.startsWith("com.mojang.")) {
                return frame;
            }
        }
        return t.getStackTrace().length > 0 ? t.getStackTrace()[0] : null;
    }
}
