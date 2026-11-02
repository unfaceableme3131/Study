# EDUCORE 🎓
### B.Com (Hons.) Semester 1 Academic Planner, Double Revision Tracker, Smart Alarms & AI Copilot

[![Download APK](https://img.shields.io/badge/Download-Android%20APK%20(v2.0.0)-22c55e?style=for-the-badge&logo=android&logoColor=white)](../../releases/latest/download/EduCore.apk)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin%202.2-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-cyan.svg)](https://developer.android.com/jetpack/compose)
[![Gemini](https://img.shields.io/badge/AI-Google%20Gemini%20Flash-violet.svg)](https://deepmind.google/technologies/gemini/)
[![Database](https://img.shields.io/badge/Storage-Room%20Database-orange.svg)](https://developer.android.com/training/data-storage/room)
[![Version](https://img.shields.io/badge/Release-v2.0.0-blueviolet.svg)]()
[![Target](https://img.shields.io/badge/Window-3%20Nov%20--%206%20Dec%202026-red.svg)]()

> **Master the Core. Excel in Exams. Plan Smart.**

**EduCore** is a high-performance Android productivity and academic copilot application custom-tailored for B.Com (Hons.) Semester 1 students preparing for university examinations between **3 November 2026 and 6 December 2026**.

---

## 📥 Direct APK Download Link (Version 2.0.0)

You can download and install the new Version 2.0 application directly onto your Android device:

- **[👉 Download Latest EduCore.apk (v2.0.0)](../../releases/latest/download/EduCore.apk)**
- **[👉 Alternative Mirror: EduCore-v2.0.apk](../../releases/latest/download/EduCore-v2.0.apk)**
- **Release Version**: `v2.0.0` (Build `2`)
- **Requires**: Android 7.0 (Nougat) or newer (API 24 to 36)

### How to Install on Your Android Phone:
1. Tap the download link above to download `EduCore.apk` to your phone.
2. Tap the downloaded file in your browser or Downloads folder.
3. If Android displays *"For your security, your phone is not allowed to install unknown apps from this source"*, tap **Settings** &rarr; toggle on **Allow from this source**.
4. Tap **Install** &rarr; **Open**.

---

## ⚡ Key Highlights

- **Google Gemini 3.8 Flash AI Copilot**:
  - Instant university-grade answers for Financial Accounting, Business Laws, and Management Principles.
  - Generates exam cheatsheets, formula sheets, case-law briefs, and mock Q&A.
  - Native **PDF Export** (`android.graphics.pdf.PdfDocument`) directly saved to Android Downloads or shared.
- **33-Day Exam Countdown**: Live telemetry countdown to 6 December 2026 with real-time composite readiness index.
- **7 Complete Subject Syllabi**:
  1. **Financial Accounting (FA)**: 5 core units (Theoretical framework, PPE & Inventory AS-2/AS-10, Sole Prop & NPO, Inland Branches & Leases, CAS).
  2. **Business Laws (BL)**: 5 core units (Indian Contract Act 1872, Discharge & Remedies, Special Contracts, Sale of Goods Act 1930, LLP Act 2008).
  3. **Management Principles & Applications (MPA)**: 5 core units (Evolution, Planning & MBO, Organising, Directing & Controlling, Contemporary Trends).
  4. **Generic Elective (GE)**: Fully customizable 5-unit syllabus module.
  5. **Ability Enhancement Course (AEC)**: Customizable units, chapters, and revision notes.
  6. **Skill Enhancement Course (SEC)**: Customizable practical skills tracker.
  7. **Value Addition Course (VAC)**: Customizable ethics and values syllabus.
- **Double Revision Guarantee**:
  - **Phase 1: Syllabus Completion** (3 Nov – 24 Nov)
  - **Phase 2: First Revision** (25 Nov – 30 Nov)
  - **Phase 3: Second Revision** (1 Dec – 5 Dec)
  - **Final Preparation** (6 Dec)
- **Smart Alarm & Routine System**:
  - 9 automated daily routine alarms (4:00 AM Wakeup, 4:05 AM Revision, 5:00 AM Complete, 4:00 PM Study 1, 6:00 PM Complete, 6:30 PM Study 2, 8:30 PM Complete, 9:00 PM Testing, 10:00 PM Day Target Complete).
  - Uses native Android `AlarmManager`, `BroadcastReceiver`, custom notification channels, vibration patterns, and `BootReceiver` for device restart restoration.
- **Productivity Analytics**:
  - Strictly logs verified completed study sessions (no fake or inflated hours).
  - Subject breakdown, streak counter, and daily achievement badges.
- **Offline Room Database**:
  - Fully offline, local persistence with Room Database, automatic default population, and JSON export/backup.

---

## 🏗️ Architecture & Tech Stack

- **Language**: Kotlin 2.2.10
- **UI Toolkit**: Jetpack Compose (Material 3 Dynamic Theme)
- **AI Integration**: Firebase AI Logic (Google Gemini Flash) + Firebase App Check
- **Document Generation**: Android Graphics PDF Engine (`android.graphics.pdf.PdfDocument`)
- **Architecture**: MVVM + Clean Repository Pattern
- **Local Persistence**: Android Jetpack Room 2.7.0 with Kotlin Coroutines & Flow
- **Background Tasks & Alarms**: Android `AlarmManager.setExactAndAllowWhileIdle`, `BroadcastReceiver`, `NotificationCompat`
- **Testing**: Robolectric JVM unit tests covering subjects, units, alarms, and string context

---

## 🚀 Publishing a Release on GitHub

To attach the `.apk` file to a release on your GitHub repository:
1. Push this project to your GitHub account using the **GitHub** button in the top toolbar.
2. Go to your repository on GitHub and click **Releases** (on the right sidebar) &rarr; **Draft a new release**.
3. Set Tag: `v2.0.0`
4. Title: `EduCore v2.0.0`
5. Drag and drop both `EduCore.apk` and `EduCore-v2.0.apk` files into the *"Attach binaries by dropping them here or selecting them"* box.
6. Click **Publish release**. The direct download link at the top of the README will instantly become live for anyone!

---

## 📄 License
Created for academic excellence and examination preparation.
