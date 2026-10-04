package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons ORDER BY id ASC")
    fun getAllLessons(): Flow<List<LessonItem>>

    @Query("SELECT * FROM lessons WHERE subject = :subject ORDER BY id ASC")
    fun getLessonsBySubject(subject: SubjectCategory): Flow<List<LessonItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonItem>)

    @Update
    suspend fun updateLesson(lesson: LessonItem)
}

@Dao
interface QuizDao {
    @Query("SELECT * FROM quiz_questions ORDER BY difficulty ASC")
    fun getAllQuestions(): Flow<List<QuizQuestion>>

    @Query("SELECT * FROM quiz_questions WHERE subject = :subject")
    fun getQuestionsBySubject(subject: SubjectCategory): Flow<List<QuizQuestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestion>)
}

@Dao
interface ProgressDao {
    @Query("SELECT * FROM daily_progress ORDER BY dateString DESC")
    fun getAllProgress(): Flow<List<DailyProgress>>

    @Query("SELECT * FROM daily_progress WHERE dateString = :date LIMIT 1")
    suspend fun getProgressForDate(date: String): DailyProgress?

    @Query("SELECT * FROM daily_progress WHERE dateString = :date LIMIT 1")
    fun observeProgressForDate(date: String): Flow<DailyProgress?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: DailyProgress)
}

@Dao
interface BadgeDao {
    @Query("SELECT * FROM badges ORDER BY requiredStars ASC")
    fun getAllBadges(): Flow<List<BadgeItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBadges(badges: List<BadgeItem>)

    @Update
    suspend fun updateBadge(badge: BadgeItem)
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearHistory()
}

@Dao
interface ParentCommunityDao {
    @Query("SELECT * FROM parent_posts ORDER BY likesCount DESC")
    fun getAllPosts(): Flow<List<ParentCommunityPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<ParentCommunityPost>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: ParentCommunityPost)

    @Update
    suspend fun updatePost(post: ParentCommunityPost)
}
