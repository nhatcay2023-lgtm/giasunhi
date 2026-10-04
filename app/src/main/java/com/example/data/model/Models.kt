package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SubjectCategory(val displayName: String, val iconEmoji: String) {
    MATH("Toán Vui", "🔢"),
    VIETNAMESE("Tiếng Việt", "📖"),
    SCIENCE("Tự Nhiên & Xã Hội", "🌱"),
    BRAIN_TEASER("Đố Vui Trí Tuệ", "🧩")
}

@Entity(tableName = "lessons")
data class LessonItem(
    @PrimaryKey val id: String,
    val subject: SubjectCategory,
    val title: String,
    val description: String,
    val content: String,
    val visualAidText: String, // e.g. "🍎 🍎 + 🍎 🍎 🍎 = 5 quả táo"
    val sampleProblem: String,
    val stepByStepSolution: String,
    val isCompleted: Boolean = false,
    val starsReward: Int = 3
)

@Entity(tableName = "quiz_questions")
data class QuizQuestion(
    @PrimaryKey val id: String,
    val subject: SubjectCategory,
    val questionText: String,
    val visualAid: String, // e.g. "🥢 🥢 + 🥢 = ?" or "🐥 🐥 🐥"
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String, // Child-friendly explanation
    val difficulty: Int = 1 // 1: dễ, 2: vừa, 3: thử thách
)

@Entity(tableName = "daily_progress")
data class DailyProgress(
    @PrimaryKey val dateString: String, // "YYYY-MM-DD"
    val questionsAttempted: Int = 0,
    val questionsCorrect: Int = 0,
    val minutesStudied: Int = 0,
    val starsEarned: Int = 0,
    val mathCorrect: Int = 0,
    val vietnameseCorrect: Int = 0,
    val scienceCorrect: Int = 0,
    val brainCorrect: Int = 0
)

@Entity(tableName = "badges")
data class BadgeItem(
    @PrimaryKey val badgeId: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean = false,
    val unlockedDate: String? = null,
    val requiredStars: Int = 10
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "user" or "tutor"
    val text: String,
    val visualAid: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isSpoken: Boolean = false
)

@Entity(tableName = "parent_posts")
data class ParentCommunityPost(
    @PrimaryKey val id: String,
    val author: String,
    val authorRole: String, // "Mẹ bé Sóc - Hà Nội", "Thầy giáo Tiểu học", "Chuyên gia tâm lý"
    val title: String,
    val content: String,
    val category: String, // "Rèn chữ & Đọc", "Toán đố vui", "Kỷ luật tích cực", "Dinh dưỡng"
    val likesCount: Int = 12,
    val commentsCount: Int = 4,
    val isSaved: Boolean = false,
    val date: String = "Hôm nay"
)

data class MascotAccessory(
    val id: String,
    val name: String,
    val emoji: String,
    val priceStars: Int,
    val isUnlocked: Boolean,
    val isEquipped: Boolean
)

data class PersonalizedPlanItem(
    val dayNumber: Int,
    val dayTitle: String,
    val subject: SubjectCategory,
    val focusTopic: String,
    val reason: String,
    val isDone: Boolean = false
)
