package com.manageupgrades.example;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.manageupgrades.ManageUpgradesService;

public class JavaMainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize the ManageUpgrades service
        ManageUpgradesService upgradeService = ManageUpgradesService.getShared();
        
        // Check for updates
        upgradeService.checkAppStatus(
            this,
            "YOUR_PROJECT_ID",  // Replace with your project ID from ManageUpgrades dashboard
            "1.0.0",           // Your app version
            "android",         // Platform
            "YOUR_API_KEY",    // Your API key from ManageUpgrades dashboard
            "com.yourapp.id",  // Your app's Google Play Store ID
            ""                 // Leave empty for Android
        );
    }
}
