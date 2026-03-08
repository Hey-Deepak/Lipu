# Git Diff Analysis Strategy
## Auto-Extracting "Interesting Findings" from Lipu Commits

This document defines what categories of changes can be auto-detected from git diffs, and how to classify them for LinkedIn posts.

---

## Detection Categories

### 1. NEW FEATURE ADDED
**Detection signals in diff:**
- New Screen/Route added in `Screen.kt` or `NavHostGraph.kt`
- New `@Composable` function in a new file under `feature/`
- New ViewModel class or new public function in existing ViewModel
- New DAO method (new `@Query`, `@Insert`, `@Delete`)
- New `@Entity` class (new Room table)
- New dependency in `build.gradle.kts`

**Example findings:**
- "Added PieChart visualization for daily time breakdown"
- "Introduced voice input mode for hands-free time tracking"
- "Added Google Sheets sync to export time data"

**LinkedIn angle:** Feature announcement, problem-solution narrative

---

### 2. AI PROMPT ENGINEERING CHANGES
**Detection signals in diff:**
- Changes to `NORMAL_CHAT_SYSTEM_INSTRUCTION` or `INSIGHTS_CHAT_SYSTEM_INSTRUCTION` in `GeminiModel.kt`
- Changes to model name (e.g., `gemini-1.5-flash` → `gemini-2.0-flash`)
- Changes to generation config (`temperature`, `topK`, `topP`, `maxOutputTokens`)
- New system instruction constants
- Changes to `responseMimeType`

**Example findings:**
- "Refined AI prompt to better extract overlapping time slots"
- "Switched from Gemini 1.5 Flash to Gemini 2.0 for better accuracy"
- "Tuned temperature from 1.0 to 0.7 for more consistent CSV output"

**LinkedIn angle:** AI/LLM lessons learned, prompt engineering tips

---

### 3. ARCHITECTURE PATTERN CHANGES
**Detection signals in diff:**
- New package/directory created
- Class moved between packages (file path changes)
- New DI module or provider in `Koin.kt`
- Introduction of new base classes or interfaces
- Repository pattern additions (`*Repo.kt`, `*Repository.kt`)
- New extension function files

**Example findings:**
- "Migrated from direct API calls to Repository pattern"
- "Introduced Koin modules for better separation of concerns"
- "Extracted reusable extensions for Gemini model interactions"

**LinkedIn angle:** Clean architecture decisions, refactoring stories

---

### 4. UI/UX CHANGES
**Detection signals in diff:**
- Changes in `@Composable` functions (new UI components)
- Theme changes (`Color.kt`, `Theme.kt`, `Type.kt`)
- New Material 3 components used
- Layout changes (Modifier chains, padding, sizing)
- New icons or visual elements
- Animation additions (`AnimatedContent`, `animate*`)

**Example findings:**
- "Added animated transitions between text and voice input modes"
- "Redesigned message cards with timestamp alignment"
- "Implemented Material 3 dynamic color theming"

**LinkedIn angle:** Design decisions, UX improvements, before/after

---

### 5. DATA MODEL CHANGES
**Detection signals in diff:**
- Changes to `@Entity` classes (new fields, type changes)
- Room database version bump in `LocalDB.kt`
- New migration classes
- DAO query changes
- New data classes in `domain/model/`

**Example findings:**
- "Added custom attributes system for flexible task metadata"
- "Migrated database schema to support multi-day analytics"

**LinkedIn angle:** Data modeling decisions, schema evolution

---

### 6. BUG FIXES
**Detection signals in diff:**
- Small, targeted changes (< 10 lines modified)
- Fix in parsing logic (e.g., CSV parsing in `saveTaskInfoToLocal`)
- Null safety additions (`?.`, `?:`, `!!` removals)
- Error handling additions (`try-catch`, `runCatching`)
- Edge case handling in time calculations (`TimeDiff.kt`)

**Example findings:**
- "Fixed time parsing crash when AI returns 'just now' instead of timestamp"
- "Handled edge case where CSV response has trailing newline"

**LinkedIn angle:** Debugging stories, lessons learned

---

### 7. DEPENDENCY/TOOLING CHANGES
**Detection signals in diff:**
- Changes in `build.gradle.kts` dependencies section
- New plugins added
- Version bumps in `libs.versions.toml`
- New Gradle properties

**Example findings:**
- "Migrated from Retrofit to Ktor for networking"
- "Added Firebase Analytics for usage tracking"
- "Upgraded to Room 2.6.1 for KSP support"

**LinkedIn angle:** Tech stack decisions, library comparisons

---

### 8. CONFIGURATION CHANGES
**Detection signals in diff:**
- Feature flags toggled (e.g., `ENABLE_INSIGHTS_CHAT`)
- Build config changes
- ProGuard rules
- Manifest changes (`AndroidManifest.xml`)
- SDK version changes (`minSdk`, `targetSdk`, `compileSdk`)

**Example findings:**
- "Enabled experimental Insights chat feature"
- "Bumped target SDK to 35 for Android 15 compatibility"

**LinkedIn angle:** Release decisions, feature flag strategies

---

## Diff Analysis Algorithm

```
For each commit:
1. Get list of changed files (git diff --name-only)
2. Categorize each file by its path:
   - feature/genAI/*       → Check for AI PROMPT changes
   - feature/chat/*        → Check for FEATURE or UI changes
   - domain/model/*        → Check for DATA MODEL changes
   - data/local/*          → Check for DATA MODEL changes
   - di/*                  → Check for ARCHITECTURE changes
   - ui/theme/*            → Check for UI changes
   - *.gradle*             → Check for DEPENDENCY changes
   - AndroidManifest.xml   → Check for CONFIG changes
3. Analyze diff content:
   - New files = likely NEW FEATURE
   - Deleted files = REFACTORING or REMOVAL
   - Small changes in existing files = BUG FIX or TWEAK
   - String literal changes in GeminiModel = PROMPT ENGINEERING
4. Generate finding summary with category tag
```

---

## Priority Ranking for LinkedIn Posts

| Priority | Category               | Engagement Potential |
|----------|------------------------|---------------------|
| 1        | AI Prompt Engineering   | Very High           |
| 2        | New Feature Added       | High                |
| 3        | Architecture Patterns   | High                |
| 4        | Bug Fix (interesting)   | Medium-High         |
| 5        | UI/UX Changes           | Medium              |
| 6        | Data Model Changes      | Medium              |
| 7        | Dependency Changes      | Low-Medium          |
| 8        | Config Changes          | Low                 |

---

## Lipu-Specific Watch Points

These are areas in the Lipu codebase that are particularly "post-worthy" when changed:

1. **`GeminiModel.kt`** - Any prompt change is gold for content
2. **`ChatViewModel.kt:sendPrompt()`** - Core AI interaction loop
3. **`ChatViewModel.kt:saveTaskInfoToLocal()`** - The CSV parsing magic
4. **`InsightsChat.kt`** - Natural language to SQL is compelling content
5. **`ENABLE_INSIGHTS_CHAT`** - Feature flag toggle = launch story
6. **`Screen.kt`** - New screens = new features
7. **`TTSHelper.kt`** - Voice features are demo-friendly
8. **Package rename timify→lipu** - Branding/naming story
