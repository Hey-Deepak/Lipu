# Build In Public - LinkedIn Post Strategy CLI

A Kotlin CLI tool that analyzes git commits and generates LinkedIn post ideas for building in public.

## Usage

```bash
cd build-in-public

# Analyze the latest commit (HEAD)
../gradlew run

# Analyze a specific commit
../gradlew run --args="<commit-hash>"

# Generate a weekly summary (last 7 commits)
../gradlew run --args="weekly HEAD~7"
```

## How It Works

1. Parses git diff for any commit
2. Classifies changed files into categories (AI/Prompt Engineering, New Feature, Bug Fix, etc.)
3. Ranks categories by signal strength
4. Picks the best LinkedIn post template
5. Generates a draft post with hashtags

## Project Structure

```
build-in-public/
├── build.gradle.kts              # Gradle build config (Kotlin 2.0.21, JVM 21)
├── settings.gradle.kts           # Standalone Gradle settings with repositories
└── src/main/kotlin/com/streamliners/buildinpublic/
    ├── Main.kt                   # CLI entry point
    ├── analyzer/
    │   └── GitDiffAnalyzer.kt    # Git diff parsing & categorization engine
    ├── generator/
    │   ├── PostGenerator.kt      # LinkedIn post draft generator
    │   └── ReportPrinter.kt      # Terminal report formatter
    ├── model/
    │   ├── AnalysisResult.kt     # Analysis output data structures
    │   ├── DiffCategory.kt       # Change category enum with priorities
    │   ├── FileClassification.kt # File path to category pattern matching
    │   └── PostTemplate.kt       # Post template definitions with placeholders
    ├── strategy/
    │   ├── CodebaseProfile.kt    # Lipu app metadata & tech stack info
    │   ├── LinkedInStrategy.kt   # Content pillars, hashtags & engagement tips
    │   └── PostIdeas.kt          # Pre-generated post ideas tied to codebase
    └── util/
        └── GitCommand.kt         # Git CLI wrapper for safe repository access
```

## File Descriptions

### Entry Point

**Main.kt** - CLI entry point that parses arguments and routes to three execution modes: single commit analysis (default HEAD), specific commit analysis, or weekly summary. Calls `findRepoRoot()` to locate the git repository root.

### Analyzer

**GitDiffAnalyzer.kt** - Core analysis engine. Parses git diffs, classifies changed files using pattern matching, detects content signals (AI prompts, bug fixes, etc.) via regex, and produces structured `AnalysisResult` findings. Handles both single commits (`analyzeCommit()`) and commit ranges (`analyzeRange()`).

### Generators

**PostGenerator.kt** - Converts `AnalysisResult` into LinkedIn post drafts. Dispatches to category-specific generators (`generatePromptPost()`, `generateFeaturePost()`, `generateArchitecturePost()`, etc.) and can produce weekly summary posts from multiple commits.

**ReportPrinter.kt** - Formats analysis reports and post drafts for terminal output. Displays stats (files changed, insertions/deletions), findings with priority indicators, and the generated post draft.

### Data Models

**AnalysisResult.kt** - Central data structure holding commit analysis output: metadata, list of `Finding` objects (each with title, detail, affected files, code snippet, suggested template), and `CommitStats` (files changed, insertions, deletions). Provides `topFindings()` and `primaryCategory()` helpers.

**DiffCategory.kt** - Enum of 8 change categories: `AI_PROMPT`, `NEW_FEATURE`, `ARCHITECTURE`, `BUG_FIX`, `UI_UX`, `DATA_MODEL`, `DEPENDENCY`, `CONFIG`. Each has a display name, emoji, priority level (VERY_HIGH to LOW), and description.

**FileClassification.kt** - Maps file paths to `DiffCategory` using regex rules (e.g., `genAI/` -> `AI_PROMPT`). Also defines `contentSignals` - regex patterns that detect category-relevant content within diffs (e.g., `SYSTEM_INSTRUCTION` for AI prompts).

**PostTemplate.kt** - Enum of 7 post templates: `FEATURE_ANNOUNCEMENT`, `PROMPT_ENGINEERING`, `ARCHITECTURE_DECISION`, `BUG_FIX_STORY`, `TECHNICAL_DEEP_DIVE`, `WEEKLY_UPDATE`, `TECH_STACK_DECISION`. Each has applicable categories, structure with placeholders (`{{APP_NAME}}`, `{{FEATURE_NAME}}`, etc.), and a `forCategory()` mapper.

### Strategy

**CodebaseProfile.kt** - Domain knowledge about the Lipu app: package name, SDK versions, `TechStack` enum (13 technologies: Compose, Gemini, Room, Koin, Firebase, etc.), 5 major `Feature` descriptions, high-value watch files, WIP features, and notable code patterns.

**LinkedInStrategy.kt** - LinkedIn content strategy config: `ContentPillar` enum with target percentages (AI_FEATURES 40%, ANDROID_ENGINEERING 30%, PRODUCT_BUILDING 20%, WEEKLY_UPDATES 10%), predefined hashtag groups, and engagement tips.

**PostIdeas.kt** - Editorial calendar with 6 pre-generated `PostIdea` objects tied to codebase features: AI time tracking, Gemini CSV output, voice-first Android, NL-to-SQL, app rename story, Room + Flow reactive chat.

### Utilities

**GitCommand.kt** - Wrapper around git CLI using `ProcessBuilder`. Provides structured access to commit info, changed/new/deleted file lists, diff summaries, full diffs, commit ranges, and weekly stats. All git operations go through the `exec()` function.

### Build Files

**build.gradle.kts** - Kotlin JVM project with `kotlinx-serialization-json`, `kotlinx-coroutines-core`, and `jgit` dependencies. Uses Kotlin 2.0.21 with JVM 21 toolchain. Main class: `com.streamliners.buildinpublic.MainKt`.

**settings.gradle.kts** - Standalone Gradle settings (decoupled from root Android project) with Maven Central and Gradle Plugin Portal repositories.
