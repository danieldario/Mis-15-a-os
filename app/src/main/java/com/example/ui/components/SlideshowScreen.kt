package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.MemoryEntity
import com.example.ui.theme.QuinceDarkBackground
import com.example.ui.theme.QuinceDarkSurface
import com.example.ui.theme.QuinceGold
import com.example.ui.theme.QuinceGoldLight
import com.example.ui.theme.QuinceRose
import com.example.ui.theme.QuinceRoseLight

@Composable
fun SlideshowScreen(
    memories: List<MemoryEntity>,
    currentIndex: Int,
    isPlaying: Boolean,
    intervalSec: Int,
    onTogglePlay: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onIntervalChange: (Int) -> Unit,
    onClose: () -> Unit
) {
    if (memories.isEmpty()) return

    val safeIndex = currentIndex.coerceIn(0, memories.size - 1)
    val currentMemory = memories[safeIndex]

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(QuinceDarkBackground)
                .testTag("slideshow_screen")
        ) {
            // Top Bar: Event title & Close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = QuinceRose.copy(alpha = 0.3f),
                        border = BorderStroke(1.dp, QuinceGold)
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "EN VIVO",
                                color = QuinceGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "✨ Mis 15 Luchy • Proyección en Salón",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "${safeIndex + 1} de ${memories.size} recuerdos",
                            color = QuinceGoldLight.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.15f), CircleShape)
                        .testTag("close_slideshow_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Salir de pantalla completa",
                        tint = Color.White
                    )
                }
            }

            // Main Animated Content (Photo + Floating Dedication)
            AnimatedContent(
                targetState = currentMemory,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500)) togetherWith
                            fadeOut(animationSpec = tween(400))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 80.dp),
                contentAlignment = Alignment.Center,
                label = "slideshow_transition"
            ) { memory ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Large Framed Photo
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(
                            2.dp,
                            Brush.linearGradient(listOf(QuinceGold, QuinceRose, QuinceGoldLight))
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 11f)
                        ) {
                            MemoryImage(
                                imageUri = memory.imageUri,
                                contentDescription = "Recuerdo de ${memory.guestName}",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Floating Glass Dedication Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = QuinceDarkSurface.copy(alpha = 0.92f),
                        border = BorderStroke(1.dp, QuinceGold.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = memory.guestName,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = QuinceGold.copy(alpha = 0.2f),
                                    border = BorderStroke(0.5.dp, QuinceGold)
                                ) {
                                    Text(
                                        text = memory.tableNumber,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        color = QuinceGoldLight,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            if (memory.dedication.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FormatQuote,
                                        contentDescription = null,
                                        tint = QuinceGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = memory.dedication,
                                        color = QuinceRoseLight,
                                        fontStyle = FontStyle.Italic,
                                        fontSize = 15.sp,
                                        lineHeight = 22.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            // Reaction summary
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("💖 ${memory.reactionHearts}", color = Color.White, fontSize = 12.sp)
                                Text("🥂 ${memory.reactionCheers}", color = Color.White, fontSize = 12.sp)
                                Text("✨ ${memory.reactionSparkles}", color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Bottom Controller Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                shape = RoundedCornerShape(28.dp),
                color = QuinceDarkSurface.copy(alpha = 0.85f),
                border = BorderStroke(1.dp, QuinceGold.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Speed selector
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(4, 6, 8).forEach { sec ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (intervalSec == sec) QuinceRose else Color.White.copy(alpha = 0.1f),
                                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                            ) {
                                Text(
                                    text = "${sec}s",
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Playback navigation
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onPrev,
                            modifier = Modifier.testTag("slideshow_prev_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Anterior",
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = onTogglePlay,
                            modifier = Modifier
                                .background(QuinceRose, CircleShape)
                                .testTag("slideshow_play_pause_button")
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = onNext,
                            modifier = Modifier.testTag("slideshow_next_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Siguiente",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
