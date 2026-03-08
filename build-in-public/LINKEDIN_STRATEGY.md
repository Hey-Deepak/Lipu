# LinkedIn Build-in-Public Strategy for Lipu

## Content Pillars

### Pillar 1: AI-Powered Features (40% of posts)
Lipu's core differentiator is using Gemini AI for conversational time tracking. This is inherently interesting to the LinkedIn audience.

**Topics to cover:**
- Prompt engineering iterations (every change to `GeminiModel.kt`)
- AI parsing reliability (CSV output from natural conversation)
- Natural language to SQL (Insights feature)
- Model selection decisions (Gemini 1.5 Flash vs alternatives)
- Handling AI hallucinations in structured output
- Voice + AI: the conversational UX

### Pillar 2: Android Engineering (30% of posts)
Technical implementation details that help other developers.

**Topics to cover:**
- Jetpack Compose patterns (MessagesList, AnimatedContent)
- Room DB with Flow for reactive chat UI
- Koin DI setup for Android
- Coroutine patterns (suspendCoroutine for TTS)
- Navigation Compose multi-screen architecture
- Google Sheets API integration

### Pillar 3: Product/Indie Building (20% of posts)
The journey of building a product.

**Topics to cover:**
- Feature prioritization decisions
- Why time tracking needs a conversational approach
- User feedback and iterations
- The Timify → Lipu rebrand story
- Shipping incomplete features (PieChart, SheetSync stubs)

### Pillar 4: Weekly Updates (10% of posts)
Consistency and accountability.

**Topics to cover:**
- Weekly commit summaries
- Metrics (lines changed, features shipped)
- Roadmap updates

---

## Immediate Post Ideas from Current Codebase

### Post 1: "I'm building an AI time tracker - here's why chat beats forms"
- Angle: Product vision
- Content: Why conversational time tracking is better than manual entry
- Code reference: `NORMAL_CHAT_SYSTEM_INSTRUCTION` - show the AI's role

### Post 2: "How I taught Gemini to output structured CSV from casual conversation"
- Angle: Prompt engineering
- Content: The system instruction for CSV output format
- Code reference: The CSV format specification in `GeminiModel.kt` lines 26-38
- Show the `saveTaskInfoToLocal()` parsing logic

### Post 3: "Voice-first Android app: Making TTS work with Kotlin Coroutines"
- Angle: Technical deep-dive
- Content: `suspendCoroutine` wrapper around Android TTS callbacks
- Code reference: `TTSHelper.kt` - the async TTS pattern

### Post 4: "Natural Language to SQL: Building an insights engine for a time tracker"
- Angle: AI/Engineering
- Content: How user questions become SQL queries via Gemini
- Code reference: `INSIGHTS_CHAT_SYSTEM_INSTRUCTION` + `InsightsChat.kt`
- Note: Currently feature-flagged off - talk about why

### Post 5: "From Timify to Lipu: Why I renamed my app mid-development"
- Angle: Product/Branding
- Content: Package naming inconsistency (`timify` vs `lipu`) tells a story
- Real artifact: Mixed package names in the codebase

### Post 6: "I use Room + Flow for a reactive chat UI. Here's the pattern."
- Angle: Android engineering
- Content: `ChatHistoryDao.getList()` returns `Flow`, collected in ViewModel
- Code reference: `ChatViewModel.loadChat()` with `collectLatest`

---

## Post Frequency
- **Target**: 3-4 posts per week
- **Timing**: Best LinkedIn engagement: Tue-Thu, 8-10 AM
- **Mix**: 2 technical + 1 product + 1 update per week

---

## Hashtag Strategy
**Primary (always include 2-3):**
- #BuildInPublic
- #AndroidDev
- #IndieHacker

**Topic-specific (rotate):**
- #PromptEngineering #AI #GenerativeAI #Gemini
- #Kotlin #JetpackCompose #MobileDev
- #ProductDevelopment #StartupJourney
- #CleanArchitecture #SoftwareEngineering

---

## Engagement Strategy
1. **Ask questions** at the end of posts ("Would you have made the same choice?")
2. **Share code snippets** - technical posts get saved and shared
3. **Show vulnerability** - share failures, bugs, wrong decisions
4. **Be specific** - "I changed temperature from 1.0 to 0.7" beats "I tuned the AI"
5. **Visuals** - Screenshots of the app, architecture diagrams, before/after
