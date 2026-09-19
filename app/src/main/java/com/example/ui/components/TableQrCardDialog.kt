package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.QuinceBlush
import com.example.ui.theme.QuinceChampagne
import com.example.ui.theme.QuinceGold
import com.example.ui.theme.QuinceGoldDark
import com.example.ui.theme.QuinceGoldLight
import com.example.ui.theme.QuinceRose
import com.example.ui.theme.QuinceRoseDark
import com.example.ui.theme.QuinceTextPrimary
import com.example.ui.theme.QuinceTextSecondary

@Composable
fun TableQrCardDialog(
    selectedTable: String,
    onSelectTable: (String) -> Unit,
    onJoinAsGuestOfTable: (String) -> Unit,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .testTag("table_qr_card_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = null,
                            tint = QuinceRose,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tarjetón de Mesa con QR",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_qr_dialog_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Text(
                    text = "Tarjetón de bienvenida que se coloca para que los invitados escaneen el QR y compartan fotos en vivo:",
                    style = MaterialTheme.typography.bodySmall.copy(color = QuinceTextSecondary),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // The Table Card (Physical card simulation)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    color = QuinceBlush,
                    border = BorderStroke(
                        2.dp,
                        Brush.linearGradient(
                            listOf(QuinceGold, QuinceChampagne, QuinceGoldDark)
                        )
                    ),
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Crown & Title
                        Text(
                            text = "👑 Mis 15 Luchy 👑",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuinceRoseDark,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "24 de Octubre • Noche de Gala",
                            fontSize = 12.sp,
                            color = QuinceGoldDark,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Table Badge
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = QuinceRose,
                            border = BorderStroke(1.dp, QuinceGold)
                        ) {
                            Text(
                                text = selectedTable.uppercase(),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Beautiful stylized Canvas QR Code
                        Box(
                            modifier = Modifier
                                .size(170.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            QrCodeCanvas(tableNumber = selectedTable)

                            // Center Emblem Badge
                            Surface(
                                shape = CircleShape,
                                color = QuinceRose,
                                border = BorderStroke(2.dp, QuinceGold),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "15",
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Instructions
                        Text(
                            text = "¡Comparte tus fotos y dedicatorias!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = QuinceTextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "1. Escanea el código con tu cámara\n2. Sube tus fotos de la fiesta en vivo\n3. Las fotos se proyectarán en pantalla gigante",
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = QuinceTextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action buttons
                Button(
                    onClick = {
                        onJoinAsGuestOfTable(selectedTable)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("simulate_qr_scan_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = QuinceRose)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = QuinceGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ingresar como Invitado", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}

/**
 * Draws a clean procedural QR pattern with position markers and randomized data blocks
 * based on table seed for realistic preview.
 */
@Composable
private fun QrCodeCanvas(tableNumber: String) {
    val darkColor = QuinceRoseDark
    val lightColor = Color.Transparent

    Canvas(modifier = Modifier.size(150.dp)) {
        val count = 21 // 21x21 standard QR grid
        val cellSize = size.width / count

        fun drawSquareFinder(gridX: Int, gridY: Int) {
            // Outer 7x7
            drawRoundRect(
                color = darkColor,
                topLeft = Offset(gridX * cellSize, gridY * cellSize),
                size = Size(7 * cellSize, 7 * cellSize),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            // Inner white 5x5
            drawRoundRect(
                color = Color.White,
                topLeft = Offset((gridX + 1) * cellSize, (gridY + 1) * cellSize),
                size = Size(5 * cellSize, 5 * cellSize),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )
            // Center dark 3x3
            drawRoundRect(
                color = darkColor,
                topLeft = Offset((gridX + 2) * cellSize, (gridY + 2) * cellSize),
                size = Size(3 * cellSize, 3 * cellSize),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )
        }

        // 3 finder patterns
        drawSquareFinder(0, 0)
        drawSquareFinder(14, 0)
        drawSquareFinder(0, 14)

        // Seed data based on table string
        val seed = tableNumber.hashCode().toLong()
        val random = java.util.Random(seed)

        for (x in 0 until count) {
            for (y in 0 until count) {
                // Avoid finders and center emblem area
                val inTopLeft = x < 8 && y < 8
                val inTopRight = x > 12 && y < 8
                val inBottomLeft = x < 8 && y > 12
                val inCenter = (x in 8..12) && (y in 8..12)

                if (!inTopLeft && !inTopRight && !inBottomLeft && !inCenter) {
                    val isBlack = random.nextFloat() > 0.48f
                    if (isBlack) {
                        drawRoundRect(
                            color = darkColor,
                            topLeft = Offset(x * cellSize, y * cellSize),
                            size = Size(cellSize * 0.9f, cellSize * 0.9f),
                            cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
                        )
                    }
                }
            }
        }
    }
}
