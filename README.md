# WeekNow

A minimal Android app that permanently shows the current **ISO week number** as an icon in the status bar.

<img src="docs/screenshot.png" width="280" alt="WeekNow screenshot" />

## Features

- 📅 **Persistent status bar icon** — displays the current week number at all times using a foreground notification service
- 🌗 **Dark & light theme** — follows the system theme automatically
- 🔁 **Auto-restarts on reboot** — a `BroadcastReceiver` listens for `ACTION_BOOT_COMPLETED`
- ⚙️ **Configurable settings:**
  - Enable / disable the status bar icon
  - Icon size — S / M / L
  - First day of week — Monday (ISO 8601) or Sunday (US convention)
- 🔔 **Tap notification to open the app**

## How it works

| Component | Role |
|---|---|
| `WeekService` | Foreground service; draws the week number as a white bitmap icon and posts a persistent notification |
| `WeekIconHelper` | Renders the week number onto a `Bitmap` using auto-fit text; converts to a notification `Icon` |
| `BootReceiver` | Restarts `WeekService` after device reboot if the user had it enabled |
| `PrefsHelper` | `SharedPreferences` wrapper for all settings |
| `MainActivity` | Single-screen UI with enable toggle, icon size, and first-day-of-week picker |

## Requirements

- Android 8.0+ (API 26)
- Android Studio (Hedgehog or newer recommended)

## Getting started

1. Clone the repo
   ```bash
   git clone git@github.com:kristianolsson/WeekNow.git
   ```
2. Open the project in Android Studio (`File → Open → weeknow/`)
3. Wait for Gradle sync to complete
4. Run on an emulator or physical device (API 26+)

## Project structure

```
app/src/main/
├── java/com/weeknow/app/
│   ├── MainActivity.kt       # UI + settings
│   ├── WeekService.kt        # Foreground notification service
│   ├── WeekIconHelper.kt     # Bitmap icon generation
│   ├── BootReceiver.kt       # Auto-start after reboot
│   └── PrefsHelper.kt        # SharedPreferences wrapper
└── res/
    ├── layout/activity_main.xml
    └── values/               # strings, colors, themes
```

