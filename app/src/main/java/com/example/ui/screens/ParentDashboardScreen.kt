package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ParentCommunityPost
import com.example.data.model.PersonalizedPlanItem
import com.example.data.model.SubjectCategory
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ParentDashboardScreen(
    viewModel: MainViewModel,
    onLockParentZone: () -> Unit,
    modifier: Modifier = Modifier
) {
    var parentSubTab by remember { mutableStateOf(0) } // 0: Reports, 1: Personalized Plan, 2: Reminders, 3: Community
    var showAddPostDialog by remember { mutableStateOf(false) }

    val todayProgress by viewModel.todayProgress.collectAsState()
    val progressHistory by viewModel.allProgressHistory.collectAsState()
    val reminderTime by viewModel.reminderTime.collectAsState()
    val dailyGoal by viewModel.dailyGoalMinutes.collectAsState()
    val parentPosts by viewModel.parentPosts.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftKidBackground)
    ) {
        // 1. Top bar with Lock button
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Bảng Điều Khiển Phụ Huynh 👨‍👩‍👧",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }
                        Text(
                            text = "Giám sát tiến độ & Định hướng phát triển cho con",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    OutlinedButton(
                        onClick = onLockParentZone,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Khóa Lại", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Parent Sub-Tabs
                ScrollableTabRow(
                    selectedTabIndex = parentSubTab,
                    edgePadding = 0.dp,
                    containerColor = Color.Transparent,
                    divider = {}
                ) {
                    Tab(
                        selected = parentSubTab == 0,
                        onClick = { parentSubTab = 0 },
                        text = { Text("📊 Báo Cáo", fontWeight = if (parentSubTab == 0) FontWeight.Bold else FontWeight.Normal) }
                    )
                    Tab(
                        selected = parentSubTab == 1,
                        onClick = { parentSubTab = 1 },
                        text = { Text("🗺️ Lộ Trình AI", fontWeight = if (parentSubTab == 1) FontWeight.Bold else FontWeight.Normal) }
                    )
                    Tab(
                        selected = parentSubTab == 2,
                        onClick = { parentSubTab = 2 },
                        text = { Text("⏰ Nhắc Nhở", fontWeight = if (parentSubTab == 2) FontWeight.Bold else FontWeight.Normal) }
                    )
                    Tab(
                        selected = parentSubTab == 3,
                        onClick = { parentSubTab = 3 },
                        text = { Text("💬 Cộng Đồng", fontWeight = if (parentSubTab == 3) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }
        }

        // Sub-Tab Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (parentSubTab) {
                0 -> ParentReportsView(todayProgress = todayProgress, dailyGoal = dailyGoal)
                1 -> PersonalizedPlanView()
                2 -> ParentSettingsRemindersView(
                    reminderTime = reminderTime,
                    dailyGoal = dailyGoal,
                    onUpdateTime = { viewModel.updateReminderTime(it) },
                    onUpdateGoal = { viewModel.updateDailyGoal(it) }
                )
                3 -> ParentCommunityView(
                    posts = parentPosts,
                    onLikePost = { viewModel.togglePostLike(it) },
                    onOpenNewPost = { showAddPostDialog = true }
                )
            }
        }
    }

    if (showAddPostDialog) {
        NewPostDialog(
            onDismiss = { showAddPostDialog = false },
            onSubmit = { title, content, cat, author ->
                viewModel.addNewParentPost(title, content, cat, author)
                showAddPostDialog = false
            }
        )
    }
}

// VIEW 1: Reports & Analytics
@Composable
private fun ParentReportsView(
    todayProgress: com.example.data.model.DailyProgress?,
    dailyGoal: Int
) {
    val studiedMin = todayProgress?.minutesStudied ?: 8
    val correctCount = todayProgress?.questionsCorrect ?: 12
    val totalCount = todayProgress?.questionsAttempted ?: 15
    val accuracy = if (totalCount > 0) (correctCount * 100 / totalCount) else 80

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary Cards Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricSummaryCard(
                    title = "Thời Gian Học",
                    value = "$studiedMin / $dailyGoal phút",
                    subtitle = "Mục tiêu hàng ngày",
                    emoji = "⏱️",
                    color = SkyBluePrimary,
                    modifier = Modifier.weight(1f)
                )
                MetricSummaryCard(
                    title = "Độ Chính Xác",
                    value = "$accuracy%",
                    subtitle = "$correctCount / $totalCount câu đúng",
                    emoji = "🎯",
                    color = EmeraldFun,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 7-day Bar Chart Visualizer
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Biểu Đồ Thời Gian Học 7 Ngày Qua (Phút)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark)
                    Spacer(modifier = Modifier.height(18.dp))

                    val days = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
                    val minutes = listOf(12, 18, 15, 10, 20, 25, studiedMin)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.zip(minutes).forEach { (day, min) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("$min'", fontSize = 11.sp, color = TextSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .height((min * 3.5).coerceIn(15.0, 95.0).dp)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(if (min >= dailyGoal) EmeraldFun else SkyBluePrimary)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(day, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextDark)
                            }
                        }
                    }
                }
            }
        }

        // Subject Breakdown Accuracy
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Năng Lực Theo Từng Môn Học", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark)
                    Spacer(modifier = Modifier.height(14.dp))

                    SubjectProgressBar(title = "Toán Vui & Tính Nhẩm 🔢", percentage = 88, color = SkyBluePrimary)
                    Spacer(modifier = Modifier.height(10.dp))
                    SubjectProgressBar(title = "Tiếng Việt & Ghép Vần 📖", percentage = 92, color = EmeraldFun)
                    Spacer(modifier = Modifier.height(10.dp))
                    SubjectProgressBar(title = "Tự Nhiên & Xã Hội 🌱", percentage = 85, color = GoldenAmber)
                    Spacer(modifier = Modifier.height(10.dp))
                    SubjectProgressBar(title = "Đố Vui & Tư Duy Logic 🧩", percentage = 80, color = PurpleJoy)
                }
            }
        }

        // Evaluation Summary for Parent
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🦉", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Nhận Xét Của Gia Sư AI", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SkyBluePrimary)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Bé rất hào hứng với môn Tiếng Việt và các bài toán so sánh số! Bé tư duy hình ảnh que tính rất nhanh. Phụ huynh nên tiếp tục khích lệ bé làm thêm 2-3 bài toán đố có lời văn mỗi tối để rèn khả năng đọc hiểu đề bài.",
                        fontSize = 13.sp,
                        color = TextDark,
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricSummaryCard(
    title: String,
    value: String,
    subtitle: String,
    emoji: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 12.sp, color = TextSecondary)
                Text(emoji, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 11.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun SubjectProgressBar(
    title: String,
    percentage: Int,
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 13.sp, color = TextDark)
            Text("$percentage%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = Color(0xFFF1F5F9)
        )
    }
}

