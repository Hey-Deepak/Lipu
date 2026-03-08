#!/bin/bash
# analyze_diff.sh - Analyzes git diffs to identify post-worthy changes
# Usage: ./analyze_diff.sh [commit_hash] or ./analyze_diff.sh (for last commit)

COMMIT=${1:-HEAD}
PREV_COMMIT="${COMMIT}~1"

echo "============================================="
echo "  Build-in-Public: Diff Analysis Report"
echo "  Commit: $(git log --oneline -1 $COMMIT)"
echo "============================================="
echo ""

# Get changed files
CHANGED_FILES=$(git diff --name-only $PREV_COMMIT $COMMIT 2>/dev/null)

if [ -z "$CHANGED_FILES" ]; then
    echo "No changes found or invalid commit reference."
    exit 1
fi

echo "📁 Changed files:"
echo "$CHANGED_FILES"
echo ""

# Category detection
AI_CHANGES=false
FEATURE_CHANGES=false
UI_CHANGES=false
DATA_CHANGES=false
ARCH_CHANGES=false
DEPENDENCY_CHANGES=false
CONFIG_CHANGES=false

AI_FILES=""
FEATURE_FILES=""
UI_FILES=""
DATA_FILES=""

while IFS= read -r file; do
    case "$file" in
        *genAI/* | *GeminiModel*)
            AI_CHANGES=true
            AI_FILES="$AI_FILES $file"
            ;;
        *feature/chat/ChatViewModel* | *feature/chat/ChatScreen* | *feature/voice/*)
            FEATURE_CHANGES=true
            FEATURE_FILES="$FEATURE_FILES $file"
            ;;
        *feature/chat/comp/* | *ui/theme/*)
            UI_CHANGES=true
            UI_FILES="$UI_FILES $file"
            ;;
        *domain/model/* | *data/local/dao/* | *data/local/LocalDB*)
            DATA_CHANGES=true
            DATA_FILES="$DATA_FILES $file"
            ;;
        *di/* | *ui/main/NavHostGraph* | *ui/main/Screen*)
            ARCH_CHANGES=true
            ;;
        *.gradle* | *libs.versions.toml*)
            DEPENDENCY_CHANGES=true
            ;;
        *AndroidManifest* | *proguard* | *secrets*)
            CONFIG_CHANGES=true
            ;;
    esac
done <<< "$CHANGED_FILES"

echo "============================================="
echo "  Detected Categories"
echo "============================================="

if $AI_CHANGES; then
    echo "🤖 AI/PROMPT ENGINEERING CHANGES [HIGH PRIORITY]"
    echo "   Files:$AI_FILES"
    # Check for specific prompt changes
    if git diff $PREV_COMMIT $COMMIT -- '*GeminiModel*' | grep -q "SYSTEM_INSTRUCTION"; then
        echo "   ⭐ System prompt modified - GREAT post material!"
    fi
    if git diff $PREV_COMMIT $COMMIT -- '*GeminiModel*' | grep -q "modelName"; then
        echo "   ⭐ Model changed - Write about model selection!"
    fi
    if git diff $PREV_COMMIT $COMMIT -- '*GeminiModel*' | grep -q "temperature\|topK\|topP"; then
        echo "   ⭐ Generation config tuned - Write about parameter tuning!"
    fi
    echo ""
fi

if $FEATURE_CHANGES; then
    echo "🚀 NEW FEATURE / FEATURE CHANGES [HIGH PRIORITY]"
    echo "   Files:$FEATURE_FILES"
    # Check for new files (new feature) vs modified (enhancement)
    NEW_FILES=$(git diff --diff-filter=A --name-only $PREV_COMMIT $COMMIT -- 'app/src/main/java/*feature*')
    if [ -n "$NEW_FILES" ]; then
        echo "   ⭐ New feature files detected: $NEW_FILES"
    fi
    echo ""
fi

if $UI_CHANGES; then
    echo "🎨 UI/UX CHANGES [MEDIUM PRIORITY]"
    echo "   Files:$UI_FILES"
    echo ""
fi

if $DATA_CHANGES; then
    echo "💾 DATA MODEL CHANGES [MEDIUM PRIORITY]"
    echo "   Files:$DATA_FILES"
    echo ""
fi

if $ARCH_CHANGES; then
    echo "🏗️  ARCHITECTURE CHANGES [HIGH PRIORITY]"
    echo ""
fi

if $DEPENDENCY_CHANGES; then
    echo "📦 DEPENDENCY CHANGES [LOW-MEDIUM PRIORITY]"
    echo ""
fi

if $CONFIG_CHANGES; then
    echo "⚙️  CONFIG CHANGES [LOW PRIORITY]"
    echo ""
fi

# Stats
echo "============================================="
echo "  Commit Stats"
echo "============================================="
echo "Files changed: $(echo "$CHANGED_FILES" | wc -l)"
git diff --stat $PREV_COMMIT $COMMIT | tail -1
echo ""

# Suggested post type
echo "============================================="
echo "  Suggested Post Type"
echo "============================================="
if $AI_CHANGES; then
    echo "→ Use Template 2: AI/Prompt Engineering Lesson"
elif $FEATURE_CHANGES; then
    echo "→ Use Template 1: Feature Announcement"
elif $UI_CHANGES; then
    echo "→ Use Template 7: Before/After UI Changes"
elif $DATA_CHANGES; then
    echo "→ Use Template 6: Technical Deep-Dive"
elif $ARCH_CHANGES; then
    echo "→ Use Template 3: Architecture Decision"
elif $DEPENDENCY_CHANGES; then
    echo "→ Use Template 8: Dependency/Tech Stack Decision"
else
    echo "→ Use Template 4: Bug Fix Story"
fi
