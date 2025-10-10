# ReMind - Theme & Style Guide

## 🎨 Design System

### Color Palette

#### Primary Colors (Indigo)

- **Primary**: `#6366F1` - Main brand color
- **Primary Dark**: `#4F46E5` - Pressed/active states
- **Primary Light**: `#818CF8` - Hover states
- **Primary Container**: `#E0E7FF` - Background tints
- **On Primary**: `#FFFFFF` - Text on primary

#### Secondary Colors (Pink)

- **Secondary**: `#EC4899` - Accent color
- **Secondary Dark**: `#DB2777`
- **Secondary Light**: `#F472B6`
- **Secondary Container**: `#FCE7F3`
- **On Secondary**: `#FFFFFF`

#### Background & Surface

- **Background**: `#F8FAFC` - Main app background
- **Surface**: `#FFFFFF` - Card/surface background
- **Surface Variant**: `#F1F5F9` - Alternate surfaces
- **Card Background**: `#FFFFFF`
- **Card Stroke**: `#E2E8F0`

#### Text Colors

- **Text Primary**: `#0F172A` - Main text
- **Text Secondary**: `#64748B` - Subtitles
- **Text Tertiary**: `#94A3B8` - Hints/disabled

#### Status Colors

- **Success**: `#10B981` - Completed habits
- **Warning**: `#F59E0B` - Pending actions
- **Error**: `#EF4444` - Missed habits
- **Info**: `#3B82F6` - Informational

#### Category Colors

- **Health**: `#10B981` (Green)
- **Mindfulness**: `#8B5CF6` (Purple)
- **Learning**: `#F59E0B` (Orange)
- **Hydration**: `#3B82F6` (Blue)
- **Sleep**: `#6366F1` (Indigo)

#### Streak Colors

- **Streak Color**: `#F97316` (Orange)
- **Streak Badge BG**: `#FFF7ED` (Light orange)

---

## 🌙 Dark Mode

### Dark Mode Colors

All colors automatically adapt to dark mode with optimized variants:

#### Primary (Dark Mode)

- **Primary**: `#818CF8` (Lighter indigo)
- **Primary Container**: `#312E81` (Dark indigo)

#### Background (Dark Mode)

- **Background**: `#0F172A` (Dark slate)
- **Surface**: `#1E293B` (Card surface)
- **Surface Variant**: `#334155`

#### Text (Dark Mode)

- **Text Primary**: `#F1F5F9` (Light)
- **Text Secondary**: `#CBD5E1`
- **Text Tertiary**: `#94A3B8`

---

## 📐 Spacing System

```kotlin
spacing_xs:    4dp
spacing_sm:    8dp
spacing_md:    12dp
spacing_lg:    16dp
spacing_xl:    20dp
spacing_xxl:   24dp
spacing_xxxl:  32dp
```

---

## 🔘 Corner Radius

```kotlin
corner_radius_sm:  8dp   // Small components
corner_radius_md:  12dp  // Medium components (buttons, cards)
corner_radius_lg:  16dp  // Large components (cards)
corner_radius_xl:  20dp  // Extra large
```

---

## 🎯 Component Styles

### Buttons

#### Primary Button (`Widget.ReMind.Button`)

```xml
<item name="cornerRadius">12dp</item>
<item name="android:textAllCaps">false</item>
<item name="backgroundTint">@color/primary</item>
```

**Usage:**

```xml
<com.google.android.material.button.MaterialButton
    style="@style/Widget.ReMind.Button"
    android:text="Save Habit" />
```

#### Outlined Button (`Widget.ReMind.Button.Outlined`)

```xml
<item name="cornerRadius">12dp</item>
<item name="strokeColor">@color/primary</item>
<item name="strokeWidth">2dp</item>
```

### Cards (`Widget.ReMind.CardView`)

```xml
<item name="cardCornerRadius">16dp</item>
<item name="cardElevation">2dp</item>
<item name="cardBackgroundColor">@color/card_background</item>
```

### FAB (`Widget.ReMind.FloatingActionButton`)

```xml
<item name="cornerSize">16dp</item>
<item name="backgroundTint">@color/primary</item>
```

### Chips (`Widget.ReMind.Chip`)

```xml
<item name="chipCornerRadius">12dp</item>
```

### Text Input (`Widget.ReMind.TextInputLayout`)

```xml
<item name="boxCornerRadius">12dp</item>
<item name="boxStrokeColor">@color/primary</item>
```

---

## 📝 Typography

### Headlines

#### Headline 1 (`TextAppearance.ReMind.Headline1`)

- **Size**: 32sp
- **Weight**: Medium
- **Color**: Text Primary
- **Usage**: Main titles

#### Headline 2 (`TextAppearance.ReMind.Headline2`)

- **Size**: 24sp
- **Weight**: Medium
- **Color**: Text Primary
- **Usage**: Section titles

### Body Text

#### Body 1 (`TextAppearance.ReMind.Body1`)

- **Size**: 16sp
- **Weight**: Regular
- **Color**: Text Primary
- **Usage**: Main content

#### Body 2 (`TextAppearance.ReMind.Body2`)

- **Size**: 14sp
- **Weight**: Regular
- **Color**: Text Secondary
- **Usage**: Secondary content

