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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BadgeItem
import com.example.ui.components.MascotVisual
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

data class AccessoryOption(
    val id: String,
    val name: String,
    val emoji: String,
    val requiredStars: Int
)

@Composable
fun RewardsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val totalStars by viewModel.totalStars.collectAsState()
    val equippedAccessory by viewModel.equippedAccessory.collectAsState()
    val allBadges by viewModel.allBadges.collectAsState()

    val accessories = listOf(
        AccessoryOption("cap", "Mũ Cử Nhân", "🎓", 0),
        AccessoryOption("glasses", "Kính Bác Học", "👓", 10),
        AccessoryOption("cape", "Khăn Siêu Nhân", "🦸", 20),
        AccessoryOption("star", "Sao Phép Thuật", "⭐", 30),
        AccessoryOption("none", "Tự Nhiên", "🦉", 0)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoftKidBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Star Balance & Mascot Room
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mascot_dressing_room"),
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
                    Text(
                        text = "Tủ Đồ Của Cú Mèo 🦉",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "Dùng ngôi sao bé tích lũy được để diện đồ thật ngầu!",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Large Interactive Mascot Display
                    MascotVisual(
                        size = 110.dp,
                        accessory = equippedAccessory,
                        isSpeaking = false
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(AmberContainer, RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("⭐ $totalStars Ngôi Sao Vàng", color = AmberOnContainer, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Accessory Selector
                    Text("Chọn phụ kiện cho Cú Mèo:", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextDark)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(accessories) { acc ->
                            val isEquipped = equippedAccessory == acc.id
                            val isUnlocked = totalStars >= acc.requiredStars

                            Card(
                                modifier = Modifier
                                    .clickable(enabled = isUnlocked) {
                                        viewModel.equipAccessory(acc.id)
                                    }
                                    .testTag("acc_item_${acc.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isEquipped) SkyBlueContainer else if (isUnlocked) Color(0xFFF8FAFC) else Color(0xFFF1F5F9)
                                ),
                                border = if (isEquipped) CardDefaults.outlinedCardBorder() else null
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(acc.emoji, fontSize = 24.sp)
                                    Text(acc.name, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    if (isEquipped) {
                                        Text("Đang mặc", fontSize = 10.sp, color = SkyBluePrimary, fontWeight = FontWeight.Bold)
                                    } else if (!isUnlocked) {
                                        Text("Cần ${acc.requiredStars} ⭐", fontSize = 10.sp, color = TextSecondary)
                                    } else {
                                        Text("Mặc thử", fontSize = 10.sp, color = EmeraldFun)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Honors Badges Header
        item {
            Text(
                text = "Tủ Huy Hiệu Danh Dự 🏆",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // 3. Badges List
        items(allBadges, key = { it.badgeId }) { badge ->
            val isUnlocked = badge.isUnlocked || totalStars >= badge.requiredStars

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("badge_item_${badge.badgeId}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUnlocked) Color.White else Color(0xFFF1F5F9)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 2.dp else 0.dp)
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
                            .background(if (isUnlocked) Color(0xFFFEF3C7) else Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isUnlocked) badge.iconEmoji else "🔒",
                            fontSize = 26.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = badge.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (isUnlocked) TextDark else TextSecondary
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = badge.description,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (isUnlocked) {
                            Text(
                                text = "✨ Đã đạt được",
                                color = EmeraldFun,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        } else {
                            Text(
                                text = "Yêu cầu: Tích lũy ${badge.requiredStars} ⭐",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
