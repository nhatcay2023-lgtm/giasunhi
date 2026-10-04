package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun GamesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedGame by viewModel.selectedGameIndex.collectAsState()
    val gameScore by viewModel.gameScore.collectAsState()
    val feedback by viewModel.gameFeedback.collectAsState()
    val appleQuestion by viewModel.currentAppleQuestion.collectAsState()
    val rhymeQuestion by viewModel.currentRhymeQuestion.collectAsState()
    val compareQuestion by viewModel.currentCompareQuestion.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftKidBackground)
    ) {
        // 1. Top Game Header with Score
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
                            text = "Khu Vui Chơi Trí Tuệ 🎮",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "Vừa chơi vừa học - Nhận thưởng sao vàng!",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(AmberContainer, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("⭐ Điểm: $gameScore", color = AmberOnContainer, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Game Tab Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TabGameButton(
                        title = "🍎 Hái Táo",
                        isSelected = selectedGame == 0,
                        onClick = { viewModel.selectGame(0) },
                        modifier = Modifier.weight(1f)
                    )
                    TabGameButton(
                        title = "🔤 Ghép Vần",
                        isSelected = selectedGame == 1,
                        onClick = { viewModel.selectGame(1) },
                        modifier = Modifier.weight(1f)
                    )
                    TabGameButton(
                        title = "🐊 Cá Sấu",
                        isSelected = selectedGame == 2,
                        onClick = { viewModel.selectGame(2) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Feedback Banner
        feedback?.let { msg ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (msg.contains("Chính xác") || msg.contains("Xuất sắc") || msg.contains("Đúng rồi"))
                        EmeraldContainer else Color(0xFFFFE4E6)
                )
            ) {
                Text(
                    text = msg,
                    modifier = Modifier.padding(14.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (msg.contains("Chính xác") || msg.contains("Xuất sắc") || msg.contains("Đúng rồi"))
                        EmeraldOnContainer else CoralOnContainer,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // 3. Active Game Screen Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            when (selectedGame) {
                0 -> AppleHarvestGame(
                    question = appleQuestion,
                    onPickApple = { viewModel.answerAppleGame(it) }
                )
                1 -> RhymeBuilderGame(
                    question = rhymeQuestion,
                    onPickRhyme = { viewModel.answerRhymeGame(it) }
                )
                2 -> CrocodileCompareGame(
                    question = compareQuestion,
                    onChooseSymbol = { viewModel.answerCompareGame(it) }
                )
            }
        }
    }
}

@Composable
private fun TabGameButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) SkyBluePrimary else Color(0xFFF1F5F9),
            contentColor = if (isSelected) Color.White else TextDark
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(title, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
    }
}

// GAME 1: Apple Math Harvest
@Composable
private fun AppleHarvestGame(
    question: com.example.ui.viewmodel.AppleGameQuestion,
    onPickApple: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("apple_game_container"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🌳 Vườn Táo Tính Nhẩm", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text("Bé hãy chạm vào quả táo có kết quả đúng để hái vào giỏ nhé!", fontSize = 13.sp, color = TextSecondary, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(20.dp))

            // Chalkboard math problem
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val sign = if (question.isPlus) "+" else "-"
                    Text(
                        text = "${question.num1} $sign ${question.num2} = ?",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFACC15)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "🍎".repeat(question.num1) + (if (question.isPlus) "  +  " else "  -  ") + "🍎".repeat(question.num2),
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Apples to pick
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                question.apples.forEach { answerValue ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onPickApple(answerValue) }
                            .testTag("apple_choice_$answerValue")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(Color(0xFFFB7185), Color(0xFFE11D48), Color(0xFF9F1239))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = answerValue.toString(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("🍎 Hái", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("🧺 Giỏ đựng táo của bé", fontSize = 14.sp, color = TextSecondary)
        }
    }
}

// GAME 2: Word Rhyme Builder
@Composable
private fun RhymeBuilderGame(
    question: com.example.ui.viewmodel.RhymeGameQuestion,
    onPickRhyme: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("rhyme_game_container"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🔤 Ghép Vần Kỳ Thú", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text("Chọn vần thích hợp để hoàn thành từ tiếng Việt có nghĩa!", fontSize = 13.sp, color = TextSecondary, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(20.dp))

            // Flashcard
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = question.promptWord,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = SkyBluePrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "💡 Gợi ý: ${question.hintMeaning}",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Bubble choices
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                question.choices.forEach { rhyme ->
                    Button(
                        onClick = { onPickRhyme(rhyme) },
                        modifier = Modifier
                            .size(68.dp)
                            .testTag("rhyme_choice_$rhyme"),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldFun),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = rhyme,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// GAME 3: Crocodile Number Compare (< = >)
@Composable
private fun CrocodileCompareGame(
    question: com.example.ui.viewmodel.CompareGameQuestion,
    onChooseSymbol: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("compare_game_container"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🐊 Cá Sấu So Sánh Số", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text("Cá sấu đói bụng luôn há miệng to về phía số lớn hơn!", fontSize = 13.sp, color = TextSecondary, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(20.dp))

            // Two groups to compare
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left box
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE4E6)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(question.leftNumber.toString(), fontSize = 34.sp, fontWeight = FontWeight.Bold, color = CoralOnContainer)
                        Text(question.leftEmojis, fontSize = 14.sp, textAlign = TextAlign.Center)
                    }
                }

                Text("❓", fontSize = 28.sp, modifier = Modifier.padding(horizontal = 8.dp))

                // Right box
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(question.rightNumber.toString(), fontSize = 34.sp, fontWeight = FontWeight.Bold, color = EmeraldOnContainer)
                        Text(question.rightEmojis, fontSize = 14.sp, textAlign = TextAlign.Center)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 3 Crocodile symbols
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(">", "=", "<").forEach { symbol ->
                    Button(
                        onClick = { onChooseSymbol(symbol) },
                        modifier = Modifier
                            .size(72.dp)
                            .testTag("compare_button_$symbol"),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                    ) {
                        Text(symbol, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}
