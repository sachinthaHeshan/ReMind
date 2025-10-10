# Mood Journal Feature

## ✅ Implementation Complete

### Overview

The Mood Journal feature allows users to track their emotional states throughout the day with emoji-based mood logging and optional notes.

---

## 🎯 Features Implemented

### 1. **Emoji-Based Mood Selector**

10 different moods with corresponding emojis:

- 😊 Happy
- 🤩 Excited
- 😐 Neutral
- 😢 Sad
- 😠 Angry
- 😴 Tired
- 😰 Anxious
- 😌 Relaxed
- 🥰 Loved
- 🤒 Sick

### 2. **Mood Entry Features**

- ✅ Visual emoji grid selector
- ✅ Selected mood highlighting
- ✅ Optional text notes
- ✅ Automatic timestamp
- ✅ Save to SharedPreferences

### 3. **Mood History**

- ✅ Chronological list of all moods
- ✅ Display emoji, mood name, and note
- ✅ Smart time formatting (Today, Yesterday, etc.)
- ✅ Delete individual entries
- ✅ Empty state when no entries

### 4. **Navigation**

- ✅ Bottom Navigation Bar
- ✅ Two main tabs: Habits and Mood
- ✅ Smooth navigation between sections
- ✅ Persistent bottom bar

---

## 📱 UI Components

### Main Screen Layout

```
┌─────────────────────────────┐
│   Mood Journal (Toolbar)    │
├─────────────────────────────┤
│ How are you feeling?         │
│ Select your current mood     │
│                              │
│ ┌──┬──┬──┬──┬──┐            │
│ │😊│🤩│😐│😢│😠│            │
│ └──┴──┴──┴──┴──┘            │
│ ┌──┬──┬──┬──┬──┐            │
│ │😴│😰│😌│🥰│🤒│            │
│ └──┴──┴──┴──┴──┘            │
│                              │
│ [ Add note (optional) ]      │
│                              │
│ [ Save Mood ]                │
├─────────────────────────────┤
│ Mood History                 │
│ ┌──────────────────────────┐│
│ │ 😊 Happy                 ││
│ │ 10:30 AM • Today         ││
│ └──────────────────────────┘│
├─────────────────────────────┤
│  [Habits]  [Mood]           │
└─────────────────────────────┘
```

---

## 🗂️ File Structure

### New Files Created:

#### Layouts

- `fragment_mood_journal.xml` - Main mood journal UI
- `item_mood.xml` - Individual mood entry item
- `bottom_nav_menu.xml` - Navigation menu

#### Kotlin Classes

- `Mood.kt` - Data model for mood entries
- `MoodRepository.kt` - SharedPreferences storage management
- `MoodAdapter.kt` - RecyclerView adapter
- `MoodJournalFragment.kt` - Main fragment logic

#### Resources

- `ic_mood.xml` - Mood tab icon
- `ic_habits.xml` - Habits tab icon

### Updated Files:

- `activity_main.xml` - Added bottom navigation
- `MainActivity.kt` - Setup bottom navigation controller
- `nav_graph.xml` - Added MoodJournalFragment
- `strings.xml` - Added mood-related strings

---

## 💾 Data Storage

### Mood Data Model

```kotlin
data class Mood(
    id: String,           // Unique UUID
    emoji: String,        // Emoji character
    moodName: String,     // Mood name (Happy, Sad, etc.)
    note: String,         // Optional note
    timestamp: Long       // Unix timestamp
)
```

### Storage Method

- **SharedPreferences** with Gson serialization
- Stored as JSON array
- Key: `"moods"`
- Sorted by most recent first

---

## 🔄 User Flow

### Adding a Mood Entry:

1. User opens Mood Journal tab
2. Selects an emoji from the grid
3. (Optional) Adds a text note
4. Clicks "Save Mood"
5. Entry is saved and appears in history

### Viewing History:

1. Scroll down to "Mood History" section
2. See all past mood entries
3. Each entry shows:
   - Emoji
   - Mood name
   - Optional note
   - Relative time (Today, Yesterday, etc.)

### Deleting an Entry:

1. Click delete button on any mood entry
2. Confirm deletion in dialog
3. Entry is removed from history

---

## 🎨 Visual Design

### Colors

