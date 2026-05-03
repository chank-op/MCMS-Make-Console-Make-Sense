# Make Console Make Sense (MCMS)

> Turn cryptic Minecraft server console spam into something a human can actually read.

[![GitHub Release](https://img.shields.io/github/v/release/chank-op/MCMS-Make-Console-Make-Sense?style=flat-square)](https://github.com/chank-op/MCMS-Make-Console-Make-Sense/releases/latest)
[![Modrinth](https://img.shields.io/modrinth/dt/mcms?style=flat-square&logo=modrinth)](https://modrinth.com/plugin/mcms)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.18--1.21.x-brightgreen?style=flat-square)](https://papermc.io/)
[![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square)](https://adoptium.net/)
[![License](https://img.shields.io/badge/license-MIT-blue?style=flat-square)](LICENSE)

---

## What is this?

You know that feeling when your server console looks like this?

```log
[12:34:56 WARN]: java.net.SocketException: Connection reset
[12:34:56 ERROR]: java.lang.NullPointerException
[12:34:56 WARN]: Can't keep up! Is the server overloaded? Running 2034ms or 40 ticks behind
[12:34:56 WARN]: Skipping BlockEntity with id mysticmod:magic_chest
[12:34:56 WARN]: io.netty.handler.codec.DecoderException: Bad packet id 68
```

**MCMS makes it look like this instead:**

```log
[12:34:56 WARN]: A player ragequit so hard they broke TCP. Respect. RIP connection.
[12:34:56 ERROR]: ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
[12:34:56 ERROR]: [MCMS] WHAT: Something was null that really shouldn't have been. Classic developer blunder.
[12:34:56 ERROR]: [MCMS] WHERE: MyPlugin.java:42 in com.example.MyPlugin.doThing()
[12:34:56 ERROR]: [MCMS] Full stack trace follows:
[12:34:56 ERROR]: ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
[12:34:56 WARN]: ALERT: Server is gasping for air! Running 2034ms (40 ticks) behind. Turn off the 500-hopper contraptions, please.
[12:34:56 WARN]: Found a mysterious block entity 'mysticmod:magic_chest'. Probably from a mod someone installed, loved for a week, and uninstalled.
[12:34:56 WARN]: Received a malformed/corrupted packet from a client: Bad packet id 68. Lag, bug, or hacked client.
```

Same information. Actually readable. Slightly sarcastic.

---

## Features

- **70+ message patterns** covering every common server message
- **Funny, human-readable descriptions** — because if something breaks, it should at least be entertaining
- **Smart exception summaries** — root cause + exact file/line number shown first, full stack trace still follows
- **Zero config required** — drop the JAR in, it works immediately
- **Update checker** — notifies you loudly in the console when a new version is out
- **Works on everything** — Bukkit, Spigot, Paper, Purpur, Folia, and all forks (1.18+)

### What gets transformed?

| Category | Examples |
| --- | --- |
| Network errors | SocketException, SocketTimeout, Broken pipe, Connection reset |
| Server lag | "Can't keep up!", skipped ticks |
| Plugin errors | Enable/disable failures, deprecated API warnings, wrong-thread calls |
| World/Chunks | Unknown block entities, duplicate UUIDs, chunk load/save failures |
| Player actions | Joins, disconnects, illegal movement, unknown commands |
| Authentication | Mojang auth failures, session server down |
| Memory | OutOfMemoryError, GC overhead, disk full |
| Java exceptions | NullPointerException, StackOverflow, ClassCastException, and 15+ more |
| Netty/network layer | DecoderException, CorruptedFrameException, NativeIoException |
| YAML/config | Parse errors with line numbers |
| Startup/shutdown | Version info, world loading, save messages |

---

## Supported Platforms

| Server | Status | Notes |
| --- | --- | --- |
| **Paper 1.18+** | ✅ Fully supported | Recommended |
| **Purpur** | ✅ Fully supported | Paper fork |
| **Spigot 1.18+** | ✅ Fully supported | |
| **Bukkit 1.18+** | ✅ Fully supported | |
| **Folia** | ✅ Supported | Paper fork |
| **Pufferfish / Leaves / other Paper forks** | ✅ Supported | |
| **Pre-1.18 servers** | ❌ Not supported | Log4j2 integration missing |
| **Velocity / BungeeCord / Waterfall** | ❌ Not supported | Proxy servers |
| **Fabric / Forge** | ❌ Not supported | Mod loaders (would need a separate mod) |

---

## Installation

1. Download the latest **`MCMS.jar`** from the [Releases](https://github.com/chank-op/MCMS-Make-Console-Make-Sense/releases/latest) page
2. Drop it into your server's `plugins/` folder
3. Restart the server
4. Done — no configuration required

---

## Commands & Permissions

| Command | Description | Permission |
| --- | --- | --- |
| `/mcms` | Show stats | `mcms.use` |
| `/mcms stats` | Messages transformed, exceptions summarized | `mcms.use` |
| `/mcms update` | Manually trigger an update check | `mcms.admin` |
| `/mcms reload` | Reload config.yml | `mcms.admin` |
| `/mcms help` | Show help | `mcms.use` |

All commands default to **OP only**.

---

## Update Checker

On every startup MCMS checks GitHub for a newer version. If one exists, it prints a hard-to-miss notice in the console with the download link. That's it — no automatic downloading, no surprises.

You can also trigger a manual check with `/mcms update`.

---

## Configuration

`plugins/MCMS/config.yml`:

```yaml
# Check GitHub for updates on every server startup
check-for-updates: true

# Your GitHub repo in "username/repo" format
github-repo: "chank-op/MCMS-Make-Console-Make-Sense"
```

---

## Building from Source

**Requirements:** Java 17+, Maven

```bash
git clone https://github.com/chank-op/MCMS-Make-Console-Make-Sense.git
cd MCMS-Make-Console-Make-Sense
mvn package
# Output: target/MCMS.jar
```

---

## Releasing a New Version

Push a version tag — the GitHub Actions workflow does everything else automatically:

```bash
git add .
git commit -m "chore: prepare release 1.2.0"
git tag v1.2.0
git push && git push --tags
```

The workflow will:

1. Build the plugin with the correct version number baked in
2. Create a GitHub Release named `MCMS v1.2.0`
3. Upload `MCMS.jar` as the release asset

> You can also trigger a release manually via **Actions → Build & Release → Run workflow** and entering a version number.

---

## License

MIT — do whatever you want with it.
