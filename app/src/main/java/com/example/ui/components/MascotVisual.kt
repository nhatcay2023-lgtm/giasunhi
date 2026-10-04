package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun MascotVisual(
    size: Dp = 100.dp,
    accessory: String = "cap",
    isSpeaking: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Gentle breathing / floating animation
    val infiniteTransition = rememberInfiniteTransition(label = "mascot_anim")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isSpeaking) 8f else 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isSpeaking) 300 else 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSpeaking) 1.06f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isSpeaking) 250 else 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "speaking_scale"
    )

    Box(
        modifier = modifier
            .size(size)
            .offset(y = (-floatOffset).dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFEF3C7),
                        Color(0xFFFDE68A),
                        Color(0xFFF59E0B)
                    )
                )
            )
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .testTag("mascot_avatar"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Accessory on top of head
            when (accessory) {
                "cap" -> Text("🎓", fontSize = (size.value * 0.28).sp)
                "glasses" -> Text("👓", fontSize = (size.value * 0.25).sp)
                "cape" -> Text("🦸", fontSize = (size.value * 0.25).sp)
                "star" -> Text("⭐", fontSize = (size.value * 0.25).sp)
                else -> Spacer(modifier = Modifier.height(4.dp))
            }

            // Mascot Owl Face
            Text(
                text = if (isSpeaking) "🦉" else "🦉",
                fontSize = (size.value * 0.42).sp
            )

            // Cute cheer spark
            if (isSpeaking) {
                Text(
                    text = "✨",
                    fontSize = (size.value * 0.16).sp,
                    color = GoldenAmber
                )
            }
        }
    }
}

@Composable
fun KidGreetingBanner(
    stars: Int,
    mascotAccessory: String,
    onMascotClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("kid_greeting_banner"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF3B82F6),
                            Color(0xFF2563EB),
                            Color(0xFF1D4ED8)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Chào Bé Yêu! 🌟",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Hôm nay chúng mình cùng giải bài toán đố và thu hoạch táo nhé!",
                        color = Color(0xFFDBEAFE),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("⭐ $stars Ngôi Sao Vàng", color = Color(0xFFFEF3C7), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("🔥 Chuỗi 3 ngày", color = Color.White, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                MascotVisual(
                    size = 90.dp,
                    accessory = mascotAccessory,
                    isSpeaking = false,
                    onClick = onMascotClick
                )
            }
        }
    }
}
