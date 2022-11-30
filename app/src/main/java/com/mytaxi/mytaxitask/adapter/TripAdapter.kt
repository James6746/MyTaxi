package com.mytaxi.mytaxitask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mytaxi.mytaxitask.Constants
import com.mytaxi.mytaxitask.R
import com.mytaxi.mytaxitask.databinding.ItemTripBinding
import com.mytaxi.mytaxitask.model.Trip

class TripAdapter(private val list: List<Trip>, val onClick: (pos: Int) -> Unit) :
    RecyclerView.Adapter<TripAdapter.TripsVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripsVH {
        return TripsVH(ItemTripBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TripsVH, position: Int) {
        holder.binding.apply {
            list[position].apply {
                tvDeparturePoint.text = departurePoint
                tvDestination.text = destination
                tvTimeAndCost.text = time.plus(" ・ ").plus(rideCost)

                when (rideType) {
                    Constants.TYPE_STANDARD -> ivRideType.setImageResource(R.drawable.ic_car_standard)
                    Constants.TYPE_BUSINESS -> ivRideType.setImageResource(R.drawable.ic_car_business)
                    Constants.TYPE_LUGGAGE -> ivRideType.setImageResource(R.drawable.ic_car_luggage)
                }
            }
            rideLayout.setOnClickListener {
                onClick(position)
            }


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class TripsVH(val binding: ItemTripBinding) : RecyclerView.ViewHolder(binding.root) {

    }


}