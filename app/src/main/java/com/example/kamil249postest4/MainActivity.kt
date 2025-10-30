package com.example.kamil249postest4

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.kamil249postest4.R
import com.example.kamil249postest4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbWarga: DatabaseWarga
    private lateinit var wargaDao: WargaDao
    private lateinit var appExecutors: AppExecutor

    private var listWarga: List<Warga> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appExecutors = AppExecutor()
        dbWarga = DatabaseWarga.getDatabase(applicationContext)
        wargaDao = dbWarga.wargaDao()

        setupListeners()
        observeWargaData()
    }

    private fun setupListeners() {
        binding.btnSimpanData.setOnClickListener {
            simpanDataWarga()
        }

        binding.btnResetData.setOnClickListener {
            resetSemuaData()
        }

        binding.lvWarga.setOnItemClickListener { _, _, position, _ ->
            if (listWarga.isNotEmpty()) {
                val selectedWarga = listWarga[position]
                val detailIntent = Intent(this, DetailActivity::class.java)
                detailIntent.putExtra("warga_id", selectedWarga.id)
                startActivity(detailIntent)
            }
        }
    }

    private fun observeWargaData() {
        wargaDao.getAllWarga().observe(this, Observer { list ->
            listWarga = list

            val displayList = list.mapIndexed { index, warga ->
                val alamat = "Alamat: RT ${warga.rt}/RW ${warga.rw}, ${warga.desa}, ${warga.kecamatan}, ${warga.kabupaten}"
                "${index + 1}. ${warga.namaLengkap} (${warga.jenisKelamin}) - ${warga.statusPernikahan}\nNIK: ${warga.nik}\n$alamat"
            }

            binding.lvWarga.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                displayList.ifEmpty { listOf("Belum ada data warga yang tersimpan.") }
            )

            clearForm()
        })
    }

    private fun simpanDataWarga() {
        binding.apply {
            val nama = etNamaLengkap.text.toString().trim()
            val nik = etNIK.text.toString().trim()
            val kabupaten = etKabupaten.text.toString().trim()
            val kecamatan = etKecamatan.text.toString().trim()
            val desa = etDesa.text.toString().trim()
            val rt = etRT.text.toString().trim()
            val rw = etRW.text.toString().trim()

            if (nama.isEmpty() || nik.isEmpty() || kabupaten.isEmpty() || kecamatan.isEmpty() || desa.isEmpty() || rt.isEmpty() || rw.isEmpty()) {
                Toast.makeText(this@MainActivity, "Semua data harus diisi!", Toast.LENGTH_SHORT).show()
                return
            }

            val selectedGenderId = rgJenisKelamin.checkedRadioButtonId
            val jenisKelamin = findViewById<RadioButton>(selectedGenderId).text.toString()

            val statusPernikahan = spStatusPernikahan.selectedItem.toString()

            val newWarga = Warga(
                namaLengkap = nama, nik = nik, jenisKelamin = jenisKelamin,
                statusPernikahan = statusPernikahan, kabupaten = kabupaten,
                kecamatan = kecamatan, desa = desa, rt = rt, rw = rw
            )

            appExecutors.diskIO.execute {
                wargaDao.insert(newWarga)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    clearForm()
                }
            }
        }
    }

    private fun resetSemuaData() {
        appExecutors.diskIO.execute {
            wargaDao.deleteAll()
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Semua data berhasil dihapus!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearForm() {
        binding.etNamaLengkap.text.clear()
        binding.etNIK.text.clear()
        binding.etKabupaten.text.clear()
        binding.etKecamatan.text.clear()
        binding.etDesa.text.clear()
        binding.etRT.text.clear()
        binding.etRW.text.clear()
        binding.rgJenisKelamin.check(R.id.rbLakiLaki)
        binding.spStatusPernikahan.setSelection(0)
    }
}