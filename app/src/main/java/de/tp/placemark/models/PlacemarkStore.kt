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
     * Logs all stored placemarks to the console
     * @author Thomas Pilz
     */
    fun logAll(): Unit
}