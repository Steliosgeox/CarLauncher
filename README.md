# CarLauncher 🚗💨

A modular, premium Android Car Launcher app optimized for landscape tablets (Android 12/14, minSdk 31), customized for a KIA Pro_Cee'd 2010 dashboard integration.

## Features

- **Jetpack Compose UI**: Smooth animations, adaptive landscape layout, and custom Canvas-based RPM/Speed dials.
- **Dynamic Color/Status**: RPM color shifts smoothly from Green ➔ Orange ➔ Red as engine speed rises.
- **Dual Telemetry Architecture**:
  - **Mock Provider**: For local testing and UI prototyping.
  - **Torque Pro AIDL**: Grabs telemetry directly from the Torque Pro Android app.
  - **Direct ELM327 Bluetooth**: Communicates directly with OBD-II adapter via Bluetooth RFCOMM.
- **Vehicle Profile**: Specialized calibration profiles (e.g., KIA Pro_Cee'd 2010).
- **Media Session Integration**: Control and display current music/media details seamlessly.

## Project Structure

```
CarLauncher/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── aidl/                     # Torque Pro AIDL Interface
│   │   │   ├── java/com/example/carlauncher/
│   │   │   │   ├── data/                 # Telemetry & Bluetooth ELM327/Torque providers
│   │   │   │   ├── media/                # Media Session controls
│   │   │   │   ├── theme/                # Custom Theme & Typography
│   │   │   │   ├── ui/                   # Jetpack Compose ViewModels & Screens
│   │   │   │   └── MainActivity.kt
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts
│   ├── build.gradle.kts
│   └── settings.gradle.kts
```

## Setup & Running

1. Clone this repository:
   ```bash
   git clone https://github.com/Steliosgeox/CarLauncher.git
   ```
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Build and run on an Android tablet or emulator running Android 12 (API 31) or above.
