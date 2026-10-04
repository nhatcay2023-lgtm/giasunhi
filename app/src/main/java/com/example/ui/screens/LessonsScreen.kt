package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.LessonItem
import com.example.data.model.SubjectCategory
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun LessonsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val allLessons by viewModel.allLessons.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val activeLesson by viewModel.activeLessonDetail.collectAsState()

    val filteredLessons = remember(allLessons, selectedSubject) {
        if (selectedSubject == null) allLessons
        else allLessons.filter { it.subject == selectedSubject }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftKidBackground)
    ) {
        // 1. Top bar with Offline Mode Badge
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Kho Bài Học Lớp 1 📚",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "Học mọi lúc mọi nơi - Hoạt động ngoại tuyến 100%",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(Color(0xFFDCFCE7), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("⚡ Ngoại Tuyến", color = EmeraldOnContainer, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Subject filter tabs
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedSubject == null,
                            onClick = { viewModel.setSelectedSubject(null) },
                            label = { Text("Tất Cả", fontSize = 13.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SkyBluePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }

                    items(SubjectCategory.values()) { subject ->
                        FilterChip(
                            selected = selectedSubject == subject,
                            onClick = { viewModel.setSelectedSubject(subject) },
                            label = { Text("${subject.iconEmoji} ${subject.displayName}", fontSize = 13.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SkyBluePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // 2. Lesson items list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredLessons, key = { it.id }) { lesson ->
                LessonCardItem(
                    lesson = lesson,
                    onClick = { viewModel.openLessonDetail(lesson) }
                )
            }
        }
    }

    // Lesson Detail Dialog / Full screen study view
    activeLesson?.let { lesson ->
        LessonDetailDialog(
            lesson = lesson,
            onDismiss = { viewModel.openLessonDetail(null) },
            onReadAloud = {
                viewModel.voiceManager.speak("${lesson.title}. ${lesson.content}. Bài toán: ${lesson.sampleProblem}. Lời giải: ${lesson.stepByStepSolution}")
            },
            onComplete = {
                viewModel.completeCurrentLesson(lesson)
            }
        )
    }
}

@Composable
private fun LessonCardItem(
    lesson: LessonItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("lesson_card_${lesson.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        when (lesson.subject) {
                            SubjectCategory.MATH -> SkyBlueContainer
                            SubjectCategory.VIETNAMESE -> EmeraldContainer
                            SubjectCategory.SCIENCE -> AmberContainer
                            SubjectCategory.BRAIN_TEASER -> PurpleContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(lesson.subject.iconEmoji, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lesson.description,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "+${lesson.starsReward} ⭐",
                        color = GoldenAmber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (lesson.isCompleted) {
                        Text(
                            text = "Đã hoàn thành",
                            color = EmeraldFun,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Icon(
                imageVector = if (lesson.isCompleted) Icons.Default.CheckCircle else Icons.Default.Star,
                contentDescription = null,
                tint = if (lesson.isCompleted) EmeraldFun else GoldenAmber
            )
        }
    }
}

@Composable
private fun LessonDetailDialog(
    lesson: LessonItem,
    onDismiss: () -> Unit,
    onReadAloud: () -> Unit,
    onComplete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .testTag("lesson_detail_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(lesson.subject.iconEmoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = lesson.subject.displayName,
                            color = SkyBluePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Đóng")
                    }
                }

                Text(
                    text = lesson.title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Content scrollable
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Visual Aid Box
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("💡 Hình Minh Họa Trực Quan", fontWeight = FontWeight.Bold, color = AmberOnContainer, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = lesson.visualAidText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }
                    }

                    // Main Explanation
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftKidBackground)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("📖 Lời Cô Cú Giảng", fontWeight = FontWeight.Bold, color = TextDark, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = lesson.content,
                                fontSize = 14.sp,
                                color = TextSecondary,
                                lineHeight = 21.sp
                            )
                        }
                    }

                    // Sample Problem (Toán đố có lời văn)
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("❓ Bài Toán Đố Vui", fontWeight = FontWeight.Bold, color = SkyBluePrimary, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = lesson.sampleProblem,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextDark
                            )
                        }
                    }

                    // Step by step breakdown
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("✨ Các Bước Giải Siêu Dễ", fontWeight = FontWeight.Bold, color = EmeraldFun, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = lesson.stepByStepSolution,
                                fontSize = 14.sp,
                                color = TextDark,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onReadAloud,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cô Cú Đọc", fontSize = 13.sp)
                    }

                    Button(
                        onClick = onComplete,
                        modifier = Modifier.weight(1.3f).testTag("complete_lesson_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldFun)
                    ) {
                        Text("Đã Hiểu (+5 ⭐)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
