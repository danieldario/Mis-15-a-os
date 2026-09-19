package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.MemoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [MemoryEntity::class], version = 2, exportSchema = false)
abstract class MemoryDatabase : RoomDatabase() {
    abstract fun memoryDao(): MemoryDao

    companion object {
        @Volatile
        private var INSTANCE: MemoryDatabase? = null

        fun getDatabase(context: Context): MemoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemoryDatabase::class.java,
                    "mis15_memories_db"
                ).fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Pre-populate with initial celebration memories
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                populateInitialMemories(database.memoryDao())
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateInitialMemories(dao: MemoryDao) {
            if (dao.getCount() > 0) return

            val now = System.currentTimeMillis()
            val sampleMemories = listOf(
                MemoryEntity(
                    guestName = "Luchy (Quinceañera)",
                    tableNumber = "Mesa de Honor",
                    dedication = "¡Bienvenidos a la mejor noche de mi vida! Gracias por estar aquí conmigo en mis 15 primaveras ✨👑💖",
                    imageUri = "res://drawable/quince_hero_banner",
                    tag = "Vals",
                    timestamp = now - 1000 * 60 * 30, // 30 min ago
                    reactionHearts = 48,
                    reactionCheers = 32,
                    reactionSparkles = 56,
                    votes = 12,
                    isPinned = true
                ),
                MemoryEntity(
                    guestName = "Tía Carmen & Familia",
                    tableNumber = "Mesa 3",
                    dedication = "¡Te vimos crecer y hoy eres una princesa hermosa! Que Dios bendiga cada uno de tus pasos. ¡Te amamos Luchy!",
                    imageUri = "res://drawable/quince_vals_moment",
                    tag = "Vals",
                    timestamp = now - 1000 * 60 * 20,
                    reactionHearts = 24,
                    reactionCheers = 19,
                    reactionSparkles = 28,
                    votes = 7
                ),
                MemoryEntity(
                    guestName = "Mateo y los del Colegio",
                    tableNumber = "Mesa 7",
                    dedication = "¡La pista de baile ya es nuestra! ¡Felices 15 Luchy, rompiste con el vestido! 💃🎉🔥",
                    imageUri = "res://drawable/quince_cake_festive",
                    tag = "Fiesta",
                    timestamp = now - 1000 * 60 * 12,
                    reactionHearts = 35,
                    reactionCheers = 41,
                    reactionSparkles = 30,
                    votes = 9
                ),
                MemoryEntity(
                    guestName = "Abuelita Rosa",
                    tableNumber = "Mesa de Honor",
                    dedication = "Mi niña adorada Luchy, tu vals con tu papá nos sacó lágrimas a todos. Eres la alegría de la casa.",
                    imageUri = "res://drawable/quince_vals_moment",
                    tag = "Dedicatoria",
                    timestamp = now - 1000 * 60 * 5,
                    reactionHearts = 52,
                    reactionCheers = 18,
                    reactionSparkles = 44,
                    votes = 15
                )
            )
            dao.insertAll(sampleMemories)
        }
    }
}
