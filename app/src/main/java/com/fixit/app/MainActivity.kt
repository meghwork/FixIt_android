package com.fixit.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Kitchen
        findViewById<androidx.cardview.widget.CardView>(R.id.cardKitchen).setOnClickListener {
            startActivity(Intent(this, KitchenActivity::class.java))
        }

        // Electronics
        findViewById<androidx.cardview.widget.CardView>(R.id.cardElectronics).setOnClickListener {
            startActivity(Intent(this, ElectronicsActivity::class.java))
        }

        // Cameras & Optics
        findViewById<androidx.cardview.widget.CardView>(R.id.cardCameras).setOnClickListener {
            startActivity(Intent(this, CamerasActivity::class.java))
        }

        // Computers
        findViewById<androidx.cardview.widget.CardView>(R.id.cardComputers).setOnClickListener {
            startActivity(Intent(this, ComputersActivity::class.java))
        }

        // Mobile Devices
        findViewById<androidx.cardview.widget.CardView>(R.id.cardMobile).setOnClickListener {
            startActivity(Intent(this, MobileDevicesActivity::class.java))
        }

        // Home Appliances
        findViewById<androidx.cardview.widget.CardView>(R.id.cardHome).setOnClickListener {
            startActivity(Intent(this, HomeAppliancesActivity::class.java))
        }
    }
}