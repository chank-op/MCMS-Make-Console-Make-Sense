package com.dima.mcms.logging;

import org.apache.logging.log4j.Level;

import java.util.List;

public class KnownPatterns {

    public static List<PatternEntry> build() {
        return List.of(

            // ════════════════════════════════════════════════════════════════════════
            // NETWORK / CONNECTIONS
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "java\\.net\\.SocketException: Connection reset",
                m -> "A player ragequit so hard they broke TCP. Respect. RIP connection."
            ),
            new PatternEntry(
                "java\\.net\\.SocketTimeoutException",
                m -> "A player went to get a snack and never came back. Their connection waited... and waited... and gave up."
            ),
            new PatternEntry(
                "java\\.io\\.IOException: Broken pipe",
                m -> "The internet tube connecting a player just snapped like a twig. Gone. Poof."
            ),
            new PatternEntry(
                "java\\.io\\.IOException: Connection reset by peer",
                m -> "A player slammed the disconnect button so hard the server felt it personally."
            ),
            new PatternEntry(
                "java\\.io\\.IOException: An existing connection was forcibly closed by the remote host",
                m -> "Windows player closed the game through Task Manager like an animal."
            ),
            new PatternEntry(
                "java\\.net\\.ConnectException: Connection refused",
                m -> "Tried to connect somewhere and got told NO. Door was locked."
            ),
            new PatternEntry(
                "java\\.net\\.UnknownHostException: (.+)",
                m -> "Tried to find server '" + m.group(1).trim() + "' on the internet. It doesn't exist. Or DNS is dead again."
            ),
            new PatternEntry(
                "java\\.net\\.NoRouteToHostException",
                m -> "Tried to reach a host but the network said 'nope, not today.'"
            ),

            // ════════════════════════════════════════════════════════════════════════
            // SERVER PERFORMANCE
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "Can't keep up! Is the server overloaded\\? Running (\\d+)ms? or (\\d+) ticks? behind",
                m -> "ALERT: Server is gasping for air! Running " + m.group(1) + "ms (" + m.group(2)
                    + " ticks) behind. Turn off the 500-hopper contraptions, please.",
                Level.WARN
            ),
            new PatternEntry(
                "Can't keep up! Is the server overloaded\\? Running (\\d+)ms? behind",
                m -> "ALERT: Server is sweating. Running " + m.group(1) + "ms behind schedule. Someone's lag machine is working perfectly.",
                Level.WARN
            ),
            new PatternEntry(
                "Skipping (\\d+) tick(?:s)? \\(skipping (.+?)\\)",
                m -> "Server skipped " + m.group(1) + " ticks (" + m.group(2) + "). Time just... disappeared.",
                Level.WARN
            ),
            new PatternEntry(
                "Handling a new connection took too long",
                m -> "A player took forever to connect. Either they have dial-up or the server is struggling.",
                Level.WARN
            ),

