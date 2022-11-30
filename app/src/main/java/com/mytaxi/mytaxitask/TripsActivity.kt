package com.mytaxi.mytaxitask

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mytaxi.mytaxitask.adapter.DailyTripsAdapter
import com.mytaxi.mytaxitask.databinding.ActivityTripsBinding
import com.mytaxi.mytaxitask.model.DailyTrips
import com.mytaxi.mytaxitask.model.Trip
import kotlinx.coroutines.delay

class TripsActivity : AppCompatActivity() {
    lateinit var binding: ActivityTripsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.btnBack.setOnClickListener {
            this.finish()
        }
        refreshAdapter()


    }

    private fun refreshAdapter() {
        val dailyRides: ArrayList<DailyTrips> = ArrayList()

        dailyRides.add(
            DailyTrips(
                "6 Июля, Вторник",
                listOf(
                    Trip(
                        "улица Sharof Rashidov, Ташкент",
                        "5a улица Асадуллы Ходжаева",
                        "21:36",
                        "12 900 cум",
                        Constants.TYPE_STANDARD
                    ),
                    Trip(
                        "Яшнабадский район, улица Sharof Rashidov, Ташкент",
                        "Юнусабадский район, м-в юнусабад-19, ул. дехканабад, 17",
                        "19:24",
                        "14 800 cум",
                        Constants.TYPE_LUGGAGE
                    ),
                    Trip(
                        "улица Sharof Rashidov, Ташкент",
                        "5a улица Асадуллы Ходжаева",
                        "21:36",
                        "12 900 cум",
                        Constants.TYPE_BUSINESS
                    )
                )
            )
        )

        dailyRides.add(
            DailyTrips(
                "5 Июля, Вторник",
                listOf(
                    Trip(
                        "улица Sharof Rashidov, Ташкент",
                        "5a улица Асадуллы Ходжаева",
                        "21:36",
                        "12 900 cум",
                        Constants.TYPE_STANDARD
                    ),
                    Trip(
                        "Яшнабадский район, улица Sharof Rashidov, Ташкент",
                        "Юнусабадский район, м-в юнусабад-19, ул. дехканабад, 17",
                        "19:24",
                        "14 800 cум",
                        Constants.TYPE_LUGGAGE
                    ),
                    Trip(
                        "улица Sharof Rashidov, Ташкент",
                        "5a улица Асадуллы Ходжаева",
                        "21:36",
                        "12 900 cум",
                        Constants.TYPE_BUSINESS
                    )
                )
            )
        )
        lifecycleScope.launchWhenStarted {
            delay(1000)
            binding.rvRides.show()
            binding.rvRides.adapter = DailyTripsAdapter(dailyRides) {
                startActivity(Intent(this@TripsActivity, TripDetailsActivity::class.java))
            }
            binding.shimmer.stopShimmerAnimation()
            binding.shimmer.hide()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.shimmer.startShimmerAnimation()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmer.stopShimmerAnimation()
    }


}