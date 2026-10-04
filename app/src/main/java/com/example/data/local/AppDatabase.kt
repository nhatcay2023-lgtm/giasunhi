package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        LessonItem::class,
        QuizQuestion::class,
        DailyProgress::class,
        BadgeItem::class,
        ChatMessageEntity::class,
        ParentCommunityPost::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun lessonDao(): LessonDao
    abstract fun quizDao(): QuizDao
    abstract fun progressDao(): ProgressDao
    abstract fun badgeDao(): BadgeDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun parentCommunityDao(): ParentCommunityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kids_tutor_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Preload initial lessons, questions, badges, parent posts
                        INSTANCE?.let { database ->
                            scope.launch(Dispatchers.IO) {
                                database.lessonDao().insertLessons(PreloadData.getInitialLessons())
                                database.quizDao().insertQuestions(PreloadData.getInitialQuestions())
                                database.badgeDao().insertBadges(PreloadData.getInitialBadges())
                                database.parentCommunityDao().insertPosts(PreloadData.getInitialParentPosts())
                                // Preload welcome message from AI tutor
                                database.chatMessageDao().insertMessage(
                                    ChatMessageEntity(
                                        sender = "tutor",
                                        text = "Chào bé ngoan! Cô là Cú Mèo Thông Thái 🦉. Bé có bài toán đố nào hay từ nào chưa hiểu không? Hãy bấm vào nút Micro 🎙️ để nói hoặc chọn câu hỏi bên dưới nhé!",
                                        visualAid = "🦉✨ Xin chào bé yêu!"
                                    )
                                )
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
