package com.manageupgrades

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import android.util.Log


class ManageUpgradesService private constructor() {
    companion object {
        @JvmStatic
        val shared = ManageUpgradesService()
    }

    fun checkAppStatus(
        activity: Activity,
        projectId: String,
        version: String,
        platform: String,
        apiKey: String,
        googleId: String,
        appleId: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
               

                val url = URL("https://api.manageupgrades.com/checkupdate")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Authorization", apiKey)
                connection.doOutput = true

                val jsonBody = JSONObject().apply {
                    put("project_id", projectId)
                    put("version", version)
                    put("platform", platform)
                    put("api_key", apiKey)
                }



                OutputStreamWriter(connection.outputStream).use {
                    it.write(jsonBody.toString())
                }

              
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)
                    
                   

                    val maintenanceMode = jsonResponse.optBoolean("maintenance_mode", false)
                    val appUpdateNotes = jsonResponse.optString("appUpdate_notes", "")
                    val updateScenario = jsonResponse.optString("update_scenario", "")

                    activity.runOnUiThread {
                        when {
                            maintenanceMode -> showMaintenanceAlert(activity)
                            updateScenario == "Force Upgrade" -> showForceUpdateAlert(activity, appUpdateNotes, googleId)
                            updateScenario == "Display Message" -> showUpdateMessage(activity, appUpdateNotes, googleId)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showMaintenanceAlert(activity: Activity) {
        val alertDialog = AlertDialog.Builder(activity)
            .setTitle("Under Maintenance")
            .setMessage("The app is under maintenance. Please try again later.")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                showMaintenanceAlert(activity)
            }
            .create()

        alertDialog.show()
    }

    private fun showForceUpdateAlert(activity: Activity, message: String, googleId: String) {
        AlertDialog.Builder(activity)
            .setTitle("Update Required")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Update") { _, _ ->
                openPlayStore(activity, googleId)
                showForceUpdateAlert(activity,message,googleId)
            }
            .create()
            .show()
    }

    private fun showUpdateMessage(activity: Activity, message: String, googleId: String) {
        AlertDialog.Builder(activity)
            .setTitle("Update Available")
            .setMessage(message)
            .setPositiveButton("Update") { _, _ ->
                openPlayStore(activity, googleId)
            }
            .setNegativeButton("Later", null)
            .create()
            .show()
    }

    private fun openPlayStore(activity: Activity, googleId: String) {
        try {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$googleId")))
        } catch (e: Exception) {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$googleId")))
        }
    }
}
