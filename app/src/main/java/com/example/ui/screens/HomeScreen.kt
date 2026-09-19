package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.GuestCheckInCard
import com.example.ui.components.MemoryCard
import com.example.ui.components.MemoryDetailDialog
import com.example.ui.components.MemoryGridCard
import com.example.ui.components.SlideshowScreen
import com.example.ui.components.TableQrCardDialog
import com.example.ui.components.UploadMemoryDialog
import com.example.ui.theme.QuinceBlush
import com.example.ui.theme.QuinceChampagne
import com.example.ui.theme.QuinceGold
import com.example.ui.theme.QuinceGoldDark
import com.example.ui.theme.QuinceGoldLight
import com.example.ui.theme.QuinceRose
import com.example.ui.theme.QuinceRoseDark
import com.example.ui.theme.QuinceTextSecondary
import com.example.ui.viewmodel.FeedDisplayMode
import com.example.ui.viewmodel.MemoryViewModel
import com.example.ui.viewmodel.SortMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MemoryViewModel,
    modifier: Modifier = Modifier
) {
    val memories by viewModel.memories.collectAsStateWithLifecycle()
    val selectedTag by viewModel.selectedTag.collectAsStateWithLifecycle()
    val sortMode by viewModel.sortMode.collectAsStateWithLifecycle()
    val votedMemoryIds by viewModel.votedMemoryIds.collectAsStateWithLifecycle()
    val displayMode by viewModel.feedDisplayMode.collectAsStateWithLifecycle()
    val guestProfile by viewModel.guestProfile.collectAsStateWithLifecycle()

    val isUploadOpen by viewModel.isUploadDialogOpen.collectAsStateWithLifecycle()
    val uploadState by viewModel.uploadFormState.collectAsStateWithLifecycle()
    val selectedMemoryDetail by viewModel.selectedMemoryDetail.collectAsStateWithLifecycle()

    val isTableQrCardOpen by viewModel.isTableQrCardOpen.collectAsStateWithLifecycle()
    val currentQrTable by viewModel.currentQrTable.collectAsStateWithLifecycle()
    val partySharedUrl by viewModel.partySharedUrl.collectAsStateWithLifecycle()

    val isSlideshowOpen by viewModel.isSlideshowOpen.collectAsStateWithLifecycle()
    val slideshowIndex by viewModel.slideshowIndex.collectAsStateWithLifecycle()
    val slideshowPlaying by viewModel.slideshowIsPlaying.collectAsStateWithLifecycle()
    val slideshowInterval by viewModel.slideshowIntervalSec.collectAsStateWithLifecycle()
    val recentNotificationMessage by viewModel.recentNotificationMessage.collectAsStateWithLifecycle()

    val filterTags = listOf("Todos", "Vals", "Fiesta", "Cena", "Dedicatoria", "Momentos")

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = QuinceRose,
                            border = BorderStroke(1.dp, QuinceGold),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "15",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Mis 15 Luchy",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "Muro de Recuerdos en Vivo",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = QuinceRose,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                },
                actions = {
                    // QR Table Card Button
                    IconButton(
                        onClick = { viewModel.openTableQrCard() },
                        modifier = Modifier.testTag("open_table_qr_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "Ver Tarjetón QR",
                            tint = QuinceRoseDark
                        )
                    }

                    // Projector Slideshow Button
                    IconButton(
                        onClick = { viewModel.openSlideshow() },
                        modifier = Modifier.testTag("open_slideshow_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Slideshow,
                            contentDescription = "Modo Proyección en Vivo",
                            tint = QuinceGoldDark
                        )
                    }

                    // Feed Mode Toggle (Cards vs Grid)
                    IconButton(
                        onClick = { viewModel.toggleFeedDisplayMode() },
                        modifier = Modifier.testTag("toggle_view_mode_button")
                    ) {
                        Icon(
                            imageVector = if (displayMode == com.example.ui.viewmodel.FeedDisplayMode.CARDS)
                                Icons.Default.GridView
                            else
                                Icons.Default.ViewAgenda,
                            contentDescription = "Cambiar vista",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.openUploadDialog() },
                modifier = Modifier.testTag("open_upload_fab"),
                containerColor = QuinceRose,
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = null,
                    tint = QuinceGoldLight
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Subir Recuerdo",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Real-time Notification Banner for uploads and voting status
            recentNotificationMessage?.let { notificationMsg ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { viewModel.dismissNotificationMessage() },
                    shape = RoundedCornerShape(12.dp),
                    color = QuinceRose,
                    border = BorderStroke(1.dp, QuinceGold),
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = QuinceGoldLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = notificationMsg,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "✕",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Header Content List
            if (displayMode == com.example.ui.viewmodel.FeedDisplayMode.CARDS) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("memories_cards_list"),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        HeroCelebrationBanner()
                    }

                    item {
                        GuestCheckInCard(
                            profile = guestProfile,
                            onSaveProfile = { name, table -> viewModel.setGuestProfile(name, table) }
                        )
                    }

                    item {
                        FilterAndSortBar(
                            tags = filterTags,
                            selectedTag = selectedTag,
                            onSelectTag = { viewModel.setSelectedTag(it) },
                            sortMode = sortMode,
                            onToggleSort = { viewModel.toggleSortMode() }
                        )
                    }

                    if (memories.isEmpty()) {
                        item {
                            EmptyMemoriesView(onUploadClick = { viewModel.openUploadDialog() })
                        }
                    } else {
                        items(memories, key = { it.id }) { memory ->
                            MemoryCard(
                                memory = memory,
                                hasVoted = votedMemoryIds.contains(memory.id),
                                onCardClick = { viewModel.openMemoryDetail(memory) },
                                onReact = { reactionType -> viewModel.reactToMemory(memory.id, reactionType) },
                                onToggleVote = { viewModel.toggleVoteForMemory(memory.id) }
                            )
                        }
                    }
                }
            } else {
                // Grid View Mode
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("memories_grid_list"),
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 88.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                        Column {
                            HeroCelebrationBanner()
                            Spacer(modifier = Modifier.height(12.dp))
                            GuestCheckInCard(
                                profile = guestProfile,
                                onSaveProfile = { name, table -> viewModel.setGuestProfile(name, table) }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            FilterAndSortBar(
                                tags = filterTags,
                                selectedTag = selectedTag,
                                onSelectTag = { viewModel.setSelectedTag(it) },
                                sortMode = sortMode,
                                onToggleSort = { viewModel.toggleSortMode() }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    if (memories.isEmpty()) {
                        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                            EmptyMemoriesView(onUploadClick = { viewModel.openUploadDialog() })
                        }
                    } else {
                        items(memories, key = { it.id }) { memory ->
                            MemoryGridCard(
                                memory = memory,
                                hasVoted = votedMemoryIds.contains(memory.id),
                                onCardClick = { viewModel.openMemoryDetail(memory) },
                                onToggleVote = { viewModel.toggleVoteForMemory(memory.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Dialogs
    if (isUploadOpen) {
        UploadMemoryDialog(
            formState = uploadState,
            onImageSelected = { viewModel.setUploadImageUri(it) },
            onPresetSelected = { viewModel.setPresetSampleImage(it) },
            onGuestNameChanged = { viewModel.updateUploadGuestName(it) },
            onTableNumberChanged = { viewModel.updateUploadTableNumber(it) },
            onDedicationChanged = { viewModel.updateUploadDedication(it) },
            onTagChanged = { viewModel.updateUploadTag(it) },
            onQuickSnippetSelected = { viewModel.appendDedicationSnippet(it) },
            onSubmit = { viewModel.submitMemory() },
            onDismiss = { viewModel.closeUploadDialog() }
        )
    }

    if (isTableQrCardOpen) {
        TableQrCardDialog(
            selectedTable = currentQrTable,
            sharedUrl = partySharedUrl,
            onSelectTable = { viewModel.setQrTable(it) },
            onJoinAsGuestOfTable = { table ->
                viewModel.setGuestProfile(
                    name = if (guestProfile.hasCheckedIn) guestProfile.name else "Invitado Especial",
                    tableNumber = table
                )
            },
            onDismiss = { viewModel.closeTableQrCard() }
        )
    }

    if (isSlideshowOpen) {
        SlideshowScreen(
            memories = memories,
            currentIndex = slideshowIndex,
            isPlaying = slideshowPlaying,
            intervalSec = slideshowInterval,
            onTogglePlay = { viewModel.toggleSlideshowPlay() },
            onNext = { viewModel.nextSlide() },
            onPrev = { viewModel.previousSlide() },
            onIntervalChange = { viewModel.setSlideshowInterval(it) },
            onClose = { viewModel.closeSlideshow() }
        )
    }

    selectedMemoryDetail?.let { detail ->
        MemoryDetailDialog(
            memory = detail,
            hasVoted = votedMemoryIds.contains(detail.id),
            onReact = { viewModel.reactToMemory(detail.id, it) },
            onToggleVote = { viewModel.toggleVoteForMemory(detail.id) },
            onDismiss = { viewModel.closeMemoryDetail() }
        )
    }
}

@Composable
private fun HeroCelebrationBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hero_celebration_banner"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            1.5.dp,
            Brush.linearGradient(listOf(QuinceGold, QuinceRose, QuinceChampagne))
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.quince_hero_banner),
                contentDescription = "Mis 15 Luchy",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Scrim gradient for contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.25f),
                                Color.Black.copy(alpha = 0.75f)
                            )
                        )
                    )
            )

            // Banner Text & CTAs
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, QuinceGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = QuinceGoldLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Dots Memories Quinceañera",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Bottom Content
                Column {
                    Text(
                        text = "¡Bienvenidos a mis 15! 👑",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Comparte tus fotos y dedicatorias en vivo para proyectarlas en el salón y vota por tus favoritas.",
                        color = QuinceGoldLight,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterAndSortBar(
    tags: List<String>,
    selectedTag: String,
    onSelectTag: (String) -> Unit,
    sortMode: SortMode,
    onToggleSort: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Tag Filters
        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tags) { tag ->
                val isSelected = tag == selectedTag
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectTag(tag) },
                    label = {
                        Text(
                            text = tag,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = QuinceRose,
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) QuinceRose else QuinceGold.copy(alpha = 0.4f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Sort toggle chip (Recientes vs Top Votos)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (sortMode == SortMode.POPULAR) QuinceGold else MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, QuinceGold),
            modifier = Modifier.clickable(onClick = onToggleSort)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = if (sortMode == SortMode.POPULAR) Icons.Filled.Star else Icons.Filled.Whatshot,
                    contentDescription = null,
                    tint = if (sortMode == SortMode.POPULAR) Color.White else QuinceGoldDark,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = if (sortMode == SortMode.POPULAR) "Top Votos" else "Recientes",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (sortMode == SortMode.POPULAR) Color.White else QuinceGoldDark
                )
            }
        }
    }
}

@Composable
private fun EmptyMemoriesView(onUploadClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = QuinceBlush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AddAPhoto,
                contentDescription = null,
                tint = QuinceRose,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "¡Aún no hay fotos en esta categoría!",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Sé el primero en compartir un momento y dedicatoria para Luchy.",
                fontSize = 12.sp,
                color = QuinceTextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