---

## 🎭 Shape System

### Small Components (8dp corners)

- Small buttons
- Chips
- Small cards

### Medium Components (12dp corners)

- Standard buttons
- Text inputs
- Medium cards

### Large Components (16dp corners)

- Large cards
- Bottom sheets
- Dialogs

---

## 💫 Elevation System

```kotlin
elevation_sm:  2dp   // Cards
elevation_md:  4dp   // Raised cards
elevation_lg:  8dp   // Modals/Dialogs
```

---

## 📏 Icon Sizes

```kotlin
icon_size_sm:  16dp  // Small inline icons
icon_size_md:  24dp  // Standard icons
icon_size_lg:  32dp  // Large feature icons
icon_size_xl:  48dp  // Hero icons
```

---

## 🎨 Usage Examples

### Using Theme Colors in XML

```xml
<!-- Button with primary color -->
<Button
    android:backgroundTint="@color/primary"
    android:textColor="@color/on_primary" />

<!-- Card with theme style -->
<com.google.android.material.card.MaterialCardView
    style="@style/Widget.ReMind.CardView">
    <!-- Content -->
</com.google.android.material.card.MaterialCardView>

<!-- Text with theme typography -->
<TextView
    android:textAppearance="@style/TextAppearance.ReMind.Headline1"
    android:text="Daily Habits" />
```

### Using Theme Colors in Code

```kotlin
// Get theme colors
val primary = ContextCompat.getColor(context, R.color.primary)
val textPrimary = ContextCompat.getColor(context, R.color.text_primary)

// Apply to views
view.setBackgroundColor(primary)
textView.setTextColor(textPrimary)
```

---

## 🌈 Category Color Mapping

| Category    | Color Code | Color Name |
| ----------- | ---------- | ---------- |
| Health      | `#10B981`  | Green      |
| Mindfulness | `#8B5CF6`  | Purple     |
| Learning    | `#F59E0B`  | Orange     |
| Hydration   | `#3B82F6`  | Blue       |
| Sleep       | `#6366F1`  | Indigo     |

---

## ✨ Theme Features

### 1. **Automatic Dark Mode**

- All colors adapt automatically
- Status bar and navigation bar match theme
- Optimized contrast for readability

### 2. **Material Design 3**

- Modern Material You components
- Dynamic color system
- Smooth animations

### 3. **Consistent Spacing**

- Standardized spacing scale
- Predictable layouts
- Better visual hierarchy

### 4. **Accessibility**

- WCAG compliant contrast ratios
- Clear text hierarchy
- Touch-friendly sizes (min 48dp)

### 5. **Custom Shapes**

- Rounded corners throughout
- Consistent corner radius
- Modern, friendly appearance

---

## 🎯 Best Practices

### 1. **Use Theme Colors**

```xml
<!-- ✅ Good -->
android:textColor="@color/text_primary"

<!-- ❌ Bad -->
android:textColor="#000000"
```

### 2. **Use Dimension Resources**

```xml
<!-- ✅ Good -->
android:padding="@dimen/spacing_lg"

<!-- ❌ Bad -->
android:padding="16dp"
```

### 3. **Use Theme Styles**

```xml
<!-- ✅ Good -->
<Button style="@style/Widget.ReMind.Button" />

<!-- ❌ Bad -->
<Button android:background="..." android:cornerRadius="..." />
```

### 4. **Consistent Corner Radius**

```xml
<!-- ✅ Good -->
app:cardCornerRadius="@dimen/corner_radius_lg"

<!-- ❌ Bad -->
app:cardCornerRadius="15dp"
```

---

## 🔄 Migration Guide

### Updating Existing Components

#### Before:

```xml
<Button
    android:background="#6366F1"
    android:textColor="#FFFFFF"
    android:padding="16dp" />
```

#### After:

```xml
<com.google.android.material.button.MaterialButton
    style="@style/Widget.ReMind.Button"
    android:padding="@dimen/spacing_lg" />
```

---

## 📱 Platform Specific

### Status Bar

- **Light Mode**: Light status bar with dark icons
- **Dark Mode**: Dark status bar with light icons
- Matches background color seamlessly

### Navigation Bar

- Matches background color
- Adapts to theme mode
- Gesture navigation friendly

---

## 🎨 Design Tokens Summary

```kotlin
// Brand
primary:        #6366F1 (Indigo)
secondary:      #EC4899 (Pink)

// Semantics
success:        #10B981 (Green)
warning:        #F59E0B (Orange)
error:          #EF4444 (Red)
info:           #3B82F6 (Blue)

// Spacing
base:           8dp
scale:          4dp, 8dp, 12dp, 16dp, 20dp, 24dp, 32dp

// Radius
scale:          8dp, 12dp, 16dp, 20dp

// Typography
scale:          12sp, 14sp, 16sp, 18sp, 20sp, 24sp, 32sp
weights:        Regular, Medium, Bold
```

---

## 🚀 Future Enhancements

- [ ] Dynamic color theming (Material You)
- [ ] Custom font families
- [ ] Animation tokens
- [ ] More component variants
- [ ] Accessibility themes (high contrast)
- [ ] Theme previewer

---

**Built with ❤️ following Material Design 3 guidelines**
