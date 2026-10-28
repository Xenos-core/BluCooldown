# BluCooldown

BluCooldown is a lightweight Minecraft plugin for configurable item cooldowns on Paper and Folia servers.

## Features

- Native Minecraft cooldown animation
- Configurable cooldowns for any item using its Material name
- Per-player cooldown bypass and VIP reduction tiers
- Reload command and `/blucooldown handitem <seconds>` command
- Folia compatible
- Paper 26.3+ support
- Java 25 support

## Permissions

| Permission | Purpose | Default |
| --- | --- | --- |
| `blucooldown.bypass` | Bypass all BluCooldown-managed cooldowns | Operators |
| `blucooldown.tier.vip` | Apply the VIP cooldown reduction | Disabled |
| `blucooldown.reload` | Reload the plugin configuration | Operators |
| `blucooldown.handitem` | Set a cooldown for the item in hand | Operators |

VIP cooldowns use `cooldown-tiers.vip-multiplier` in `config.yml`. The default `0.5` halves configured durations, rounded up to whole seconds. Set a value greater than `0` and at most `1`.
