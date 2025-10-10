# ReMind App - Troubleshooting Guide

## ✅ RESOLVED: App Crash on Startup

### Root Cause

The app was crashing with `IllegalStateException: Activity does not have a NavController set` because we were trying to access the NavController before it was fully initialized by the FragmentContainerView.

### The Problem

```kotlin
// ❌ This fails because FragmentContainerView hasn't initialized NavController yet
val navController = findNavController(R.id.nav_host_fragment_content_main)
```

### The Solution

```kotlin
// ✅ Get NavController from the NavHostFragment directly
val navHostFragment = supportFragmentManager
    .findFragmentById(R.id.nav_host_fragment_content_main) as androidx.navigation.fragment.NavHostFragment
val navController = navHostFragment.navController
```

### Changes Made in `MainActivity.kt`:

1. Changed `findNavController()` to get the NavHostFragment first, then access its NavController
2. Updated both `onCreate()` and `onSupportNavigateUp()` methods
3. Added detailed logging with try-catch to help diagnose issues
4. Added Toast messages for user feedback

---

## Other Fixes Applied

### 1. Java Version Issue

**Problem:** Android Gradle Plugin 8.12 requires Java 17+, but system had Java 11  
**Solution:** Updated `gradle.properties` to use Android Studio's bundled JDK (Java 21)

```properties
# Add this line to gradle.properties
org.gradle.java.home=/Applications/Android Studio.app/Contents/jbr/Contents/Home
```

### 2. Enhanced Error Logging

- Added step-by-step logging to MainActivity
- Added logging to DailyHabitsFragment and MoodJournalFragment
- Added error catching with descriptive messages

---

## Useful Commands

### Build and Install App

```bash
cd /Users/sachinthaheshan/AndroidStudioProjects/ReMind
./gradlew clean installDebug
```

### Check Logcat for Errors

```bash
# Clear logs and start app
adb logcat -c
adb shell am start -n com.example.remind/.MainActivity
sleep 3
adb logcat -d | grep -E "MainActivity|AndroidRuntime|FATAL"
```

### Take Screenshot

```bash
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png ~/Desktop/app_screenshot.png
```

### Check Connected Devices

```bash
adb devices
```

### Check Java Version

```bash
java -version
/usr/libexec/java_home -V
```

---

## Common Android Development Issues

### Issue 1: Navigation Component Crash

**Symptoms:** `IllegalStateException: Activity does not have a NavController set`  
**Solution:** Use `supportFragmentManager.findFragmentById()` to get NavHostFragment first, then get its NavController

### Issue 2: Java Version Mismatch

**Symptoms:** `Android Gradle plugin requires Java 17 to run. You are currently using Java 11`  
**Solution:** Set `org.gradle.java.home` in `gradle.properties` to point to Java 17+ or Android Studio's JDK

### Issue 3: Fragment Not Found

**Symptoms:** `ClassNotFoundException` for fragments  
**Solution:**

- Ensure all Fragment classes exist in the correct package
- Check `nav_graph.xml` for correct `android:name` attributes
- Rebuild project

### Issue 4: Layout Inflation Error

**Symptoms:** `InflateException` in logcat  
**Solution:**

- Check XML layouts for syntax errors
- Verify all referenced resources exist (colors, strings, drawables)
- Check for missing closing tags

### Issue 5: ViewBinding Error

**Symptoms:** Unresolved reference to binding classes  
**Solution:**

- Ensure `viewBinding { enable = true }` in `build.gradle.kts`
- Clean and rebuild project
- Invalidate caches and restart Android Studio

### Issue 6: GridLayout Compatibility

**Symptoms:** Crash on certain Android versions when using GridLayout  
**Solution:** Use `LinearLayout` with nested layouts and `layout_weight` instead of `GridLayout`

### Issue 7: Resource Not Found

**Symptoms:** `ResourceNotFoundException` at runtime  
**Solution:**

- Run `./gradlew clean`
- Delete `app/build` folder
- Rebuild project
- Check for typos in resource IDs

---

## In Android Studio

### Quick Fixes to Try:

1. **Clean Build**

   ```
   Build → Clean Project
   Build → Rebuild Project
   ```

2. **Invalidate Caches**

   ```
   File → Invalidate Caches / Restart
   ```

3. **Sync Gradle**

   ```
   File → Sync Project with Gradle Files
   ```

4. **Check Logcat**
   - Click "Logcat" tab at bottom
   - Look for red error lines
   - Find "FATAL EXCEPTION"
   - Copy entire stack trace

---

## App Status: ✅ WORKING

The ReMind app is now fully functional with:

- ✅ Daily Habits page as the main screen
- ✅ Dynamic greeting and date display
- ✅ Habit progress tracking
- ✅ Add new habits functionality
- ✅ Habit details page
- ✅ Mood Journal with emoji selector
- ✅ Mood history
- ✅ Bottom navigation (Habits/Mood)
- ✅ Data persistence with SharedPreferences
- ✅ Beautiful blue Material Design 3 theme
- ✅ Dark mode support

### Test Results:

- Build: ✅ SUCCESS
- Installation: ✅ SUCCESS
- Launch: ✅ SUCCESS
- Navigation: ✅ SUCCESS
- UI Display: ✅ SUCCESS

---

## Getting Help

If you encounter new issues:

1. Check Logcat for error messages
2. Look for `FATAL EXCEPTION` or `AndroidRuntime` errors
3. Share the complete stack trace
4. Note when the crash occurs (startup, navigation, button click, etc.)
5. Try the Quick Fixes listed above
