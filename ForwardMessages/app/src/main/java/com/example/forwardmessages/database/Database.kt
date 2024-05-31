package com.example.forwardmessages

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Group::class, Contact::class, Relationship::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): Daos

//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getInstance(context: Context): AppDatabase {
//            synchronized(this) {
//                return INSTANCE ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "app_database"
//                ).build().also {
//                    INSTANCE = it
//                }
//            }
//        }
//    }
}