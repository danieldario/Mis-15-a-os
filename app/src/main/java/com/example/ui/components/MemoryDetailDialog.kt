package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.MemoryEntity
import com.example.data.repository.ReactionType
import com.example.ui.theme.QuinceBlush
import com.example.ui.theme.QuinceGold
import com.example.ui.theme.QuinceGoldDark
import com.example.ui.theme.QuinceGoldLight
import com.example.ui.theme.QuinceRose
import com.example.ui.theme.QuinceRoseDark
import com.example.ui.theme.QuinceTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MemoryDetailDialog(
    memory: MemoryEntity,
    hasVoted: Boolean,
    onReact: (ReactionType) -> Unit,
    onToggleVote: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .testTag("memory_detail_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Photo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 12f)
                ) {
                    MemoryImage(
                        imageUri = memory.imageUri,
                        contentDescription = "Foto de recuerdo",
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Close button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .testTag("close_detail_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White
                        )
                    }

                    // Table badge
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color.Black.copy(alpha = 0.65f),
                        border = BorderStroke(1.dp, QuinceGold)
                    ) {
                        Text(
                            text = memory.tableNumber,
                            color = QuinceGoldLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                // Details Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = memory.guestName,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            val dateStr = SimpleDateFormat("EEEE, d 'de' MMMM • HH:mm", Locale("es", "ES"))
                                .format(Date(memory.timestamp))
                            Text(
                                text = dateStr.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodySmall.copy(color = QuinceTextSecondary)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = QuinceRose.copy(alpha = 0.15f),
                            border = BorderStroke(0.5.dp, QuinceRose)
                        ) {
                            Text(
                                text = memory.tag,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                color = QuinceRoseDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Photo Voting Card
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(onClick = onToggleVote)
                            .testTag("detail_toggle_vote_button"),
                        shape = RoundedCornerShape(16.dp),
                        color = if (hasVoted) QuinceGold.copy(alpha = 0.15f) else QuinceBlush,
                        border = BorderStroke(1.dp, if (hasVoted) QuinceGold else QuinceGold.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (hasVoted) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                    contentDescription = "Votar foto favorita",
                                    tint = if (hasVoted) QuinceGoldDark else QuinceGold,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column {
                                    Text(
                                        text = if (hasVoted) "¡Has votado por esta foto!" else "Vota por esta foto",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (hasVoted) QuinceGoldDark else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Cada invitado tiene hasta 3 votos para elegir sus fotos favoritas",
                                        fontSize = 11.sp,
                                        color = QuinceTextSecondary
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (hasVoted) QuinceGold else QuinceGoldLight,
                                border = BorderStroke(1.dp, QuinceGold)
                            ) {
                                Text(
                                    text = "${memory.votes} votos",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasVoted) Color.White else QuinceGoldDark
                                )
                            }
                        }
                    }

                    if (memory.dedication.isNotBlank()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = QuinceBlush,
                            border = BorderStroke(1.dp, QuinceGold.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatQuote,
                                    contentDescription = null,
                                    tint = QuinceGold,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = memory.dedication,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontStyle = FontStyle.Italic,
                                        lineHeight = 24.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Deja tu reacción a este recuerdo:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = QuinceTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Reaction Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ReactionTile(
                            emoji = "💖",
                            label = "Me encanta",
                            count = memory.reactionHearts,
                            onClick = { onReact(ReactionType.HEART) },
                            modifier = Modifier.weight(1f)
                        )
                        ReactionTile(
                            emoji = "🥂",
                            label = "Brindis",
                            count = memory.reactionCheers,
                            onClick = { onReact(ReactionType.CHEERS) },
                            modifier = Modifier.weight(1f)
                        )
                        ReactionTile(
                            emoji = "✨",
                            label = "Mágico",
                            count = memory.reactionSparkles,
                            onClick = { onReact(ReactionType.SPARKLES) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReactionTile(
    emoji: String,
    label: String,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        color = QuinceGoldLight,
        border = BorderStroke(1.dp, QuinceGold.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$count",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = QuinceGoldDark
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = QuinceTextSecondary
            )
        }
    }
}
