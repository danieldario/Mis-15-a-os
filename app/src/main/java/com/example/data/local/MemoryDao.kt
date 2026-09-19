package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MemoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {
    @Query("SELECT * FROM memories ORDER BY isPinned DESC, timestamp DESC")
    fun getAllMemories(): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories WHERE tag = :tag ORDER BY isPinned DESC, timestamp DESC")
    fun getMemoriesByTag(tag: String): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories WHERE id = :id LIMIT 1")
    suspend fun getMemoryById(id: Long): MemoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(memories: List<MemoryEntity>)

    @Update
    suspend fun updateMemory(memory: MemoryEntity)

    @Query("UPDATE memories SET reactionHearts = reactionHearts + 1 WHERE id = :id")
    suspend fun incrementHearts(id: Long)

    @Query("UPDATE memories SET reactionCheers = reactionCheers + 1 WHERE id = :id")
    suspend fun incrementCheers(id: Long)

    @Query("UPDATE memories SET reactionSparkles = reactionSparkles + 1 WHERE id = :id")
    suspend fun incrementSparkles(id: Long)

    @Query("UPDATE memories SET votes = votes + 1 WHERE id = :id")
    suspend fun incrementVotes(id: Long)

    @Query("UPDATE memories SET votes = CASE WHEN votes > 0 THEN votes - 1 ELSE 0 END WHERE id = :id")
    suspend fun decrementVotes(id: Long)

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteMemory(id: Long)

    @Query("SELECT COUNT(*) FROM memories")
    suspend fun getCount(): Int
}
