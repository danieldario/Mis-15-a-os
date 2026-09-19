package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MemoryDatabase
import com.example.data.model.MemoryEntity
import com.example.data.repository.MemoryRepository
import com.example.data.notification.QuinceNotificationManager
import com.example.data.repository.ReactionType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class FeedDisplayMode {
    CARDS, GRID
}

enum class SortMode {
    RECENT, POPULAR
}

data class GuestProfile(
    val name: String = "",
    val tableNumber: String = "Mesa 1",
    val hasCheckedIn: Boolean = false
)

data class UploadFormState(
    val imageUri: String? = null,
    val guestName: String = "",
    val tableNumber: String = "Mesa 1",
    val dedication: String = "",
    val tag: String = "Momentos",
    val isCompressing: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
)

class MemoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MemoryRepository
    init {
        val database = MemoryDatabase.getDatabase(application)
        repository = MemoryRepository(database.memoryDao(), application)
        // Ensure sample data is populated on first start
        viewModelScope.launch {
            MemoryDatabase.populateInitialMemories(database.memoryDao())
        }
    }

    // Filter, Sort & View Mode
    val selectedTag = MutableStateFlow("Todos")
    val sortMode = MutableStateFlow(SortMode.RECENT)
    val feedDisplayMode = MutableStateFlow(FeedDisplayMode.CARDS)

    // Guest votes tracking (max 3 votes per guest)
    val maxVotesAllowed = 3
    private val _votedMemoryIds = MutableStateFlow<Set<Long>>(emptySet())
    val votedMemoryIds: StateFlow<Set<Long>> = _votedMemoryIds.asStateFlow()

    // Notification toast / banner message
    private val _recentNotificationMessage = MutableStateFlow<String?>(null)
    val recentNotificationMessage: StateFlow<String?> = _recentNotificationMessage.asStateFlow()

    // Memories stream sorted according to sortMode
    val memories: StateFlow<List<MemoryEntity>> = combine(
        selectedTag.flatMapLatest { tag -> repository.getMemoriesByTag(tag) },
        sortMode
    ) { list, sort ->
        when (sort) {
            SortMode.RECENT -> list // Ordered by isPinned DESC, timestamp DESC by DAO
            SortMode.POPULAR -> list.sortedWith(
                compareByDescending<MemoryEntity> { it.isPinned }
                    .thenByDescending { it.votes }
                    .thenByDescending { it.reactionHearts + it.reactionCheers + it.reactionSparkles }
                    .thenByDescending { it.timestamp }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Current Guest Info
    private val _guestProfile = MutableStateFlow(GuestProfile())
    val guestProfile: StateFlow<GuestProfile> = _guestProfile.asStateFlow()

    // Dialogs State
    private val _isUploadDialogOpen = MutableStateFlow(false)
    val isUploadDialogOpen: StateFlow<Boolean> = _isUploadDialogOpen.asStateFlow()

    private val _uploadFormState = MutableStateFlow(UploadFormState())
    val uploadFormState: StateFlow<UploadFormState> = _uploadFormState.asStateFlow()

    private val _selectedMemoryDetail = MutableStateFlow<MemoryEntity?>(null)
    val selectedMemoryDetail: StateFlow<MemoryEntity?> = _selectedMemoryDetail.asStateFlow()

    private val _isTableQrCardOpen = MutableStateFlow(false)
    val isTableQrCardOpen: StateFlow<Boolean> = _isTableQrCardOpen.asStateFlow()

    private val _currentQrTable = MutableStateFlow("Mesa 5")
    val currentQrTable: StateFlow<String> = _currentQrTable.asStateFlow()

    // Slideshow Mode (Projector mode)
    private val _isSlideshowOpen = MutableStateFlow(false)
    val isSlideshowOpen: StateFlow<Boolean> = _isSlideshowOpen.asStateFlow()

    private val _slideshowIndex = MutableStateFlow(0)
    val slideshowIndex: StateFlow<Int> = _slideshowIndex.asStateFlow()

    private val _slideshowIsPlaying = MutableStateFlow(true)
    val slideshowIsPlaying: StateFlow<Boolean> = _slideshowIsPlaying.asStateFlow()

    private val _slideshowIntervalSec = MutableStateFlow(5)
    val slideshowIntervalSec: StateFlow<Int> = _slideshowIntervalSec.asStateFlow()

    private var slideshowJob: Job? = null

    fun setGuestProfile(name: String, tableNumber: String) {
        _guestProfile.update {
            it.copy(
                name = name.trim(),
                tableNumber = tableNumber.trim(),
                hasCheckedIn = true
            )
        }
    }

    fun setSelectedTag(tag: String) {
        selectedTag.value = tag
    }

    fun setSortMode(mode: SortMode) {
        sortMode.value = mode
    }

    fun toggleSortMode() {
        sortMode.update {
            if (it == SortMode.RECENT) SortMode.POPULAR else SortMode.RECENT
        }
    }

    fun toggleFeedDisplayMode() {
        feedDisplayMode.update {
            if (it == FeedDisplayMode.CARDS) FeedDisplayMode.GRID else FeedDisplayMode.CARDS
        }
    }

    fun openUploadDialog() {
        val currentGuest = _guestProfile.value
        _uploadFormState.value = UploadFormState(
            guestName = if (currentGuest.hasCheckedIn) currentGuest.name else "",
            tableNumber = if (currentGuest.hasCheckedIn) currentGuest.tableNumber else "Mesa 1",
            tag = "Momentos"
        )
        _isUploadDialogOpen.value = true
    }

    fun closeUploadDialog() {
        _isUploadDialogOpen.value = false
    }

    fun setUploadImageUri(uri: Uri) {
        viewModelScope.launch {
            _uploadFormState.update { it.copy(isCompressing = true) }
            val optimizedPath = repository.optimizeAndSaveImage(uri)
            _uploadFormState.update {
                it.copy(
                    imageUri = optimizedPath,
                    isCompressing = false
                )
            }
        }
    }

    fun setPresetSampleImage(drawableResName: String) {
        _uploadFormState.update {
            it.copy(imageUri = "res://drawable/$drawableResName")
        }
    }

    fun updateUploadGuestName(name: String) {
        _uploadFormState.update { it.copy(guestName = name) }
    }

    fun updateUploadTableNumber(table: String) {
        _uploadFormState.update { it.copy(tableNumber = table) }
    }

    fun updateUploadDedication(dedication: String) {
        _uploadFormState.update { it.copy(dedication = dedication) }
    }

    fun updateUploadTag(tag: String) {
        _uploadFormState.update { it.copy(tag = tag) }
    }

    fun appendDedicationSnippet(snippet: String) {
        _uploadFormState.update { current ->
            val newText = if (current.dedication.isBlank()) {
                snippet
            } else {
                "${current.dedication} $snippet"
            }
            current.copy(dedication = newText)
        }
    }

    fun submitMemory() {
        val state = _uploadFormState.value
        val image = state.imageUri
        if (image.isNullOrBlank()) {
            _uploadFormState.update { it.copy(errorMessage = "Por favor selecciona o toma una foto para el recuerdo.") }
            return
        }
        val guestName = state.guestName.ifBlank { "Invitado Especial" }
        val table = state.tableNumber.ifBlank { "Mesa 1" }
        val dedication = state.dedication.ifBlank { "¡Muchas felicidades en tus 15 años Luchy! ✨💖" }

        viewModelScope.launch {
            _uploadFormState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val newEntity = MemoryEntity(
                guestName = guestName,
                tableNumber = table,
                dedication = dedication,
                imageUri = image,
                tag = state.tag,
                timestamp = System.currentTimeMillis()
            )
            val generatedId = repository.addMemory(newEntity)
            val savedEntity = newEntity.copy(id = generatedId)

            // Save profile if not saved
            if (!_guestProfile.value.hasCheckedIn) {
                setGuestProfile(guestName, table)
            }

            // Trigger Real-time Notification for the birthday girl (Luchy) / admin
            QuinceNotificationManager.notifyNewMemoryUploaded(
                getApplication(),
                savedEntity
            )
            _recentNotificationMessage.value = "¡Nuevo recuerdo de $guestName publicado para Luchy!"

            _uploadFormState.update { it.copy(isSubmitting = false) }
            _isUploadDialogOpen.value = false
        }
    }

    fun dismissNotificationMessage() {
        _recentNotificationMessage.value = null
    }

    /**
     * Toggles a guest's vote on a photo.
     * Each guest has a maximum of 3 votes.
     */
    fun toggleVoteForMemory(id: Long) {
        val currentVotes = _votedMemoryIds.value
        if (currentVotes.contains(id)) {
            // Unvote
            viewModelScope.launch {
                repository.unvote(id)
                _votedMemoryIds.update { it - id }
                _selectedMemoryDetail.value?.let { current ->
                    if (current.id == id) {
                        _selectedMemoryDetail.value = current.copy(votes = maxOf(0, current.votes - 1))
                    }
                }
            }
        } else {
            // Vote - check limit of 3
            if (currentVotes.size >= maxVotesAllowed) {
                _recentNotificationMessage.value = "Ya has usado tus $maxVotesAllowed votos. Desmarca una foto para votar por otra."
                return
            }
            viewModelScope.launch {
                repository.vote(id)
                _votedMemoryIds.update { it + id }
                _selectedMemoryDetail.value?.let { current ->
                    if (current.id == id) {
                        _selectedMemoryDetail.value = current.copy(votes = current.votes + 1)
                    }
                }
            }
        }
    }

    fun reactToMemory(id: Long, type: ReactionType) {
        viewModelScope.launch {
            repository.react(id, type)
            // If current detail is open for this memory, update local view
            _selectedMemoryDetail.value?.let { current ->
                if (current.id == id) {
                    _selectedMemoryDetail.value = when (type) {
                        ReactionType.HEART -> current.copy(reactionHearts = current.reactionHearts + 1)
                        ReactionType.CHEERS -> current.copy(reactionCheers = current.reactionCheers + 1)
                        ReactionType.SPARKLES -> current.copy(reactionSparkles = current.reactionSparkles + 1)
                    }
                }
            }
        }
    }

    fun openMemoryDetail(memory: MemoryEntity) {
        _selectedMemoryDetail.value = memory
    }

    fun closeMemoryDetail() {
        _selectedMemoryDetail.value = null
    }

    fun openTableQrCard(table: String = "Mesa 5") {
        _currentQrTable.value = table
        _isTableQrCardOpen.value = true
    }

    fun closeTableQrCard() {
        _isTableQrCardOpen.value = false
    }

    fun setQrTable(table: String) {
        _currentQrTable.value = table
    }

    // Slideshow controls
    fun openSlideshow() {
        _isSlideshowOpen.value = true
        _slideshowIndex.value = 0
        _slideshowIsPlaying.value = true
        startSlideshowTimer()
    }

    fun closeSlideshow() {
        _isSlideshowOpen.value = false
        slideshowJob?.cancel()
    }

    fun toggleSlideshowPlay() {
        val newPlaying = !_slideshowIsPlaying.value
        _slideshowIsPlaying.value = newPlaying
        if (newPlaying) {
            startSlideshowTimer()
        } else {
            slideshowJob?.cancel()
        }
    }

    fun nextSlide() {
        val count = memories.value.size
        if (count > 0) {
            _slideshowIndex.update { (it + 1) % count }
        }
    }

    fun previousSlide() {
        val count = memories.value.size
        if (count > 0) {
            _slideshowIndex.update { if (it - 1 < 0) count - 1 else it - 1 }
        }
    }

    fun setSlideshowInterval(seconds: Int) {
        _slideshowIntervalSec.value = seconds
        if (_slideshowIsPlaying.value) {
            startSlideshowTimer()
        }
    }

    private fun startSlideshowTimer() {
        slideshowJob?.cancel()
        slideshowJob = viewModelScope.launch {
            while (_slideshowIsPlaying.value) {
                delay(_slideshowIntervalSec.value * 1000L)
                nextSlide()
            }
        }
    }
}
