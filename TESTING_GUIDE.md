# 🧪 Testing Guide - Hydration Reminders (10 Second Interval)

## ⚡ Quick Testing Setup

The hydration reminders are now set to **10 seconds** instead of 2 hours for easy testing!

## How to Test

### Step 1: Enable Reminders

1. Open the app
2. Tap the **"Water"** tab at the bottom
3. Scroll down to **Settings** section
4. Toggle **"Reminder Notifications"** to **ON**

### Step 2: Immediate Confirmation

You should see:

- ✅ A notification appears immediately: **"Reminder notification Started"**
- ✅ Message: "You will receive reminders every 10 seconds (testing)"
- ✅ A Snackbar at the bottom: "Reminders enabled"

### Step 3: Wait for Recurring Notifications

- ⏱️ **Wait 10 seconds**
- 🔔 You'll receive a notification: **"Time to drink water!"**
- ⏱️ **Wait another 10 seconds**
- 🔔 Another notification appears
- 🔁 Notifications will repeat every 10 seconds until you disable them

### Step 4: Disable Reminders

1. Go back to the Water tab
2. Toggle **"Reminder Notifications"** to **OFF**
3. No more notifications will appear

## What's Changed

### Before (Production):

- Interval: **2 hours**
- Frequency: 7 times per day (8 AM - 10 PM)
- Hard to test without waiting

### Now (Testing):

- Interval: **10 seconds**
- Frequency: Continuous (every 10 seconds)
- Easy to test immediately

## Code Changes

### `WaterNotificationHelper.kt`

```kotlin
// Changed from:
private const val REMINDER_INTERVAL = 2 * 60 * 60 * 1000L // 2 hours

// To:
private const val REMINDER_INTERVAL = 10 * 1000L // 10 seconds for testing
```

### Simplified Scheduling

- **Before:** Complex scheduling with specific hours (8 AM, 10 AM, 12 PM, etc.)
- **Now:** Simple repeating alarm every 10 seconds

## Testing Checklist

- [ ] Enable reminders toggle
- [ ] See immediate confirmation notification
- [ ] Wait 10 seconds and see first recurring notification
- [ ] Wait 10 more seconds and see second notification
- [ ] Tap notification to open app (should go to Water tab)
- [ ] Disable reminders toggle
- [ ] Confirm notifications stop

## Expected Behavior

### ✅ When Enabled:

1. **Immediate:** "Reminder notification Started" notification
2. **After 10 sec:** "Time to drink water!" notification
3. **After 20 sec:** Another "Time to drink water!" notification
4. **After 30 sec:** Another notification (continues every 10 seconds)

### ✅ When Disabled:

- No more notifications appear
- Existing notification can be dismissed

## Notification Details

### Confirmation Notification

- **Icon:** 🔔 Bell icon
- **Title:** "Reminder notification Started"
- **Message:** "You will receive reminders every 10 seconds (testing)"
- **Action:** Tap to open Hydration Tracker

### Recurring Water Reminder

- **Icon:** 💧 Water drop icon
- **Title:** "Time to drink water!"
- **Message:** "Stay hydrated throughout the day"
- **Action:** Tap to open Hydration Tracker
- **Frequency:** Every 10 seconds

## Reverting to Production

When ready for production, change back to 2 hours:

```kotlin
// In WaterNotificationHelper.kt
private const val REMINDER_INTERVAL = 2 * 60 * 60 * 1000L // 2 hours
```

And update strings:

```xml
<string name="get_reminded_every_2_hours">Get reminded every 2 hours</string>
<string name="you_will_receive_reminders">You will receive reminders every 2 hours</string>
```

And restore complex scheduling logic for specific hours.

## Troubleshooting

### No Notifications Appearing?

1. **Check Notification Permission:**

   - Go to: Settings → Apps → ReMind → Notifications
   - Ensure notifications are enabled

2. **Check Battery Optimization:**

   - Go to: Settings → Battery → Battery optimization
   - Find ReMind and set to "Don't optimize"

3. **Check Do Not Disturb:**

   - Make sure Do Not Disturb is off

4. **Restart App:**
   - Force stop and reopen the app
   - Re-enable reminders

### Notifications Not Stopping?

1. Toggle reminders OFF in the app
2. Or uninstall/reinstall the app
3. Or clear app data in Settings

## Notes

- 📱 Notifications work even when app is in background
- 🔋 10-second interval is ONLY for testing
- ⚠️ Don't forget to change back to 2 hours for production!
- 💾 Reminder preference is saved in SharedPreferences

## Screenshots Expected

1. **Water tab with Settings section**
2. **Confirmation notification** (Reminder notification Started)
3. **Recurring notification** (Time to drink water!)
4. **Notification tray** with multiple notifications

---

## Quick Test Command

Run the app and enable reminders, then watch your notification tray:

```bash
# Launch app
adb shell am start -n com.example.remind/.MainActivity

# Check logcat for alarm scheduling
adb logcat | grep -E "WaterNotificationHelper|AlarmManager"
```

**Happy Testing!** 🧪💧
