package com.chrisp.setaraapp.feature.cvGenerate.domain

import com.chrisp.setaraapp.feature.cvGenerate.data.local.CvDao
import com.chrisp.setaraapp.feature.cvGenerate.data.local.CvDataEntity

class CvRepository(private val cvDao: CvDao) {

    suspend fun saveCvData(cvData: CvDataEntity) {
        cvDao.saveCvData(cvData)
    }

    suspend fun getLatestCvData(): CvDataEntity? {
        return cvDao.getLatestCvData()
    }
}