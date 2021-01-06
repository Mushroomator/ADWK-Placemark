package de.tp.placemark.models.room

import androidx.room.Database
import androidx.room.RoomDatabase
import de.tp.placemark.models.PlacemarkModel
import java.lang.reflect.Modifier

@Database(entities = arrayOf(PlacemarkModel::class), version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun placemarkDao(): PlacemarkDao
}