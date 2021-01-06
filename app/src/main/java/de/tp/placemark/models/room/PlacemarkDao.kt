package de.tp.placemark.models.room

import androidx.room.*
import de.tp.placemark.models.PlacemarkModel

@Dao
interface PlacemarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(placemark: PlacemarkModel)

    @Query("SELECT * FROM PlacemarkModel")
    fun findAll(): List<PlacemarkModel>

    @Query("SELECT * FROM PlacemarkModel WHERE id = :id")
    fun findById(id: Long): PlacemarkModel

    @Update
    fun update(placemark: PlacemarkModel)

    @Delete
    fun delete(placemark: PlacemarkModel)

}