// VIEW 2: Personalized AI Learning Plan
@Composable
private fun PersonalizedPlanView() {
    val planItems = listOf(
        PersonalizedPlanItem(1, "Thứ Hai", SubjectCategory.MATH, "Ôn tập phép trừ trong phạm vi 10", "Củng cố dạng toán bớt đi quả táo/kẹo", true),
        PersonalizedPlanItem(2, "Thứ Ba", SubjectCategory.VIETNAMESE, "Luyện phân biệt dấu Hỏi (?) và dấu Ngã (~)", "Giúp bé phát âm chuẩn và viết đúng chính tả", false),
        PersonalizedPlanItem(3, "Thứ Tư", SubjectCategory.MATH, "Toán đố có lời văn với que tính", "Rèn luyện kỹ năng phân tích đề bài", false),
        PersonalizedPlanItem(4, "Thứ Năm", SubjectCategory.SCIENCE, "Năm giác quan và thói quen bảo vệ răng xinh", "Giáo dục kỹ năng sống bổ ích", false),
        PersonalizedPlanItem(5, "Thứ Sáu", SubjectCategory.VIETNAMESE, "Ghép vần có âm cuối: an, at, am, ap", "Mở rộng vốn từ vựng miêu tả", false),
        PersonalizedPlanItem(6, "Thứ Bảy", SubjectCategory.BRAIN_TEASER, "Trò chơi tìm quy luật dãy số (2, 4, 6, 8...)", "Kích thích tư duy logic và suy luận nhạy bén", false),
        PersonalizedPlanItem(7, "Chủ Nhật", SubjectCategory.MATH, "Bài kiểm tra tổng hợp tuần & Nhận Cúp Tuần 🏆", "Đánh giá sự tiến bộ toàn diện của bé", false)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🎯 Lộ Trình Ôn Tập 7 Ngày Cá Nhân Hóa", fontWeight = FontWeight.Bold, color = AmberOnContainer, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "AI tự động phân tích kết quả bài tập của bé để xây dựng lộ trình ôn luyện tối ưu, giúp con nắm chắc kiến thức cơ bản mà không bị áp lực.",
                        fontSize = 13.sp,
                        color = AmberOnContainer.copy(alpha = 0.9f)
                    )
                }
            }
        }

        items(planItems) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (item.isDone) EmeraldContainer else SkyBlueContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (item.isDone) "✅" else "D${item.dayNumber}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(item.dayTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextDark)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("• ${item.subject.displayName}", fontSize = 12.sp, color = SkyBluePrimary)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(item.focusTopic, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextDark)
                        Text("💡 ${item.reason}", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
        }
    }
}

