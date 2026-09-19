package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.QuinceBlush
import com.example.ui.theme.QuinceGold
import com.example.ui.theme.QuinceGoldDark
import com.example.ui.theme.QuinceRose
import com.example.ui.theme.QuinceRoseDark
import com.example.ui.theme.QuinceTextPrimary
import com.example.ui.theme.QuinceTextSecondary
import com.example.ui.viewmodel.GuestProfile

@Composable
fun GuestCheckInCard(
    profile: GuestProfile,
    onSaveProfile: (name: String, table: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(!profile.hasCheckedIn) }
    var tempName by remember { mutableStateOf(profile.name) }
    var tempTable by remember { mutableStateOf(profile.tableNumber) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("guest_check_in_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = QuinceBlush),
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(listOf(QuinceGold.copy(alpha = 0.5f), QuinceRose.copy(alpha = 0.4f)))
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (!isEditing && profile.hasCheckedIn) {
                // Compact signed-in badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = QuinceRose,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = profile.name.take(1).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = profile.name,
                                    fontWeight = FontWeight.Bold,
                                    color = QuinceTextPrimary,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = QuinceRose.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = profile.tableNumber,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = QuinceRoseDark
                                    )
                                }
                            }
                            Text(
                                text = "Tus fotos llevarán tu firma • Invitado de Honor",
                                fontSize = 11.sp,
                                color = QuinceTextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = { isEditing = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar mi nombre",
                            tint = QuinceRoseDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else {
                // Quick registration form (non-intrusive)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = QuinceGoldDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "¡Personaliza tus fotos y dedicatorias!",
                        fontWeight = FontWeight.Bold,
                        color = QuinceRoseDark,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ingresa tu nombre y mesa para que la quinceañera sepa quién le escribe:",
                    fontSize = 11.sp,
                    color = QuinceTextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        placeholder = { Text("Tu nombre / Familia", fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1.4f)
                            .testTag("checkin_name_field"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = QuinceRose,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = tempTable,
                        onValueChange = { tempTable = it },
                        placeholder = { Text("Mesa", fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("checkin_table_field"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = QuinceRose,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )

                    Button(
                        onClick = {
                            val finalName = tempName.ifBlank { "Invitado Especial" }
                            val finalTable = tempTable.ifBlank { "Mesa 1" }
                            onSaveProfile(finalName, finalTable)
                            isEditing = false
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = QuinceRose),
                        modifier = Modifier.testTag("save_checkin_button")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar", tint = Color.White)
                    }
                }
            }
        }
    }
}
