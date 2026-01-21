# Dynamic AC+

## Overview
Dynamic AC+ is an advanced, optimized anti-cheat plugin designed specifically for low to mid-sized Minecraft servers. It offers comprehensive cheat detection, including hacks, X-ray, alt accounts, and combat logging. The plugin efficiently monitors both player behavior and server performance to ensure a fair and enjoyable gameplay experience.

## Features

### Customizable Anti-Cheat Detection
Detects a wide range of cheats such as X-ray, speed hacks, fly hacks, and combat cheats using multiple detection methods like movement anomalies and inventory scans.

### Evidence Recording
All cheating incidents are logged into server files with details including player UUID, coordinates, timestamp, and reason. This provides transparency and helps moderators review flagged events.

### Alt-Account Detection
Uses IP-based detection to identify potential alternate accounts, considering the first account as the original, and stores details securely for moderation review.

### Combat-Log Prevention
Monitors players during combat, displaying a bossbar countdown (default 10 seconds) to prevent combat logging. When detected, the incident is logged with player location, time, and reason, and suitable punishments such as kills or temporary bans can be automatically applied.

### Configurable Moderation Actions
Server administrators can define responses to cheating behaviors, including warning, kicking, temporary bans, or permanent bans, with an escalating punishment system based on the number of offenses.

### Message Customization
Allows full control over messages sent to players, flagged offenders, and moderators, ensuring clear communication and community awareness.

## Requirements
- ProtocolLib

### Lightweight and Modular
Designed to keep the server overhead low, with features and notifications that can be enabled or disabled via easy-to-edit configurations.

## Conclusion
Dynamic AC+ provides powerful, flexible tools for server owners and moderators to uphold fairness and integrity, all while maintaining a straightforward setup and minimal performance impact.
