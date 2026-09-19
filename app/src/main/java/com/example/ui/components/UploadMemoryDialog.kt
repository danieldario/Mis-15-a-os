package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.QuinceBlush
import com.example.ui.theme.QuinceGold
import com.example.ui.theme.QuinceGoldDark
import com.example.ui.theme.QuinceGoldLight
import com.example.ui.theme.QuinceRose
import com.example.ui.theme.QuinceRoseDark
import com.example.ui.theme.QuinceTextPrimary
import com.example.ui.theme.QuinceTextSecondary
import com.example.ui.viewmodel.UploadFormState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UploadMemoryDialog(
    formState: UploadFormState,
    onImageSelected: (Uri) -> Unit,
    onPresetSelected: (String) -> Unit,
    onGuestNameChanged: (String) -> Unit,
    onTableNumberChanged: (String) -> Unit,
    onDedicationChanged: (String) -> Unit,
    onTagChanged: (String) -> Unit,
    onQuickSnippetSelected: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onImageSelected(uri)
        }
    }

    val tags = listOf("Momentos", "Vals", "Fiesta", "Cena", "Dedicatoria")
    val quickDedicationSnippets = listOf(
        "👑 ¡Que brilles como la reina que eres!",
        "✨ ¡Felices 15 hermosa!",
        "🥂 ¡Por una noche inolvidable!",
        "💃 ¡A romper la pista de baile!",
        "💖 ¡Te queremos muchísimo!"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .testTag("upload_memory_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = QuinceRose.copy(alpha = 0.15f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = null,
                                    tint = QuinceRose,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Subir Recuerdo y Dedicatoria",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "Para la cumpleañera Luchy ✨",
                                style = MaterialTheme.typography.bodySmall.copy(color = QuinceRose)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_upload_dialog_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Image Selection Area
                if (formState.imageUri != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 10f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(QuinceBlush)
                    ) {
                        MemoryImage(
                            imageUri = formState.imageUri,
                            contentDescription = "Foto seleccionada",
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Badge of optimized image
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(8.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.6f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = QuinceGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Foto optimizada",
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Change photo button
                        IconButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Collections,
                                contentDescription = "Cambiar foto",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                } else {
                    // Empty Photo Picker card with preset suggestions
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        color = QuinceBlush,
                        border = BorderStroke(
                            1.5.dp,
                            Brush.linearGradient(listOf(QuinceRose, QuinceGold))
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = QuinceRose,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Toca para elegir una foto de tu celular",
                                fontWeight = FontWeight.SemiBold,
                                color = QuinceTextPrimary,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Optimización automática de tamaño incluida",
                                fontSize = 11.sp,
                                color = QuinceTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Instant Quick presets for easy testing in browser emulator!
                    Text(
                        text = "O prueba con una foto festiva de muestra:",
                        fontSize = 12.sp,
                        color = QuinceTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onPresetSelected("quince_vals_moment") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("preset_vals_button"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
                        ) {
                            Text("👑 Vals", fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = { onPresetSelected("quince_cake_festive") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("preset_cake_button"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
                        ) {
                            Text("🎂 Torta", fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = { onPresetSelected("quince_hero_banner") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("preset_banner_button"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
                        ) {
                            Text("✨ Gala", fontSize = 11.sp)
                        }
                    }
                }

                if (formState.isCompressing) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = QuinceRose,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Optimizando y comprimiendo imagen...",
                            fontSize = 12.sp,
                            color = QuinceRose
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Guest Name & Table
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = formState.guestName,
                        onValueChange = onGuestNameChanged,
                        label = { Text("Tu Nombre / Familia") },
                        placeholder = { Text("Ej: Andrea Gómez") },
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("guest_name_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = QuinceRose,
                            focusedLabelColor = QuinceRose
                        )
                    )

                    OutlinedTextField(
                        value = formState.tableNumber,
                        onValueChange = onTableNumberChanged,
                        label = { Text("Mesa") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("guest_table_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = QuinceRose,
                            focusedLabelColor = QuinceRose
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Dedication input
                OutlinedTextField(
                    value = formState.dedication,
                    onValueChange = onDedicationChanged,
                    label = { Text("Dedicatoria para Luchy") },
                    placeholder = { Text("Escribe tus felicitaciones y buenos deseos...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("dedication_text_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = QuinceRose,
                        focusedLabelColor = QuinceRose
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick dedication suggestion chips
                Text(
                    text = "Ideas rápidas para tu dedicatoria:",
                    fontSize = 11.sp,
                    color = QuinceTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickDedicationSnippets.forEach { snippet ->
                        Surface(
                            modifier = Modifier.clickable { onQuickSnippetSelected(snippet) },
                            shape = RoundedCornerShape(12.dp),
                            color = QuinceGoldLight,
                            border = BorderStroke(0.5.dp, QuinceGold.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = snippet,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                color = QuinceGoldDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tag Selector
                Text(
                    text = "Categoría del recuerdo:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    tags.forEach { tag ->
                        val isSelected = formState.tag == tag
                        FilterChip(
                            selected = isSelected,
                            onClick = { onTagChanged(tag) },
                            label = { Text(tag, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuinceRose,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Error Message if any
                if (formState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button
                Button(
                    onClick = onSubmit,
                    enabled = !formState.isSubmitting && !formState.isCompressing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_memory_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = QuinceRose)
                ) {
                    if (formState.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = QuinceGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Publicar en el Muro en Vivo",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
