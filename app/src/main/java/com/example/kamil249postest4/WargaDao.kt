package com.example.kamil249postest4

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WargaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(warga: Warga)

    @Update
    fun update(warga: Warga)

    @Delete
    fun delete(warga: Warga)

    @Query("DELETE FROM warga")
    fun deleteAll()

    @Query("SELECT * from warga ORDER BY id ASC")
    fun getAllWarga(): LiveData<List<Warga>>

    @Query("SELECT * FROM warga WHERE id = :wargaId")
    fun getWargaById(wargaId: Int): Warga
}