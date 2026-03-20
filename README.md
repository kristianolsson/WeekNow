# WeekNow

A minimal Android app that permanently shows the current **ISO week number** as an icon in the status bar.

## Features

- 📅 **Persistent status bar icon** — displays the current week number at all times using a foreground notification service
- 🌗 **Dark & light theme** — follows the system theme automatically
- 🔁 **Auto-restarts on reboot** — a `BroadcastReceiver` listens for `ACTION_BOOT_COMPLETED`
- ⚙️ **Configurable settings:**
  - Enable / disable the status bar icon
  - Show border — wraps the number inside a smooth rounded rectangle
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



