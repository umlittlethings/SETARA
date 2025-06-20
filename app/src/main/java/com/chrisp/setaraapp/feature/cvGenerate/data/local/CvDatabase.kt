package com.chrisp.setaraapp.feature.cvGenerate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CvDataEntity::class], version = 1, exportSchema = false)
abstract class CvDatabase : RoomDatabase() {

    abstract fun cvDao(): CvDao

    companion object {
        @Volatile
        private var INSTANCE: CvDatabase? = null

        fun getDatabase(context: Context): CvDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CvDatabase::class.java,
                    "cv_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}