            // ════════════════════════════════════════════════════════════════════════
            // PLUGIN SYSTEM
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "Error occurred while enabling (.+?) \\(Is it up to date\\?\\)",
                m -> "Plugin '" + m.group(1).trim() + "' walked into the server and immediately face-planted. It is NOT enabled. Check for updates or bugs.",
                Level.ERROR
            ),
            new PatternEntry(
                "Error occurred while disabling (.+?) \\(Is it up to date\\?\\)",
                m -> "Plugin '" + m.group(1).trim() + "' couldn't even quit without causing a scene. Dramatic to the end.",
                Level.ERROR
            ),
            new PatternEntry(
                "Error occurred while enabling (.+)",
                m -> "Plugin '" + m.group(1).trim() + "' exploded on startup. Someone should probably fix that.",
                Level.ERROR
            ),
            new PatternEntry(
                "Nag author(?:\\(s\\))?: '(.+?)' of '(.+?)' about the following: (.+)",
                m -> "Hey dev of '" + m.group(2).trim() + "': your plugin uses deprecated API. It's not 2015 anymore. Update your code, '" + m.group(1).trim() + "'!",
                Level.WARN
            ),
            new PatternEntry(
                "Plugin (.+?) attempted to register task while (disabled|not enabled)",
                m -> "Plugin '" + m.group(1).trim() + "' tried to schedule work while it was " + m.group(2) + ". Bold move. Ignored.",
                Level.WARN
            ),
            new PatternEntry(
                "Could not load '(.+?)' in folder '(.+?)'",
                m -> "Couldn't load '" + m.group(1) + "' from '" + m.group(2) + "'. Did you drag in a random .jar from 2013?",
                Level.ERROR
            ),
            new PatternEntry(
                "Plugin (.+?) attempted to call (.+?) from a? ?non-?(?:primary|main)? ?(?:server)? ?thread",
                m -> "Plugin '" + m.group(1).trim() + "' tried to run Bukkit stuff from the wrong thread. This is dangerous. They know what they did.",
                Level.WARN
            ),
            new PatternEntry(
                "Plugin (.+?) generated an exception while executing task (\\d+)",
                m -> "Plugin '" + m.group(1).trim() + "' blew up during a scheduled task (#" + m.group(2) + "). The task is cancelled. The developer is (probably) embarrassed.",
                Level.ERROR
            ),
            new PatternEntry(
                "Unhandled exception(?:s)? in scheduler run for (.+)",
                m -> "Plugin '" + m.group(1).trim() + "' threw an unhandled exception in the scheduler. Wild.",
                Level.ERROR
            ),
            new PatternEntry(
                "Plugin (.+?) has failed to register events for (.+?) because (.+?) does not exist",
                m -> "Plugin '" + m.group(1).trim() + "' tried to listen to class '" + m.group(2).trim() + "' which doesn't exist. Someone can't read a classpath.",
                Level.ERROR
            ),
            new PatternEntry(
                "Loading libraries, please wait\\.\\.\\.",
                m -> "Downloading plugin libraries... yes this takes a minute. Go get a coffee.",
                Level.INFO
            ),
            new PatternEntry(
                "Enabling (.+?) v(.+)",
                m -> "Waking up plugin: " + m.group(1).trim() + " v" + m.group(2).trim(),
                Level.INFO
            ),
            new PatternEntry(
                "Disabling (.+?) v(.+)",
                m -> "Putting plugin to sleep: " + m.group(1).trim() + " v" + m.group(2).trim(),
                Level.INFO
            ),

            // ════════════════════════════════════════════════════════════════════════
            // WORLD / CHUNKS
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "Skipping BlockEntity with id (\\S+)",
                m -> "Found a mysterious block entity '" + m.group(1) + "' that doesn't exist anymore. Probably from a mod someone installed, loved for a week, and uninstalled.",
                Level.WARN
            ),
            new PatternEntry(
                "Skipping Entity with id (\\S+)",
                m -> "Found a ghost entity '" + m.group(1) + "' from a plugin/mod that no longer exists. It's been yeeted from existence.",
                Level.WARN
            ),
            new PatternEntry(
                "Keeping entity (\\S+) that already exists with UUID ([a-f0-9\\-]+)",
                m -> "Two entities tried to share the same UUID like sharing a social security number. That's illegal. Keeping the original. Kicking the clone.",
                Level.WARN
            ),
            new PatternEntry(
                "(?:Loading|Preparing) level \"(.+?)\"",
                m -> "Loading world: " + m.group(1) + " (please don't have too many entities...)",
                Level.INFO
            ),
            new PatternEntry(
                "Preparing spawn area: (\\d+)%",
                m -> "Generating spawn chunks: " + m.group(1) + "% done... almost there...",
                Level.INFO
            ),
            new PatternEntry(
                "Time elapsed: (\\d+) ms",
                m -> "That operation took " + m.group(1) + "ms. " + (Long.parseLong(m.group(1)) > 5000 ? "Yeah that was slow." : "Not bad."),
                Level.INFO
            ),
            new PatternEntry(
                "Chunk file at (-?\\d+),(-?\\d+) is in the wrong location",
                m -> "Chunk at " + m.group(1) + "," + m.group(2) + " is having an identity crisis — it's stored in the wrong file. Minecraft will fix it (probably).",
                Level.WARN
            ),
            new PatternEntry(
                "Failed to load chunk at \\[(-?\\d+), (-?\\d+)\\]",
                m -> "Couldn't load chunk at " + m.group(1) + "," + m.group(2) + ". It might be corrupted. F.",
                Level.ERROR
            ),
            new PatternEntry(
                "Failed to save chunk at (-?\\d+),(-?\\d+)",
                m -> "Couldn't save chunk at " + m.group(1) + "," + m.group(2) + ". Hope nobody was building there.",
                Level.ERROR
            ),
            new PatternEntry(
                "Saved (\\d+) chunks",
                m -> "Auto-saved " + m.group(1) + " chunks. Your builds are safe (for now).",
                Level.INFO
            ),

            // ════════════════════════════════════════════════════════════════════════
            // PLAYER ACTIONS & ANTI-CHEAT NOISE
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "(\\S+)\\[(/.+?)\\] logged in with entity id (\\d+) at \\((.+?)\\)",
                m -> "Player '" + m.group(1) + "' joined from " + m.group(2) + ". Welcome... or whatever.",
                Level.INFO
            ),
            new PatternEntry(
                "(\\S+) lost connection: (.+)",
                m -> "Player '" + m.group(1) + "' left: " + m.group(2).trim() + ". Another one bites the dust.",
                Level.INFO
            ),
            new PatternEntry(
                "(\\S+) was kicked for: (.+)",
                m -> "Player '" + m.group(1) + "' got the boot: " + m.group(2).trim() + ". Buh-bye.",
                Level.INFO
            ),
            new PatternEntry(
                "(\\S+) moved too quickly! (.+)",
                m -> "Player '" + m.group(1) + "' is moving suspiciously fast (" + m.group(2).trim() + "). Definitely not hacking. 100%.",
                Level.WARN
            ),
            new PatternEntry(
                "(\\S+) moved wrongly!",
                m -> "Player '" + m.group(1) + "' tried to teleport themselves somewhere illegal. Nice try.",
                Level.WARN
            ),
            new PatternEntry(
                "Invalid move player packet received",
                m -> "Received an illegal movement packet from a player. Could be lag, could be cheats. We'll never know.",
                Level.WARN
            ),
            new PatternEntry(
                "(\\S+)\\[(.+?)\\] was denied access to command '(.+?)'",
                m -> "Player '" + m.group(1) + "' tried to run '" + m.group(3) + "' without permission. The audacity.",
                Level.WARN
            ),
            new PatternEntry(
                "Unknown or incomplete command, see below for error.*",
                m -> "Someone typed a command that doesn't exist. Sad.",
                Level.WARN
            ),
            new PatternEntry(
                "(\\S+) fell out of the world",
                m -> "Player '" + m.group(1) + "' found the void. The void found them back.",
                Level.INFO
            ),
            new PatternEntry(
                "Disconnecting (\\S+) \\[(.+?)\\]: (.+)",
                m -> "Disconnecting player '" + m.group(1) + "': " + m.group(3).trim(),
                Level.INFO
            ),

            // ════════════════════════════════════════════════════════════════════════
            // AUTHENTICATION / MOJANG
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "UUID of player (\\S+) is ([a-f0-9\\-]+)",
                m -> "Player '" + m.group(1) + "' is legit (UUID: " + m.group(2) + "). Mojang says so.",
                Level.INFO
            ),
            new PatternEntry(
                "Failed to verify username(?::? (.+))?",
                m -> "Couldn't verify a player with Mojang's auth servers. Either Mojang is down (again) or someone's being sneaky.",
                Level.WARN
            ),
            new PatternEntry(
                "com\\.mojang\\.authlib\\.exceptions\\.AuthenticationException: (.+)",
                m -> "Mojang authentication blew up: " + m.group(1).trim() + ". Is session.minecraft.net down? Check the Mojang status page.",
                Level.WARN
            ),
            new PatternEntry(
                "Session servers are unavailable",
                m -> "Mojang's session servers are having a bad day. Players may not be able to authenticate. Classic Mojang.",
                Level.WARN
            ),
            new PatternEntry(
                "com\\.mojang\\.authlib\\.exceptions\\.AuthenticationUnavailableException",
                m -> "Can't reach Mojang's auth servers right now. Could be a blip. Could be Tuesday. Check status.mojang.com.",
                Level.WARN
            ),

            // ════════════════════════════════════════════════════════════════════════
            // MEMORY / JVM
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "java\\.lang\\.OutOfMemoryError: Java heap space",
                m -> "MAYDAY! SERVER IS OUT OF HEAP MEMORY! Allocate more RAM with -Xmx or something is leaking memory like a sieve!",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.OutOfMemoryError: GC overhead limit exceeded",
                m -> "The garbage collector is working so hard it's barely doing anything else. Server is basically out of memory. Allocate more RAM!",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.OutOfMemoryError: Metaspace",
                m -> "The JVM Metaspace is full. Too many classes loaded. Probably a plugin loading classes and never cleaning up.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.OutOfMemoryError(.+)?",
                m -> "SERVER IS OUT OF MEMORY! Everything is on fire. Allocate more RAM immediately!",
                Level.ERROR
            ),
            new PatternEntry(
                "Garbage collecting\\.\\.\\.",
                m -> "JVM is taking out the trash (garbage collection). Momentary lag incoming, probably.",
                Level.INFO
            ),
            new PatternEntry(
                "There are (\\d+) out of maximum (\\d+) players online",
                m -> "Players online: " + m.group(1) + "/" + m.group(2) + ".",
                Level.INFO
            ),

            // ════════════════════════════════════════════════════════════════════════
            // COMMON JAVA EXCEPTIONS (uncaught, appearing as plain messages)
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "java\\.lang\\.NullPointerException(?:: (.+))?",
                m -> "NullPointerException" + (m.group(1) != null ? ": " + m.group(1).trim() : "") + " — someone used something that was null. The timeless developer blunder.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.StackOverflowError",
                m -> "STACK OVERFLOW! A method called itself until it forgot who it was. Infinite recursion detected. It's turtles all the way down.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.ClassNotFoundException: (.+)",
                m -> "Class '" + m.group(1).trim() + "' was supposed to be here but skipped class. Probably a missing dependency.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.ClassCastException: (.+)",
                m -> "Tried to treat one type as another: " + m.group(1).trim() + ". A cat is not a dog. An Integer is not a String.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.ArrayIndexOutOfBoundsException: Index (\\d+) out of bounds for length (\\d+)",
                m -> "Tried to access element #" + m.group(1) + " in an array of length " + m.group(2) + ". The array is not that big. Math is hard.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.StringIndexOutOfBoundsException: (.+)",
                m -> "String index out of bounds: " + m.group(1).trim() + ". The string is shorter than expected. Somebody miscounted.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.IndexOutOfBoundsException: Index: (\\d+), Size: (\\d+)",
                m -> "Tried to access index " + m.group(1) + " in a collection of size " + m.group(2) + ". Off by one? Off by more? Who knows.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.IndexOutOfBoundsException: (.+)",
                m -> "Index out of bounds: " + m.group(1).trim() + ". Someone's counting is off.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.ArithmeticException: / by zero",
                m -> "DIVIDED BY ZERO! The math exploded. Someone forgot to check for zero before dividing.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.ArithmeticException: (.+)",
                m -> "Math error: " + m.group(1).trim() + ". Numbers behaved unexpectedly.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.IllegalArgumentException: (.+)",
                m -> "A method received an argument it absolutely did not want: " + m.group(1).trim(),
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.IllegalStateException: (.+)",
                m -> "The code reached a state it wasn't prepared for: " + m.group(1).trim() + ". It's having an existential crisis.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.UnsupportedOperationException(?:: (.+))?",
                m -> "Someone called a method that exists but doesn't actually do anything"
                    + (m.group(1) != null ? ": " + m.group(1).trim() : "") + ". Bold move.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.util\\.ConcurrentModificationException",
                m -> "Two threads tried to modify the same collection at the same time. Chaos erupted. Somebody needs to use synchronization.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.NumberFormatException: For input string: \"(.+?)\"",
                m -> "Tried to turn '" + m.group(1) + "' into a number. It is not a number. It never was a number.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.NumberFormatException: (.+)",
                m -> "Number format error: " + m.group(1).trim() + ". That string is not a number.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.NegativeArraySizeException: (-?\\d+)",
                m -> "Tried to create an array with size " + m.group(1) + ". Arrays cannot have negative sizes. This defies reality.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.reflect\\.InvocationTargetException",
                m -> "A method called via reflection exploded. The actual error is the cause below (check the stack trace).",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.NoSuchMethodException: (.+)",
                m -> "Tried to call method '" + m.group(1).trim() + "' but it doesn't exist. Probably a version mismatch.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.NoSuchFieldException: (.+)",
                m -> "Tried to access field '" + m.group(1).trim() + "' but it doesn't exist. Someone's reflection code is out of date.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.NoClassDefFoundError: (.+)",
                m -> "Class '" + m.group(1).trim() + "' existed at compile time but is GONE at runtime. Missing dependency or jar conflict.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.VerifyError: (.+)",
                m -> "A class failed JVM verification: " + m.group(1).trim() + ". Corrupted jar? Wrong Java version? Weird.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.ExceptionInInitializerError",
                m -> "A class blew up during static initialization. It's dead before it even started. Check the caused-by exception.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.lang\\.AssertionError(?:: (.+))?",
                m -> "An assertion failed" + (m.group(1) != null ? ": " + m.group(1).trim() : "") + ". The code hit a condition it considered impossible. Plot twist: it's possible.",
                Level.ERROR
            ),

            // ════════════════════════════════════════════════════════════════════════
            // IO / FILES
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "java\\.io\\.FileNotFoundException: (.+)",
                m -> "File not found: '" + m.group(1).trim() + "'. Either it was deleted, never created, or the path is wrong.",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.io\\.EOFException",
                m -> "Tried to read past the end of a file or stream. The data ended before expected. Corrupted file?",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.io\\.IOException: No space left on device",
                m -> "DISK IS FULL! The server has run out of storage space. Delete something! Worlds, logs, old backups — something!",
                Level.ERROR
            ),
            new PatternEntry(
                "java\\.io\\.IOException: (.+)",
                m -> "IO error: " + m.group(1).trim() + ". Something went wrong reading or writing data.",
                Level.ERROR
            ),
            new PatternEntry(
                "Failed to save player data for (.+)",
                m -> "Couldn't save player data for '" + m.group(1).trim() + "'. Their progress might be lost. Oops.",
                Level.ERROR
            ),
            new PatternEntry(
                "Failed to load player data for (.+)",
                m -> "Couldn't load player data for '" + m.group(1).trim() + "'. They might spawn as a fresh player. Hope they didn't have diamonds.",
                Level.ERROR
            ),

            // ════════════════════════════════════════════════════════════════════════
            // THREAD / CONCURRENCY
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "A deadlock was detected",
                m -> "DEADLOCK DETECTED! Two (or more) threads are waiting on each other forever. Nothing will ever run again until this is fixed.",
                Level.ERROR
            ),
            new PatternEntry(
                "Thread (.+?) is (\\d+)ms? behind$",
                m -> "Thread '" + m.group(1).trim() + "' is running " + m.group(2) + "ms behind. It's struggling.",
                Level.WARN
            ),

            // ════════════════════════════════════════════════════════════════════════
            // WATCHDOG
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "The server has stopped responding!",
                m -> "SERVER IS FROZEN! The watchdog has noticed the server isn't responding. Brace for crash.",
                Level.ERROR
            ),
            new PatternEntry(
                "The server has not responded for (\\d+) seconds",
                m -> "Server hasn't responded in " + m.group(1) + " seconds! It might be dead. Or thinking really hard.",
                Level.ERROR
            ),
            new PatternEntry(
                "Crash report saved to: (.+)",
                m -> "Server crashed. Crash report saved to: " + m.group(1).trim() + " — send that to your developer.",
                Level.ERROR
            ),

            // ════════════════════════════════════════════════════════════════════════
            // NETTY / NETWORK LAYER
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "io\\.netty\\.handler\\.codec\\.DecoderException: (.+)",
                m -> "Received a malformed/corrupted packet from a client: " + m.group(1).trim() + ". Lag, bug, or hacked client.",
                Level.WARN
            ),
            new PatternEntry(
                "io\\.netty\\.channel\\.unix\\.Errors\\$NativeIoException: (.+)",
                m -> "Low-level network I/O error: " + m.group(1).trim() + ". Player probably just disconnected ungracefully.",
                Level.WARN
            ),
            new PatternEntry(
                "io\\.netty\\.handler\\.codec\\.CorruptedFrameException: (.+)",
                m -> "Got a corrupted network frame from a client: " + m.group(1).trim() + ". Probably a hacked client or severe lag.",
                Level.WARN
            ),
            new PatternEntry(
                "io\\.netty\\.channel\\.ChannelPipeline.*Exception: (.+)",
                m -> "Netty channel pipeline error: " + m.group(1).trim() + ". Network layer is complaining.",
                Level.WARN
            ),

            // ════════════════════════════════════════════════════════════════════════
            // VERSION / COMPATIBILITY
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "Outdated server! I'm still on (\\S+)",
                m -> "This server is running outdated version " + m.group(1) + ". Update for security fixes, bugfixes, and the ability to complain about new bugs instead.",
                Level.WARN
            ),
            new PatternEntry(
                "Outdated client! Please use (\\S+)",
                m -> "A player tried to join with an outdated client. They need version " + m.group(1) + ". Ancient relics cannot enter.",
                Level.WARN
            ),
            new PatternEntry(
                "This server is running (.+?) version (.+?) \\(MC: (.+?)\\) \\(Implementing API version (.+?)\\)",
                m -> "Server: " + m.group(1) + " v" + m.group(2) + " | Minecraft " + m.group(3) + " | API " + m.group(4),
                Level.INFO
            ),

            // ════════════════════════════════════════════════════════════════════════
            // YAML / CONFIG
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "org\\.bukkit\\.configuration\\.InvalidConfigurationException: (.+)",
                m -> "Config file has broken YAML: " + m.group(1).trim() + ". Open it in a YAML validator and fix the syntax. Tabs vs spaces is probably the culprit.",
                Level.ERROR
            ),
            new PatternEntry(
                "while parsing a block mapping.*line (\\d+), column (\\d+)",
                m -> "YAML parse error at line " + m.group(1) + ", column " + m.group(2) + ". Your config has a syntax error. Copy it into yamllint.com and fix it.",
                Level.ERROR
            ),
            new PatternEntry(
                "while scanning a simple key.*line (\\d+)",
                m -> "YAML scanner error at line " + m.group(1) + ". Probably a missing colon or bad indentation in your config file.",
                Level.ERROR
            ),

            // ════════════════════════════════════════════════════════════════════════
            // GENERAL SERVER STARTUP / SHUTDOWN
            // ════════════════════════════════════════════════════════════════════════

            new PatternEntry(
                "Starting minecraft server version (.+)",
                m -> "Booting up Minecraft " + m.group(1) + "... fingers crossed.",
                Level.INFO
            ),
            new PatternEntry(
                "Done \\((.+?)s?\\)! For help, type \"help\"",
                m -> "Server is LIVE! Took " + m.group(1) + "s to start. Type 'help' if you're lost.",
                Level.INFO
            ),
            new PatternEntry(
                "Stopping server",
                m -> "Server is shutting down. Goodbye cruel world.",
                Level.INFO
            ),
            new PatternEntry(
                "Saving players",
                m -> "Saving all player data... please don't pull the plug right now.",
                Level.INFO
            ),
            new PatternEntry(
                "Saving worlds",
                m -> "Saving all worlds... your builds will survive (probably).",
                Level.INFO
            ),
            new PatternEntry(
                "All chunks are saved",
                m -> "All chunks saved successfully. The world lives on.",
                Level.INFO
            )
        );
    }
}
