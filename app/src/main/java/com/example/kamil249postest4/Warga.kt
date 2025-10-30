package com.example.kamil249postest4

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "warga")
data class Warga(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "nama_lengkap")
    val namaLengkap: String,

    @ColumnInfo(name = "nik")
    val nik: String,

    @ColumnInfo(name = "jenis_kelamin")
    val jenisKelamin: String,

    @ColumnInfo(name = "status_pernikahan")
    val statusPernikahan: String,

    @ColumnInfo(name = "kabupaten")
    val kabupaten: String,

    @ColumnInfo(name = "kecamatan")
    val kecamatan: String,

    @ColumnInfo(name = "desa")
    val desa: String,

    @ColumnInfo(name = "rt")
    val rt: String,

    @ColumnInfo(name = "rw")
    val rw: String
)