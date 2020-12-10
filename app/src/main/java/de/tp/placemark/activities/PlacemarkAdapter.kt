package de.tp.placemark.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.tp.placemark.R
import de.tp.placemark.models.PlacemarkModel
import kotlinx.android.synthetic.main.card_placemark.view.*

// Interface for clicking on Placemarks in the Recycler view within PlacemarkListActivity
interface PlacemarkListener{
    fun onPlacemarkClick(placemark: PlacemarkModel)
}

class PlacemarkAdapter constructor(
    private var placemarks: List<PlacemarkModel>,
    private val listener: PlacemarkListener
    ) : RecyclerView.Adapter<PlacemarkAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_placemark, parent,false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = placemarks[holder.adapterPosition]
        holder.bind(placemark, this.listener)
    }

    override fun getItemCount(): Int = placemarks.size

    // inner class
    class MainHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(placemark: PlacemarkModel, listener: PlacemarkListener) {
            itemView.placemarkTitle.text = placemark.title
            itemView.placemarkDescription.text = placemark.description
            itemView.setOnClickListener{ listener.onPlacemarkClick(placemark) }
        }
    }
}