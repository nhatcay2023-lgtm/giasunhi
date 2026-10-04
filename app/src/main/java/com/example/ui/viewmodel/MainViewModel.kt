package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.TutorRepository
import com.example.service.VoiceManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class ScreenTab(val title: String, val iconEmoji: String) {
    HOME("Trang Chủ", "🏠"),
    AI_TUTOR("Gia Sư AI", "🦉"),
    LESSONS("Bài Học", "📚"),
    GAMES("Trò Chơi", "🎮"),
    REWARDS("Phần Thưởng", "🏆"),
    PARENT_ZONE("Phụ Huynh", "👨‍👩‍👧")
}

data class AppleGameQuestion(
    val num1: Int,
    val num2: Int,
    val isPlus: Boolean,
    val apples: List<Int>, // 3 apple choices
    val correctAnswer: Int
)

data class RhymeGameQuestion(
    val promptWord: String, // e.g. "Cái B..." (Cái Bàn)
    val hintMeaning: String, // e.g. "Đồ vật bé dùng để ngồi viết bài"
    val choices: List<String>, // e.g. ["an", "at", "am", "ap"]
    val correctRhyme: String
)

data class CompareGameQuestion(
    val leftNumber: Int,
    val rightNumber: Int,
    val leftEmojis: String,
    val rightEmojis: String
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = TutorRepository(database)
    val voiceManager = VoiceManager(application)

    // Current navigation tab
    private val _currentTab = MutableStateFlow(ScreenTab.HOME)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    // Lessons
    val allLessons: StateFlow<List<LessonItem>> = repository.allLessons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedSubject = MutableStateFlow<SubjectCategory?>(null)
    val selectedSubject: StateFlow<SubjectCategory?> = _selectedSubject.asStateFlow()

    private val _activeLessonDetail = MutableStateFlow<LessonItem?>(null)
    val activeLessonDetail: StateFlow<LessonItem?> = _activeLessonDetail.asStateFlow()

    // Chat
    val chatHistory: StateFlow<List<ChatMessageEntity>> = repository.chatHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isAITyping = MutableStateFlow(false)
    val isAITyping: StateFlow<Boolean> = _isAITyping.asStateFlow()

    private val _chatInputText = MutableStateFlow("")
    val chatInputText: StateFlow<String> = _chatInputText.asStateFlow()

    // Daily progress & Stats
    val todayProgress: StateFlow<DailyProgress?> = repository.observeTodayProgress()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allProgressHistory: StateFlow<List<DailyProgress>> = repository.allDailyProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Badges & Rewards
    val allBadges: StateFlow<List<BadgeItem>> = repository.allBadges
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _totalStars = MutableStateFlow(35)
    val totalStars: StateFlow<Int> = _totalStars.asStateFlow()

    private val _equippedAccessory = MutableStateFlow("cap") // "none", "cap", "glasses", "cape", "star"
    val equippedAccessory: StateFlow<String> = _equippedAccessory.asStateFlow()

    // Games State
    private val _selectedGameIndex = MutableStateFlow(0) // 0: Apple, 1: Rhyme, 2: Compare
    val selectedGameIndex: StateFlow<Int> = _selectedGameIndex.asStateFlow()

    private val _gameScore = MutableStateFlow(0)
    val gameScore: StateFlow<Int> = _gameScore.asStateFlow()

    private val _gameFeedback = MutableStateFlow<String?>(null)
    val gameFeedback: StateFlow<String?> = _gameFeedback.asStateFlow()

    // Current Apple Game
    private val _currentAppleQuestion = MutableStateFlow(generateAppleQuestion())
    val currentAppleQuestion: StateFlow<AppleGameQuestion> = _currentAppleQuestion.asStateFlow()

    // Current Rhyme Game
    private val _currentRhymeQuestion = MutableStateFlow(generateRhymeQuestion())
    val currentRhymeQuestion: StateFlow<RhymeGameQuestion> = _currentRhymeQuestion.asStateFlow()

    // Current Compare Game
    private val _currentCompareQuestion = MutableStateFlow(generateCompareQuestion())
    val currentCompareQuestion: StateFlow<CompareGameQuestion> = _currentCompareQuestion.asStateFlow()

    // Parent Zone Gate
    private val _isParentUnlocked = MutableStateFlow(false)
    val isParentUnlocked: StateFlow<Boolean> = _isParentUnlocked.asStateFlow()

    private val _reminderTime = MutableStateFlow("19:30")
    val reminderTime: StateFlow<String> = _reminderTime.asStateFlow()

    private val _dailyGoalMinutes = MutableStateFlow(15)
    val dailyGoalMinutes: StateFlow<Int> = _dailyGoalMinutes.asStateFlow()

    // Parent Community Posts
    val parentPosts: StateFlow<List<ParentCommunityPost>> = repository.parentPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun setSelectedSubject(subject: SubjectCategory?) {
        _selectedSubject.value = subject
    }

    fun openLessonDetail(lesson: LessonItem?) {
        _activeLessonDetail.value = lesson
        lesson?.let {
            voiceManager.speak(it.title + ". " + it.description)
        }
    }

    fun completeCurrentLesson(lesson: LessonItem) {
        viewModelScope.launch {
            repository.completeLesson(lesson)
            _totalStars.value += lesson.starsReward
            _activeLessonDetail.value = null
            voiceManager.speak("Chúc mừng bé đã hoàn thành bài học! Bé nhận được ${lesson.starsReward} ngôi sao vàng!")
        }
    }

    fun updateChatInput(text: String) {
        _chatInputText.value = text
    }

    fun sendChatMessage(text: String? = null) {
        val messageToSend = (text ?: _chatInputText.value).trim()
        if (messageToSend.isEmpty() || _isAITyping.value) return

        _chatInputText.value = ""
        _isAITyping.value = true

        viewModelScope.launch {
            try {
                val reply = repository.sendChatMessage(messageToSend)
                _totalStars.value += 1
                repository.recordQuizAnswer(SubjectCategory.MATH, isCorrect = true, starsEarned = 1, minutesSpent = 1)
                // Speak out the tutor response
                voiceManager.speak(reply.text)
            } finally {
                _isAITyping.value = false
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    // Educational Mini-Games Logic
    fun selectGame(index: Int) {
        _selectedGameIndex.value = index
        _gameFeedback.value = null
    }

    private fun generateAppleQuestion(): AppleGameQuestion {
        val isPlus = Random.nextBoolean()
        val n1 = if (isPlus) Random.nextInt(1, 6) else Random.nextInt(4, 10)
        val n2 = if (isPlus) Random.nextInt(1, 5) else Random.nextInt(1, n1)
        val correct = if (isPlus) n1 + n2 else n1 - n2

        val options = mutableSetOf(correct)
        while (options.size < 3) {
            val wrong = (correct + Random.nextInt(-3, 4)).coerceIn(1, 10)
            options.add(wrong)
        }
        return AppleGameQuestion(n1, n2, isPlus, options.toList().shuffled(), correct)
    }

    fun answerAppleGame(selectedAnswer: Int) {
        val q = _currentAppleQuestion.value
        if (selectedAnswer == q.correctAnswer) {
            _gameScore.value += 10
            _totalStars.value += 2
            _gameFeedback.value = "Chính xác! Bé hái được táo đỏ ngọt ngào! +2 ⭐"
            voiceManager.speak("Đúng rồi bé ơi! Bé giỏi quá!")
            viewModelScope.launch {
                repository.recordQuizAnswer(SubjectCategory.MATH, true, 2, 1)
            }
            _currentAppleQuestion.value = generateAppleQuestion()
        } else {
            _gameFeedback.value = "Bé thử đếm lại xem nào! Quả táo chưa rơi đâu nhé!"
            voiceManager.speak("Chưa đúng rồi, bé đếm lại ngón tay nhé!")
        }
    }

    private fun generateRhymeQuestion(): RhymeGameQuestion {
        val questions = listOf(
            RhymeGameQuestion("Cái b...", "Đồ vật bé dùng để ngồi viết bài học", listOf("an", "at", "am", "ap"), "an"),
            RhymeGameQuestion("B... cơm", "Dùng để đựng cơm dẻo canh ngọt", listOf("at", "an", "on", "am"), "at"),
            RhymeGameQuestion("Quả c...", "Trái cây tròn mọng nước, nhiều vitamin C", listOf("am", "an", "ap", "at"), "am"),
            RhymeGameQuestion("Xe đ...", "Phương tiện 2 bánh bé dùng chân đạp", listOf("ap", "at", "am", "an"), "ap"),
            RhymeGameQuestion("Đàn g...", "Những chú gà con kêu chiếp chiếp", listOf("a", "o", "e", "u"), "a")
        )
        return questions.random()
    }

    fun answerRhymeGame(selectedChoice: String) {
        val q = _currentRhymeQuestion.value
        if (selectedChoice == q.correctRhyme) {
            _gameScore.value += 10
            _totalStars.value += 2
            _gameFeedback.value = "Xuất sắc! Bé ghép đúng từ rồi! +2 ⭐"
            voiceManager.speak("Hoan hô bé! Ghép vần rất chuẩn!")
            viewModelScope.launch {
                repository.recordQuizAnswer(SubjectCategory.VIETNAMESE, true, 2, 1)
            }
            _currentRhymeQuestion.value = generateRhymeQuestion()
        } else {
            _gameFeedback.value = "Bé chọn lại vần phù hợp hơn nhé!"
            voiceManager.speak("Bé thử chọn lại xem sao nhé!")
        }
    }

    private fun generateCompareQuestion(): CompareGameQuestion {
        val left = Random.nextInt(1, 10)
        var right = Random.nextInt(1, 10)
        if (Random.nextFloat() < 0.25f) right = left // 25% chance of equal

        val leftEmojis = "🍎 ".repeat(left).trim()
        val rightEmojis = "🍏 ".repeat(right).trim()
        return CompareGameQuestion(left, right, leftEmojis, rightEmojis)
    }

    fun answerCompareGame(symbol: String) {
        val q = _currentCompareQuestion.value
        val isCorrect = when (symbol) {
            ">" -> q.leftNumber > q.rightNumber
            "<" -> q.leftNumber < q.rightNumber
            "=" -> q.leftNumber == q.rightNumber
            else -> false
        }

        if (isCorrect) {
            _gameScore.value += 10
            _totalStars.value += 2
            _gameFeedback.value = "Đúng rồi! Cá sấu há to miệng ăn số lớn hơn! +2 ⭐"
            voiceManager.speak("Chính xác! Cá sấu há miệng đúng chỗ rồi!")
            viewModelScope.launch {
                repository.recordQuizAnswer(SubjectCategory.MATH, true, 2, 1)
            }
            _currentCompareQuestion.value = generateCompareQuestion()
        } else {
            _gameFeedback.value = "Cá sấu chưa no! Bé chọn lại dấu xem bên nào nhiều hơn nhé!"
            voiceManager.speak("Bên nào nhiều hơn nhỉ? Bé nhìn kỹ nhé!")
        }
    }

    // Mascot accessories
    fun equipAccessory(accId: String) {
        _equippedAccessory.value = accId
        voiceManager.speak("Bạn Cú Mèo cảm ơn bé đã diện đồ mới thật đẹp!")
    }

    // Parent Mode Gate & Configuration
    fun unlockParentZone(passcode: String): Boolean {
        if (passcode == "1234" || passcode == "2026") {
            _isParentUnlocked.value = true
            return true
        }
        return false
    }

    fun lockParentZone() {
        _isParentUnlocked.value = false
    }

    fun updateReminderTime(time: String) {
        _reminderTime.value = time
    }

    fun updateDailyGoal(minutes: Int) {
        _dailyGoalMinutes.value = minutes
    }

    fun togglePostLike(post: ParentCommunityPost) {
        viewModelScope.launch {
            repository.toggleLikePost(post)
        }
    }

    fun addNewParentPost(title: String, content: String, category: String, author: String) {
        viewModelScope.launch {
            val newPost = ParentCommunityPost(
                id = "user_post_${System.currentTimeMillis()}",
                author = author.ifBlank { "Phụ huynh lớp 1" },
                authorRole = "Phụ huynh chia sẻ",
                title = title,
                content = content,
                category = category,
                likesCount = 1,
                commentsCount = 0,
                isSaved = true,
                date = "Vừa xong"
            )
            repository.addParentPost(newPost)
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceManager.shutdown()
    }
}
