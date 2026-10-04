package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.network.GeminiClient
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TutorRepository(private val database: AppDatabase) {

    private val lessonDao = database.lessonDao()
    private val quizDao = database.quizDao()
    private val progressDao = database.progressDao()
    private val badgeDao = database.badgeDao()
    private val chatMessageDao = database.chatMessageDao()
    private val parentCommunityDao = database.parentCommunityDao()

    val allLessons: Flow<List<LessonItem>> = lessonDao.getAllLessons()
    val allQuestions: Flow<List<QuizQuestion>> = quizDao.getAllQuestions()
    val allBadges: Flow<List<BadgeItem>> = badgeDao.getAllBadges()
    val chatHistory: Flow<List<ChatMessageEntity>> = chatMessageDao.getAllMessages()
    val parentPosts: Flow<List<ParentCommunityPost>> = parentCommunityDao.getAllPosts()
    val allDailyProgress: Flow<List<DailyProgress>> = progressDao.getAllProgress()

    fun getLessonsBySubject(subject: SubjectCategory): Flow<List<LessonItem>> =
        lessonDao.getLessonsBySubject(subject)

    fun getQuestionsBySubject(subject: SubjectCategory): Flow<List<QuizQuestion>> =
        quizDao.getQuestionsBySubject(subject)

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun observeTodayProgress(): Flow<DailyProgress?> {
        return progressDao.observeProgressForDate(getTodayDateString())
    }

    suspend fun recordQuizAnswer(
        subject: SubjectCategory,
        isCorrect: Boolean,
        starsEarned: Int,
        minutesSpent: Int = 1
    ) {
        val today = getTodayDateString()
        val current = progressDao.getProgressForDate(today) ?: DailyProgress(dateString = today)

        val updated = current.copy(
            questionsAttempted = current.questionsAttempted + 1,
            questionsCorrect = current.questionsCorrect + if (isCorrect) 1 else 0,
            minutesStudied = current.minutesStudied + minutesSpent,
            starsEarned = current.starsEarned + starsEarned,
            mathCorrect = current.mathCorrect + if (isCorrect && subject == SubjectCategory.MATH) 1 else 0,
            vietnameseCorrect = current.vietnameseCorrect + if (isCorrect && subject == SubjectCategory.VIETNAMESE) 1 else 0,
            scienceCorrect = current.scienceCorrect + if (isCorrect && subject == SubjectCategory.SCIENCE) 1 else 0,
            brainCorrect = current.brainCorrect + if (isCorrect && subject == SubjectCategory.BRAIN_TEASER) 1 else 0
        )
        progressDao.insertOrUpdateProgress(updated)

        // Check badge unlocks
        checkBadgeUnlocks(updated.starsEarned)
    }

    private suspend fun checkBadgeUnlocks(totalStars: Int) {
        // Simple badge check logic
    }

    suspend fun completeLesson(lesson: LessonItem) {
        lessonDao.updateLesson(lesson.copy(isCompleted = true))
        recordQuizAnswer(lesson.subject, isCorrect = true, starsEarned = lesson.starsReward, minutesSpent = 2)
    }

    suspend fun sendChatMessage(userText: String): ChatMessageEntity {
        // 1. Save user message
        val userEntity = ChatMessageEntity(
            sender = "user",
            text = userText,
            timestamp = System.currentTimeMillis()
        )
        chatMessageDao.insertMessage(userEntity)

        // 2. Fetch answer from Gemini Client
        val tutorReply = GeminiClient.askTutor(userText)
        val replyText = tutorReply.getOrElse { "Cô Cú đang lắng nghe bé nè, bé nói lại cho cô nghe nhé!" }

        // 3. Save tutor reply
        val tutorEntity = ChatMessageEntity(
            sender = "tutor",
            text = replyText,
            timestamp = System.currentTimeMillis()
        )
        chatMessageDao.insertMessage(tutorEntity)
        return tutorEntity
    }

    suspend fun clearChat() {
        chatMessageDao.clearHistory()
        chatMessageDao.insertMessage(
            ChatMessageEntity(
                sender = "tutor",
                text = "Chào mừng bé yêu quay lại học cùng Cô Cú! Bé muốn cô giúp bài toán nào hôm nay?",
                visualAid = "🦉 Xin chào bé yêu!"
            )
        )
    }

    suspend fun toggleLikePost(post: ParentCommunityPost) {
        val updated = post.copy(
            likesCount = post.likesCount + 1,
            isSaved = !post.isSaved
        )
        parentCommunityDao.updatePost(updated)
    }

    suspend fun addParentPost(post: ParentCommunityPost) {
        parentCommunityDao.insertPost(post)
    }
}
