package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.ParentGateDialog
import com.example.ui.screens.*
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.AmberOnContainer
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                KidsTutorApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KidsTutorApp(viewModel: MainViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsState()
    val totalStars by viewModel.totalStars.collectAsState()
    val equippedAccessory by viewModel.equippedAccessory.collectAsState()
    val isParentUnlocked by viewModel.isParentUnlocked.collectAsState()

    var showParentGate by remember { mutableStateOf(false) }

    // Handle back button on secondary screens to return to Home
    BackHandler(enabled = currentTab != ScreenTab.HOME) {
        viewModel.setTab(ScreenTab.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = when (currentTab) {
                                ScreenTab.HOME -> "Gia Sư Nhí AI 🦉"
                                ScreenTab.AI_TUTOR -> "Hỏi Đáp Cùng Cô Cú 🎙️"
                                ScreenTab.LESSONS -> "Bài Học Lớp 1 📚"
                                ScreenTab.GAMES -> "Khu Vui Chơi 🎮"
                                ScreenTab.REWARDS -> "Phần Thưởng 🏆"
                                ScreenTab.PARENT_ZONE -> "Góc Phụ Huynh 👨‍👩‍👧"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                },
                actions = {
                    // Star counter chip in top bar
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .background(AmberContainer, RoundedCornerShape(14.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "⭐ $totalStars",
                            color = AmberOnContainer,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_navigation_bar")
            ) {
                ScreenTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (tab == ScreenTab.PARENT_ZONE && !isParentUnlocked) {
                                showParentGate = true
                            } else {
                                viewModel.setTab(tab)
                            }
                        },
                        icon = {
                            Text(tab.iconEmoji, fontSize = 20.sp)
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SkyBluePrimary,
                            indicatorColor = Color(0xFFDBEAFE)
                        ),
                        modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                ScreenTab.HOME -> HomeScreen(
                    viewModel = viewModel,
                    stars = totalStars,
                    accessory = equippedAccessory,
                    onNavigateTab = { targetTab ->
                        if (targetTab == ScreenTab.PARENT_ZONE && !isParentUnlocked) {
                            showParentGate = true
                        } else {
                            viewModel.setTab(targetTab)
                        }
                    },
                    onOpenSubject = { subject ->
                        viewModel.setSelectedSubject(subject)
                        viewModel.setTab(ScreenTab.LESSONS)
                    }
                )

                ScreenTab.AI_TUTOR -> AITutorChatScreen(
                    viewModel = viewModel
                )

                ScreenTab.LESSONS -> LessonsScreen(
                    viewModel = viewModel
                )

                ScreenTab.GAMES -> GamesScreen(
                    viewModel = viewModel
                )

                ScreenTab.REWARDS -> RewardsScreen(
                    viewModel = viewModel
                )

                ScreenTab.PARENT_ZONE -> ParentDashboardScreen(
                    viewModel = viewModel,
                    onLockParentZone = {
                        viewModel.lockParentZone()
                        viewModel.setTab(ScreenTab.HOME)
                    }
                )
            }
        }
    }

    if (showParentGate) {
        ParentGateDialog(
            onDismiss = { showParentGate = false },
            onSuccess = {
                showParentGate = false
                viewModel.unlockParentZone("1234")
                viewModel.setTab(ScreenTab.PARENT_ZONE)
            }
        )
    }
}
