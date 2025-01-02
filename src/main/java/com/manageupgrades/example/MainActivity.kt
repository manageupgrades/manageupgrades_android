package com.manageupgrades.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.manageupgrades.ManageUpgradesService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkForUpdates()
    }

    private fun checkForUpdates() {
        // Example parameters
        val projectId = "projectid"
        val version = "1.0.0"
        val platform = "android"
        val apiKey = "123"
        val googleId = "com.example.app"
        val appleId = "" // Not needed for Android

        ManageUpgradesService.shared.checkAppStatus(
            activity = this,
            projectId = projectId,
            version = version,
            platform = platform,
            apiKey = apiKey,
            googleId = googleId,
            appleId = appleId
        )
    }
}