// VIEW 3: Reminders & Safety Settings
@Composable
private fun ParentSettingsRemindersView(
    reminderTime: String,
    dailyGoal: Int,
    onUpdateTime: (String) -> Unit,
    onUpdateGoal: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Daily Study Reminder Setting
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("⏰ Nhắc Nhở Học Tập Hàng Ngày", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark)
                    Text("Thiết lập giờ cố định để bé hình thành thói quen tự giác ngồi vào bàn học.", fontSize = 13.sp, color = TextSecondary)

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Giờ nhắc học:", fontSize = 14.sp, color = TextDark)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("19:00", "19:30", "20:00").forEach { time ->
                                FilterChip(
                                    selected = reminderTime == time,
                                    onClick = { onUpdateTime(time) },
                                    label = { Text(time, fontSize = 13.sp) }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Mục tiêu mỗi ngày:", fontSize = 14.sp, color = TextDark)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(10, 15, 20).forEach { mins ->
                                FilterChip(
                                    selected = dailyGoal == mins,
                                    onClick = { onUpdateGoal(mins) },
                                    label = { Text("$mins phút", fontSize = 13.sp) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Child Safety & No Ads Guarantee Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🛡️", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cam Kết An Toàn Tuyệt Đối Cho Trẻ", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = EmeraldOnContainer)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("• Không chứa bất kỳ quảng cáo độc hại nào.", fontSize = 13.sp, color = TextDark)
                    Text("• Nội dung giáo dục được kiểm duyệt 100% chuẩn lớp 1.", fontSize = 13.sp, color = TextDark)
                    Text("• Hoạt động ngoại tuyến độc lập, bảo vệ dữ liệu cá nhân của gia đình.", fontSize = 13.sp, color = TextDark)
                    Text("• Chặn truy cập web ngoài, bảo vệ bé tối đa.", fontSize = 13.sp, color = TextDark)
                }
            }
        }
    }
}

// VIEW 4: Parent Community Forum
@Composable
private fun ParentCommunityView(
    posts: List<ParentCommunityPost>,
    onLikePost: (ParentCommunityPost) -> Unit,
    onOpenNewPost: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Cộng Đồng Nuôi Dạy Trẻ Lớp 1 💬", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                    Text("Chia sẻ kinh nghiệm rèn con vào lớp 1 bổ ích", fontSize = 12.sp, color = TextSecondary)
                }

                Button(
                    onClick = onOpenNewPost,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                ) {
                    Text("Viết Bài", fontSize = 12.sp)
                }
            }
        }

        items(posts, key = { it.id }) { post ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEDE9FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("👩", fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(post.author, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                                Text(post.authorRole, fontSize = 11.sp, color = TextSecondary)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(post.category, fontSize = 11.sp, color = SkyBluePrimary, fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = post.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = post.content,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(post.date, fontSize = 11.sp, color = Color.Gray)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { onLikePost(post) }) {
                                Icon(
                                    imageVector = if (post.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Thích",
                                    tint = if (post.isSaved) CoralPink else Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text("${post.likesCount}", fontSize = 12.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(imageVector = Icons.Default.Comment, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${post.commentsCount}", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NewPostDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Toán đố vui") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Chia Sẻ Kinh Nghiệm Dạy Trẻ ✍️", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextDark)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Tên phụ huynh (VD: Mẹ bé Bin)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Tiêu đề bài viết") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Nội dung kinh nghiệm chia sẻ") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) { Text("Hủy") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (title.isNotBlank() && content.isNotBlank()) {
                                    onSubmit(title, content, category, author)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                        ) {
                            Text("Đăng Bài")
                        }
                    }
                }
            }
        }
    }
}
