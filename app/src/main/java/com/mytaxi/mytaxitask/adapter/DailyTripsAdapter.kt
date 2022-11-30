package com.mytaxi.mytaxitask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mytaxi.mytaxitask.databinding.ItemTripsDailyBinding
import com.mytaxi.mytaxitask.model.DailyTrips

class DailyTripsAdapter(val list: List<DailyTrips>, val onClick: (pos: Int) -> Unit) :
    RecyclerView.Adapter<DailyTripsAdapter.DailyTripsVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTripsVH {
        return DailyTripsVH(ItemTripsDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DailyTripsVH, position: Int) {

        holder.binding.apply {
            list[position].apply {
                rvRides.adapter = TripAdapter(trips, onClick)
                tvRideDate.text = date
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class DailyTripsVH(var binding: ItemTripsDailyBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}