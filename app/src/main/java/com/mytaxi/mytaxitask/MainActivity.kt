package com.mytaxi.mytaxitask

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.mytaxi.mytaxitask.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val END_SCALE = 0.8f
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout

        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        navView.setupWithNavController(navController)
        navigationDrawer()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_rides -> {
                    startActivity(Intent(this, TripsActivity::class.java))
                }
            }
            drawerLayout.close()
            true
        }

        binding.appBarMain.contentMain.fab.apply {
            val params = FrameLayout.LayoutParams(
                120,
                120
            )
            params.setMargins(32, getStatusBarHeight() + 32, 0, 0)
            layoutParams = params

            setOnClickListener {
                drawerLayout.open()
            }
            requestLayout()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun navigationDrawer() {
        binding.navView.apply {
            bringToFront()
            setCheckedItem(R.id.nav_home)
        }
        animateNavigationDrawer()
    }

    private fun animateNavigationDrawer() {
        val contentView = binding.appBarMain.contentMain.root
        binding.drawerLayout.setScrimColor(Color.TRANSPARENT);

        binding.drawerLayout.addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                val diffScaledOffset: Float = slideOffset * (1 - END_SCALE)
                val offsetScale = 1 - diffScaledOffset
                contentView.scaleX = offsetScale
                contentView.scaleY = offsetScale

                val xOffset: Float = drawerView.width * slideOffset
                val xOffsetDiff: Float = contentView.width * diffScaledOffset / 2
                val xTranslation = xOffset - xOffsetDiff
                contentView.translationX = xTranslation

                binding.appBarMain.contentMain.cardView.radius = 64f
            }

            override fun onDrawerClosed(drawerView: View) {
                binding.appBarMain.contentMain.cardView.radius = 0f
                super.onDrawerClosed(drawerView)
            }
        })
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    private fun getStatusBarHeight(): Int {
        val resources: Resources = this.resources
        val id = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (id > 0) {
            resources.getDimensionPixelSize(id)
        } else 0
    }

}