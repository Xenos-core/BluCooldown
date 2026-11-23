<div align="center">
  <img src=".github/assets/banner.svg" alt="BluCooldown" width="100%">

  <h3>Lightweight, configurable item cooldowns for Paper and Folia.</h3>

  <a href="https://github.com/Xenos-core/BluCooldown/actions"><img src="https://img.shields.io/github/actions/workflow/status/Xenos-core/BluCooldown/maven.yml?style=for-the-badge&logo=github&color=4DA8FF"></a>
  <a href="https://github.com/Xenos-core/BluCooldown/releases"><img src="https://img.shields.io/github/v/release/Xenos-core/BluCooldown?style=for-the-badge&logo=github&color=2563EB"></a>
  <a href="https://github.com/Xenos-core/BluCooldown/releases"><img src="https://img.shields.io/github/downloads/Xenos-core/BluCooldown/total?style=for-the-badge&logo=github&color=8CC8FF"></a>
  <br>
  <img src="https://img.shields.io/badge/Paper-26.3%2B-4DA8FF?style=for-the-badge">
  <img src="https://img.shields.io/badge/Folia-supported-2563EB?style=for-the-badge">
  <img src="https://img.shields.io/badge/java-25-BFE3FF?style=for-the-badge&logo=openjdk&logoColor=black">
</div>

<br>

BluCooldown puts cooldowns on any item you want, like ender pearls, golden apples, chorus fruit or tridents,
and shows them with Minecraft's own cooldown sweep on the hotbar. No fake timers and no boss bars, just
the animation players already know, plus an action bar message if they try too early.

<div align="center">
  <img src=".github/assets/preview.svg" alt="Cooldown preview" width="100%">
</div>

<h3>Features</h3>

- **Vanilla animation.** Uses the native item cooldown, so the hotbar sweep just works
- **Any item.** Add it by its Material name, or hold it and run `/blucooldown handitem <seconds>`
- **Riptide tridents.** `TRIDENT_RIPTIDE` gets its own cooldown, separate from regular throws
- **VIP tiers.** Give a group shorter cooldowns with one permission and a multiplier
- **Bypass.** Staff and admins can skip cooldowns entirely
- **Folia ready.** Runs on Folia's region threads with no extra setup
- **Update check.** Ops get a heads-up when a new release is out

<h3>Installation</h3>

1. Download the jar from [Releases](https://github.com/Xenos-core/BluCooldown/releases)
2. Drop it into `plugins/`
3. Restart, then edit `plugins/BluCooldown/config.yml` and run `/blucooldown reload`

Needs Paper or Folia 26.3+ on Java 25.

<h3>Commands</h3>

| Command | What it does | Permission |
|---|---|---|
| `/blucooldown reload` | Reload the config | `blucooldown.reload` |
| `/blucooldown handitem <seconds>` | Set a cooldown for the item in your hand | `blucooldown.handitem` |

Alias: `/blu`

<h3>Permissions</h3>

| Permission | Purpose | Default |
|---|---|---|
| `blucooldown.bypass` | Bypass all BluCooldown cooldowns | op |
| `blucooldown.tier.vip` | Apply the VIP cooldown reduction | nobody |
| `blucooldown.reload` | Reload the config | op |
| `blucooldown.handitem` | Set a cooldown for the held item | op |

<h3>Configuration</h3>

```yaml
cooldown-tiers:
  # VIP cooldown = normal cooldown × this, rounded up to whole seconds.
  # Anything above 0 and up to 1.
  vip-multiplier: 0.5

items:
  ENDER_PEARL:
    enabled: true
    cooldown: 15
  GOLDEN_APPLE:
    enabled: true
    cooldown: 5
  TRIDENT_RIPTIDE:
    enabled: true
    cooldown: 10
```

Messages use [MiniMessage](https://docs.advntr.dev/minimessage/format.html), and `%time%` is the time left:

```yaml
messages:
  cooldown:
    actionbar: "<red>You must wait <white>%time%</white> before using this item again."
```

<h3>Building</h3>

```bash
git clone https://github.com/Xenos-core/BluCooldown.git
cd BluCooldown
mvn package
```

The jar lands in `target/`. Every push to `main` also gets built by
[GitHub Actions](https://github.com/Xenos-core/BluCooldown/actions).
