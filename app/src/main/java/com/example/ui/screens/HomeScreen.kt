package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubjectCategory
import com.example.ui.components.KidGreetingBanner
import com.example.ui.components.MascotVisual
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenTab

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    stars: Int,
    accessory: String,
    onNavigateTab: (ScreenTab) -> Unit,
    onOpenSubject: (SubjectCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoftKidBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top Greeting Banner with Mascot
        item {
            KidGreetingBanner(
                stars = stars,
                mascotAccessory = accessory,
                onMascotClick = { onNavigateTab(ScreenTab.REWARDS) }
            )
        }

        // 2. Featured AI Voice Tutor Callout Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateTab(ScreenTab.AI_TUTOR) }
                    .testTag("featured_ai_tutor_card"),
                shape = RoundedCornerShape(20.dp),
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
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎙️", fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hỏi Đáp Cùng Cô Cú AI 🦉",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextDark
                        )
                        Text(
                            text = "Giải toán đố chi tiết, trả lời bằng giọng nói thân thiện",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }

                    Button(
                        onClick = { onNavigateTab(ScreenTab.AI_TUTOR) },
                        colors = ButtonDefaults.buttonColors(containerColor = CoralPink),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("start_chat_button")
                    ) {
                        Text("Nói Chuyện", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. Subject Grid Header
        item {
            Text(
                text = "Môn Học Lớp 1 Yêu Thích 📚",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // 4. Subjects Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SubjectHomeCard(
                        title = "Toán Vui Lớp 1",
                        subtitle = "Cộng trừ & Toán đố",
                        emoji = "🔢",
                        badge = "8 bài học",
                        gradientColors = listOf(Color(0xFF3B82F6), Color(0xFF60A5FA)),
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenSubject(SubjectCategory.MATH) }
                    )
                    SubjectHomeCard(
                        title = "Tiếng Việt",
                        subtitle = "Bảng chữ cái & Ghép vần",
                        emoji = "📖",
                        badge = "6 bài học",
                        gradientColors = listOf(Color(0xFF10B981), Color(0xFF34D399)),
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenSubject(SubjectCategory.VIETNAMESE) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SubjectHomeCard(
                        title = "Tự Nhiên & Xã Hội",
                        subtitle = "Khám phá thế giới quanh em",
                        emoji = "🌱",
                        badge = "5 bài học",
                        gradientColors = listOf(Color(0xFFF59E0B), Color(0xFFFBBF24)),
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenSubject(SubjectCategory.SCIENCE) }
                    )
                    SubjectHomeCard(
                        title = "Đố Vui Trí Tuệ",
                        subtitle = "Rèn luyện IQ & suy luận",
                        emoji = "🧩",
                        badge = "10 câu đố",
                        gradientColors = listOf(Color(0xFF8B5CF6), Color(0xFFA78BFA)),
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenSubject(SubjectCategory.BRAIN_TEASER) }
                    )
                }
            }
        }

        // 5. Daily Quests (Thử thách ngày)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_quest_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Nhiệm Vụ Hôm Nay ⭐",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextDark
                        )
                        Text(
                            text = "Nhận +15 ⭐",
                            color = GoldenAmber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    QuestItem(title = "Nói chuyện với Cô Cú AI 🦉", reward = "+2 ⭐", isDone = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    QuestItem(title = "Hoàn thành 1 bài học Toán có lời văn 🍎", reward = "+5 ⭐", isDone = false)
                    Spacer(modifier = Modifier.height(8.dp))
                    QuestItem(title = "Chơi trò chơi Thu hoạch táo 🍎", reward = "+8 ⭐", isDone = false)
                }
            }
        }

        // 6. Quick Game Launch Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateTab(ScreenTab.GAMES) }
                    .testTag("quick_game_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFEF3C7)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎮", fontSize = 36.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Khu Vui Chơi Giáo Dục!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = AmberOnContainer
                        )
                        Text(
                            text = "Thu hoạch táo tính nhẩm, ghép vần thần tốc và săn sao vàng!",
                            fontSize = 13.sp,
                            color = AmberOnContainer.copy(alpha = 0.8f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Chơi ngay",
                        tint = AmberOnContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun SubjectHomeCard(
    title: String,
    subtitle: String,
    emoji: String,
    badge: String,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(135.dp)
            .clickable { onClick() }
            .testTag("subject_card_${title}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradientColors))
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(emoji, fontSize = 28.sp)
                    Box(
                        modifier = Modifier
                            .background(Color(0x33FFFFFF), RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(badge, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Column {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestItem(
    title: String,
    reward: String,
    isDone: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDone) Color(0xFFF1F5F9) else Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = if (isDone) "✅" else "⚪",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                color = if (isDone) TextSecondary else TextDark,
                fontWeight = if (isDone) FontWeight.Normal else FontWeight.Medium
            )
        }
        Text(
            text = reward,
            color = GoldenAmber,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}
