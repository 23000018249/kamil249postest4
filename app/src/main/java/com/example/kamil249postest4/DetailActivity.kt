package com.example.kamil249postest4

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kamil249postest4.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var appExecutors: AppExecutor
    private lateinit var wargaDao: WargaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appExecutors = AppExecutor()
        wargaDao = DatabaseWarga.getDatabase(this).wargaDao()

        val wargaId = intent.getIntExtra("warga_id", -1)
        if (wargaId != -1) {
            loadWargaData(wargaId)
        }
    }

    private fun loadWargaData(wargaId: Int) {
        appExecutors.diskIO.execute {
            val selectedWarga = wargaDao.getWargaById(wargaId)

            appExecutors.mainThread.execute {
                binding.apply {
                    etNamaLengkap.setText(selectedWarga.namaLengkap)
                    etNIK.setText(selectedWarga.nik)
                    etKabupaten.setText(selectedWarga.kabupaten)

                    btnUpdate.setOnClickListener {
                        updateData(selectedWarga)
                    }

                    btnDelete.setOnClickListener {
                        deleteData(selectedWarga)
                    }
                }
            }
        }
    }

    private fun updateData(originalWarga: Warga) {
        binding.apply {
            val updatedWarga = originalWarga.copy(
                namaLengkap = etNamaLengkap.text.toString(),
                nik = etNIK.text.toString(),
                kabupaten = etKabupaten.text.toString()
            )

            appExecutors.diskIO.execute {
                wargaDao.update(updatedWarga)
                appExecutors.mainThread.execute {
                    Toast.makeText(this@DetailActivity, "Data berhasil diupdate!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteData(warga: Warga) {
        appExecutors.diskIO.execute {
            wargaDao.delete(warga)
            appExecutors.mainThread.execute {
                Toast.makeText(this@DetailActivity, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}