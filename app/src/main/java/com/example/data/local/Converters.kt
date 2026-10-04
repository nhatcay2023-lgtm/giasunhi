package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.SubjectCategory

class Converters {
    @TypeConverter
    fun fromSubjectCategory(value: SubjectCategory): String = value.name

    @TypeConverter
    fun toSubjectCategory(value: String): SubjectCategory = try {
        SubjectCategory.valueOf(value)
    } catch (e: Exception) {
        SubjectCategory.MATH
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String = list.joinToString("|||")

    @TypeConverter
    fun toStringList(data: String): List<String> = if (data.isEmpty()) emptyList() else data.split("|||")
}
