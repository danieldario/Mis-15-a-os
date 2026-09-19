package com.example.ui.components

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.OpenInBrowser
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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
import com.example.util.QrCodeGenerator

@Composable
fun TableQrCardDialog(
    selectedTable: String,
    sharedUrl: String,
    onSelectTable: (String) -> Unit,
    onJoinAsGuestOfTable: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Generate genuine ZXing QR bitmap pointing to the party link
    val qrBitmap = remember(sharedUrl) {
        QrCodeGenerator.generateQrBitmap(
            content = sharedUrl,
            size = 512,
            foregroundColor = QuinceRoseDark.toArgb(),
            backgroundColor = android.graphics.Color.WHITE
        )
    }

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
                            text = "Código QR de la Fiesta",
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
                    text = "Muestra o imprime este tarjetón en la fiesta para que los invitados escaneen el QR y accedan al instante al espacio de recuerdos:",
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

                        Spacer(modifier = Modifier.height(14.dp))

                        // High Quality ZXing Scannable QR Code
                        Box(
                            modifier = Modifier
                                .size(190.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (qrBitmap != null) {
                                Image(
                                    bitmap = qrBitmap.asImageBitmap(),
                                    contentDescription = "Código QR para escanear y acceder a la fiesta de Luchy",
                                    modifier = Modifier.size(170.dp)
                                )
                            }

                            // Center Emblem Badge overlay
                            Surface(
                                shape = CircleShape,
                                color = QuinceRose,
                                border = BorderStroke(2.dp, QuinceGold),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "15",
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // URL pill indicator
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.8f),
                            border = BorderStroke(1.dp, QuinceGold.copy(alpha = 0.4f)),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = sharedUrl,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                color = QuinceRoseDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

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
                            text = "1. Apunta la cámara de tu celular al código\n2. Sube tus fotos de la fiesta en vivo\n3. Participa votando por tus fotos favoritas",
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = QuinceTextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action buttons: Share link & Quick join
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Share Link Button
                    OutlinedButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "👑 Mis 15 Luchy - Muro de Recuerdos en Vivo"
                                )
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "¡Hola! Entra al enlace para compartir tus fotos y dedicatorias de los 15 de Luchy: $sharedUrl"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Compartir enlace de la fiesta"))
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("share_party_link_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = QuinceRose, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Compartir", color = QuinceRose, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    // Direct Join button
                    Button(
                        onClick = {
                            onJoinAsGuestOfTable(selectedTable)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp)
                            .testTag("simulate_qr_scan_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = QuinceRose)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = QuinceGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Entrar a la App", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
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
