# ReMind - Daily Habit Tracker

## ✅ Implemented Features

### 1. **Habit Data Persistence**

- ✅ Habits saved in SharedPreferences using Gson
- ✅ Automatic JSON serialization/deserialization
- ✅ Data persists across app sessions

### 2. **Main Screens**

- ✅ **Daily Habits Page** - View all your habits
- ✅ **Add Habit Page** - Create new habits
- ✅ **Habit Details Page** - View statistics (UI ready)

### 3. **Daily Habits Features**

- ✅ Dynamic greeting (Good Morning/Afternoon/Evening/Night)
- ✅ Current date display
- ✅ Habit list with RecyclerView
- ✅ Empty state when no habits
- ✅ Progress tracking (X of Y habits completed)
- ✅ Progress bar visualization
- ✅ Statistics cards (Streak, Weekly, Total)
- ✅ Checkbox to mark habits complete
- ✅ Real-time UI updates

### 4. **Add Habit Features**

- ✅ Habit name input with validation
- ✅ Category selection (Health, Mindfulness, Learning, Hydration, Sleep)
- ✅ Visual feedback for selected category
- ✅ Time picker for reminders
- ✅ Frequency selection (Daily, Weekly, Custom)
- ✅ Form validation
- ✅ Save to SharedPreferences

### 5. **Habit Management**

- ✅ Create habits
- ✅ View habits list
- ✅ Mark habits as complete/incomplete
- ✅ Automatic streak calculation
- ✅ Track completion dates
- ✅ Best streak tracking

### 6. **Data Model**

```kotlin
Habit:
- id (UUID)
- name
- category
- time
- frequency
- isCompleted
- completedDates (List)
- currentStreak
- bestStreak
- createdAt
```

### 7. **UI Design**

- ✅ Material Design 3 components
- ✅ Dark mode support
- ✅ Beautiful color-coded categories
- ✅ Smooth animations
- ✅ Responsive layouts
- ✅ Custom icons for each category

## 🎨 Color Scheme

### Categories:

- **Health** - Green (#10B981)
- **Mindfulness** - Purple (#8B5CF6)
- **Learning** - Orange (#F59E0B)
- **Hydration** - Blue (#3B82F6)
- **Sleep** - Indigo (#6366F1)

### Streak:

- **Streak Color** - Orange (#F97316)

## 📱 Usage

### Creating a Habit:

1. Open app → Daily Habits screen
2. Click "Add Habit" FAB button
3. Enter habit name
4. Select category
5. Choose reminder time
6. Select frequency
7. Click "Save Habit"

### Marking Habit Complete:

1. On Daily Habits screen
2. Tap checkbox next to habit
3. Progress updates automatically
4. Streak increments if consecutive days

### Viewing Progress:

- Top of screen shows completion percentage
- Progress bar visualizes completion
- Statistics cards show:
  - Current streak (average)
  - Total completions this week
  - Total number of habits

## 📦 Dependencies

```kotlin
// Gson for JSON serialization
implementation("com.google.code.gson:gson:2.10.1")
```

## 🗂️ File Structure

```
app/src/main/
├── java/com/example/remind/
│   ├── MainActivity.kt
│   ├── DailyHabitsFragment.kt
│   ├── AddHabitFragment.kt
│   ├── HabitDetailsFragment.kt
│   ├── Habit.kt (Data Model)
│   ├── HabitRepository.kt (Storage)
│   └── HabitAdapter.kt (RecyclerView)
│
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   ├── fragment_daily_habits.xml
    │   ├── fragment_add_habit.xml
    │   ├── fragment_habit_details.xml
    │   ├── item_habit.xml
    │   └── item_day_status.xml
    │
    ├── drawable/
    │   ├── ic_* (22 icons)
    │   └── bg_* (backgrounds)
    │
    ├── values/
    │   ├── strings.xml
    │   ├── colors.xml
    │   └── themes.xml
    │
    └── navigation/
        └── nav_graph.xml
```

## 🔄 Data Flow

```
User Action → Fragment → Repository → SharedPreferences
                ↓
            Update UI ← Load Data ← Repository
```

## 🚀 Future Enhancements

### TODO:

- [ ] Edit existing habits
- [ ] Delete habits with confirmation
- [ ] Habit Details page implementation
- [ ] Weekly/Monthly calendar view
- [ ] Habit history and trends
- [ ] Notifications/Reminders
- [ ] Export/Import data
- [ ] Custom habit icons
- [ ] Habit notes/description
- [ ] Share achievements

## 🎯 Key Features of Storage

### HabitRepository Methods:

- `saveHabit(habit)` - Save new habit
- `getAllHabits()` - Get all habits
- `getTodayHabits()` - Get habits with today's status
- `updateHabitCompletion(id, isCompleted)` - Mark complete/incomplete
- `updateHabit(habit)` - Update habit details
- `deleteHabit(id)` - Delete habit
- `getHabitById(id)` - Get specific habit
- `getStatistics()` - Get aggregated stats
- `clearAllHabits()` - Clear all (for testing)

### Streak Calculation:

- Automatically calculates consecutive completion days
- Updates current streak on each completion
- Tracks best streak ever achieved
- Resets if habit not completed for more than 1 day

## 📊 Statistics Tracking

The app tracks:

- **Total Habits** - Number of active habits
- **Completed Today** - Habits completed today
- **Total Completed** - All-time completions
- **Average Streak** - Average streak across all habits
- **Current Streak** - Per habit streak counter
- **Best Streak** - Highest streak achieved per habit

## 🎨 UI Components

### Custom Components:

- Collapsing Toolbar with header
- Progress Indicator
- Category cards with selection
- Habit item cards with checkboxes
- Streak badges with fire icon
- Empty state design
- Time picker dialog
- Frequency chips

## ✨ Special Features

1. **Auto-refresh** - Habits reload on screen resume
2. **Smart Streak** - Automatically calculates consecutive days
3. **Today's Status** - Resets completion for new day
4. **Visual Feedback** - Category selection highlights
5. **Form Validation** - Prevents empty habit creation
6. **Date Tracking** - Stores completion dates for history
7. **Dynamic Greeting** - Changes based on time of day
8. **Real-time Stats** - Statistics update instantly

---

**Built with ❤️ using Kotlin, Material Design 3, and SharedPreferences**
