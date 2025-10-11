# ReMind App - Code Structure

This document describes the reorganized code structure of the ReMind Android application.

## Package Organization

The code has been organized following Android best practices with clear separation of concerns:

```
com.example.remind/
├── data/                      # Data Layer
│   ├── model/                 # Data Models / Entities
│   │   ├── Habit.kt          # Habit data model
│   │   ├── Mood.kt           # Mood entry data model
│   │   └── WaterLog.kt       # Water intake log data model
│   │
│   └── repository/            # Data Repositories
│       ├── HabitRepository.kt    # Habit data management
│       ├── MoodRepository.kt     # Mood data management
│       └── WaterRepository.kt    # Hydration data management
│
├── ui/                        # Presentation Layer
│   ├── activity/              # Activities
│   │   └── MainActivity.kt   # Main application activity
│   │
│   ├── fragments/             # Fragments (organized by feature)
│   │   ├── habit/            # Habit-related fragments
│   │   │   ├── AddHabitFragment.kt
│   │   │   ├── DailyHabitsFragment.kt
│   │   │   ├── EditHabitFragment.kt
│   │   │   └── HabitDetailsFragment.kt
│   │   │
│   │   ├── mood/             # Mood tracking fragments
│   │   │   └── MoodJournalFragment.kt
│   │   │
│   │   ├── hydration/        # Hydration tracking fragments
│   │   │   └── HydrationFragment.kt
│   │   │
│   │   ├── FirstFragment.kt  # Demo/legacy fragments
│   │   └── SecondFragment.kt
│   │
│   └── adapter/               # RecyclerView Adapters
│       ├── HabitAdapter.kt
│       ├── MoodAdapter.kt
│       └── WaterLogAdapter.kt
│
├── widget/                    # App Widgets
│   └── HabitWidgetProvider.kt # Home screen widget for habit progress
│
└── utils/                     # Utility Classes
    └── WaterNotificationHelper.kt # Notification management for hydration reminders
```

## Architecture Overview

### Data Layer (`data/`)

#### Models (`data/model/`)

- **Purpose**: Define the structure of data entities
- **Responsibility**: Data representation with serialization support
- Contains: Data classes with GSON annotations for persistence

#### Repositories (`data/repository/`)

- **Purpose**: Handle data operations and business logic
- **Responsibility**: CRUD operations, data transformation, statistics calculation
- Uses: SharedPreferences for local data storage

### UI Layer (`ui/`)

#### Activities (`ui/activity/`)

- **MainActivity**: Main entry point, handles navigation and bottom navigation bar

#### Fragments (`ui/fragments/`)

Organized by feature for better maintainability:

- **habit/**: All habit-related UI screens

  - Daily habit list view
  - Add/Edit habit forms
  - Habit details and statistics

- **mood/**: Mood tracking UI

  - Mood journal entry
  - Mood history

- **hydration/**: Water intake tracking UI
  - Water logging
  - Daily goal management
  - Reminder settings

#### Adapters (`ui/adapter/`)

- **Purpose**: Display lists of data in RecyclerViews
- **Implementation**: Uses DiffUtil for efficient list updates

### Widget Layer (`widget/`)

- **HabitWidgetProvider**: Home screen widget showing habit completion progress
- Updates automatically when habits are modified

### Utils Layer (`utils/`)

- **WaterNotificationHelper**: Manages scheduled notifications for hydration reminders
- **WaterReminderReceiver**: BroadcastReceiver for alarm triggers

## Benefits of This Structure

1. **Separation of Concerns**: Clear boundaries between data, UI, and utility code
2. **Maintainability**: Easy to locate and modify specific features
3. **Scalability**: Simple to add new features without cluttering existing packages
4. **Testability**: Isolated components are easier to unit test
5. **Team Collaboration**: Multiple developers can work on different packages without conflicts
6. **Feature Organization**: Related code is grouped together (e.g., all habit features in one place)

## Configuration Files

### AndroidManifest.xml

Updated to reference new package paths:

- MainActivity: `.ui.activity.MainActivity`
- HabitWidgetProvider: `.widget.HabitWidgetProvider`
- WaterReminderReceiver: `.utils.WaterReminderReceiver`

### Navigation Graph

Updated all fragment references to use new package paths

## Migration Notes

All files have been moved from the root `com.example.remind` package to their appropriate subpackages. The old files have been removed, and all import statements have been updated throughout the codebase.

## Future Improvements

Consider these enhancements for further organization:

1. **ViewModel Layer**: Add ViewModels for better lifecycle management
2. **Domain Layer**: Extract business logic into use cases
3. **Dependency Injection**: Implement Hilt/Dagger for better dependency management
4. **Database**: Migrate from SharedPreferences to Room Database
5. **Remote Data**: Add network layer for cloud sync capabilities
