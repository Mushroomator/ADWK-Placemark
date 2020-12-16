package de.tp.placemark.models

interface PlacemarkStore {
    /**
     * Returns all placemarks
     * @return all stored Placemarks
     * @author Thomas Pilz
     */
    fun findAll(): List<PlacemarkModel>

    /**
     * Stores a new placemark
     * @param placemark new Placemark to be stored
     */
    fun create(placemark: PlacemarkModel)

    /**
     * Updates a placemark in the store
     * @param placemark Placemark to be updated
     * @author Thomas Pilz
     */
    fun update(placemark: PlacemarkModel)

    /**
     * Delete a placemark from the store
     * @param placemark Placemark to be deleted
     */
    fun delete(placemark: PlacemarkModel)

    /**
     * Find a placemark by its ID. Null will be returned if none is found.
     * @param id ID of Placemark to be deleted
     * @author Thomas Pilz
     */
    fun findById(id:Long) : PlacemarkModel?

    /**
     * Logs all stored placemarks to the console
     * @author Thomas Pilz
     */
    fun logAll(): Unit
}