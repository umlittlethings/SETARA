package com.chrisp.setaraapp.feature.cvGenerate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CvDao {
    // Menggunakan REPLACE, jadi jika ada data, akan diganti, jika tidak ada, akan ditambahkan.
    // Kita hanya akan menyimpan satu baris data untuk draft CV.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCvData(cvData: CvDataEntity)

    // Mengambil data CV terakhir yang disimpan
    @Query("SELECT * FROM cv_data ORDER BY id DESC LIMIT 1")
    suspend fun getLatestCvData(): CvDataEntity?
}