- **Card Background**: White/Dark surface
- **Selected Emoji**: Primary blue border (4dp)
- **Unselected Emoji**: Light gray border (2dp)
- **Emoji Size**: 32sp in grid, 28sp in list

### Spacing

- Grid spacing: 4dp margins
- Card padding: 16-24dp
- Corner radius: 12-16dp

### Typography

- Title: TitleLarge (24sp, bold)
- Mood name: BodyLarge (16sp, bold)
- Note: BodyMedium (14sp)
- Time: BodySmall (12sp, tertiary color)

---

## ⚙️ Technical Details

### Time Formatting

Smart relative time display:

- **Today**: "10:30 AM • Today"
- **Yesterday**: "10:30 AM • Yesterday"
- **This Week**: "Monday, 10:30 AM"
- **Older**: "Jan 15, 10:30 AM"

### Emoji Grid

- GridLayout with 5 columns, 2 rows
- Responsive card sizing
- Touch feedback with ripple effect
- Visual selection state

### State Management

- Fragment-level state for selected emoji
- Repository pattern for data access
- RecyclerView with DiffUtil for efficient updates

---

## 🔐 Data Persistence

### MoodRepository Methods:

```kotlin
saveMood(mood: Mood): Boolean
getAllMoods(): List<Mood>
getTodayMoods(): List<Mood>
deleteMood(moodId: String): Boolean
getMoodStatistics(): MoodStatistics
clearAllMoods()
```

### Statistics Available:

- Total entries count
- Entries today count
- Most common mood

---

## 📊 Navigation Structure

### Bottom Navigation

```
┌─────────────────────────────┐
│     Main Navigation          │
├─────────────────────────────┤
│                              │
│    [Screen Content]          │
│                              │
├─────────────────────────────┤
│  Habits │ Mood               │
│    ✓    │                    │
└─────────────────────────────┘
```

### Top-Level Destinations:

1. **Daily Habits** (Home)
   - Habit list
   - Add/Edit habits
   - Habit details
2. **Mood Journal** (New)
   - Mood selector
   - Mood history

---

## 🚀 Future Enhancements

### Potential Features:

- [ ] Mood analytics/trends
- [ ] Calendar view of moods
- [ ] Mood patterns and insights
- [ ] Export mood data
- [ ] Mood reminders
- [ ] Mood graphs/charts
- [ ] Filter moods by date range
- [ ] Search mood entries
- [ ] Tags/categories for notes
- [ ] Photo attachments
- [ ] Mood sharing
- [ ] Custom emoji/mood types

---

## 🎯 Use Cases

### Personal Wellness

- Track daily emotional state
- Identify mood patterns
- Monitor mental health trends
- Journal thoughts and feelings

### Therapy Support

- Share mood log with therapist
- Track mood over time
- Identify triggers
- Monitor treatment effectiveness

### Mindfulness

- Increase emotional awareness
- Practice gratitude
- Reflect on daily experiences
- Build emotional intelligence

---

## 📝 Best Practices

### For Users:

1. Log mood multiple times per day
2. Add notes for context
3. Be honest about feelings
4. Review patterns weekly
5. Use alongside habit tracking

### For Developers:

1. Keep mood selection simple
2. Fast and easy data entry
3. Respect user privacy
4. Secure data storage
5. Intuitive UI/UX

---

## 🔄 Integration with Habits

While currently separate, mood data can be:

- Correlated with habit completion
- Used to identify habit impact on mood
- Combined for wellness insights
- Analyzed for patterns

Future versions may include:

- Habit-mood correlation analysis
- Combined reports
- Integrated wellness score

---

## ✨ Key Features Summary

| Feature           | Status | Description                      |
| ----------------- | ------ | -------------------------------- |
| Emoji Selector    | ✅     | 10 mood options with visual grid |
| Optional Notes    | ✅     | Add context to mood entries      |
| History View      | ✅     | Chronological list of moods      |
| Delete Entries    | ✅     | Remove individual moods          |
| Bottom Nav        | ✅     | Navigate between sections        |
| SharedPreferences | ✅     | Persistent data storage          |
| Smart Time Format | ✅     | Relative time display            |
| Empty State       | ✅     | Guide when no data               |
| Material Design   | ✅     | Modern, polished UI              |
| Dark Mode         | ✅     | Full theme support               |

---

**Built with ❤️ for mental wellness and emotional awareness**
