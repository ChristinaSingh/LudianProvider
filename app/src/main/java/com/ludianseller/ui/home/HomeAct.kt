package com.ludianseller.ui.home

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ludianseller.R
import com.ludianseller.network.NetworkChangeReceiver
import com.ludianseller.utils.Helper.Companion.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeAct : AppCompatActivity() {

    private var navView: BottomNavigationView? = null
    private var navController: NavController? = null
    private var ivPost: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        ivPost = findViewById(R.id.ivPost)

        navView = findViewById<BottomNavigationView>(R.id.nav_view)
        navView!!.itemIconTintList = null;



        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_home,
            R.id.navigation_booking,
            R.id.navigation_post,
            R.id.navigation_calendar,
            R.id.navigation_profile).build()


        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(navView!!, navController!!)

/*
        ivPost!!.setOnClickListener{
            navController!!.navigate(R.id.navigation_post)
        }
*/



        navController!!.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> showBottomNavigation()
                R.id.navigation_booking -> showBottomNavigation()
               // R.id.navigation_post -> showBottomNavigation()
                R.id.navigation_calendar -> showBottomNavigation()
                R.id.navigation_profile -> showBottomNavigation()
                // Add more cases for other fragments if needed
                else -> hideBottomNavigation()
            }
        }




    }


    private fun showBottomNavigation() {
        navView!!.visibility = View.VISIBLE
        ivPost!!.visibility =   View.VISIBLE
    }

    private fun hideBottomNavigation() {
        navView!!.visibility = View.GONE
        ivPost!!.visibility =   View.GONE

    }





}