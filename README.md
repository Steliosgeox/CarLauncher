# CarLauncher 🚗

Android car launcher for tablet head units. Built with Kotlin + Jetpack Compose.

## Current State: Sprint 1 — Foundation

### What IS real

| Feature | Status |
|---|---|
| Adaptive Compose UI (1024×600 → 1920×1080) | ✅ Working |
| Canvas-rendered RPM gauge with gradient arc | ✅ Working |
| Digital speed display with animation | ✅ Working |
| Gear indicator | ✅ Working |
| Dark automotive theme (Night/Day/Sport) | ✅ Working |
| Telemetry abstraction (TelemetryProvider interface) | ✅ Working |
| Simulated drive cycle for UI testing | ✅ Working |
| Launcher HOME intent filter | ✅ Working |
| Landscape-only, immersive fullscreen | ✅ Working |
| Vehicle profile data structure (KIA Pro_Cee'd) | ✅ Defined |

### What is NOT implemented yet

| Feature | Status | Sprint |
|---|---|---|
| OBD-II (ELM327 Bluetooth) | ❌ Placeholder interface only | Sprint 2 |
| Torque Pro integration | ❌ Placeholder interface only | Sprint 2 |
| Media/Spotify/Now Playing | ❌ Placeholder interface only | Sprint 2 |
| Map rendering (MapLibre) | ❌ Visual placeholder only | Sprint 3 |
| 3D car model | ❌ Not started | Sprint 4+ |
| Real GPS | ❌ Not started | Sprint 3 |

### Honesty policy

- The "SIMULATED" badge is visible whenever simulated data is active.
- No UI element claims "OBD Connected" or implies live vehicle data unless a real provider is active.
- Placeholder cards explicitly say "Coming in Sprint X".
- The simulated provider generates a realistic drive cycle (idle → accel → cruise → decel) for UI testing only.

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Build**: Gradle Kotlin DSL
- **Min SDK**: 31 (Android 12)
- **Target SDK**: 36
- **Package**: `com.steliosgeox.carlauncher`

## Project Structure

```
app/src/main/java/com/steliosgeox/carlauncher/
├── MainActivity.kt
├── telemetry/
│   ├── core/
│   │   ├── TelemetryProvider.kt      # Interface
│   │   ├── TelemetrySnapshot.kt      # Data class (all vehicle data)
│   │   └── TelemetrySource.kt        # Enum (SIMULATED, OBD, TORQUE)
│   ├── simulated/
│   │   └── SimulatedTelemetryProvider.kt  # Drive cycle simulator
│   ├── obd/
│   │   └── ObdTelemetryProvider.kt   # TODO placeholder
│   └── torque/
│       └── TorqueTelemetryProvider.kt # TODO placeholder
├── vehicle/
│   └── profile/
│       ├── VehicleProfile.kt
│       ├── FuelType.kt
│       └── KiaProCeedProfile.kt
├── diagnostics/
│   ├── DiagnosticLog.kt
│   └── DiagnosticLevel.kt
├── media/
│   └── MediaProvider.kt              # TODO placeholder
├── navigation/
│   └── MapProvider.kt                # TODO placeholder
└── ui/
    ├── theme/
    │   ├── Color.kt
    │   ├── Theme.kt
    │   ├── ThemeMode.kt
    │   └── Type.kt
    ├── components/
    │   ├── RpmGauge.kt               # Canvas gauge
    │   ├── SpeedDisplay.kt
    │   ├── GearIndicator.kt
    │   ├── StatusCard.kt
    │   ├── StatusRow.kt
    │   ├── ControlButton.kt
    │   └── SimulationBadge.kt
    └── cockpit/
        ├── CockpitScreen.kt          # Main dashboard layout
        └── CockpitViewModel.kt
```

## Target Vehicle

**KIA Pro_Cee'd ED Facelift 2010 — 1.4 G4FA Gamma B — Bosch ECU — Petrol/LPG**

## Building

```bash
./gradlew assembleDebug
```

## Running

Open in Android Studio. Run on a landscape Android tablet or emulator (API 31+).
The app will display with the simulated telemetry provider — a looping drive cycle animating all gauges.

## License

Private project.
