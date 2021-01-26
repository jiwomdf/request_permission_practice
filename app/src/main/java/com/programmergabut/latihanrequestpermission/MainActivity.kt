package com.programmergabut.latihanrequestpermission

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    private val arrPermission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val LOCATION_PERMISSIONS = 123

    private fun isLocationPermissionGranted() :Boolean {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun shouldShowRequestPermissionRationale() : Boolean {
        return (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!isLocationPermissionGranted()){
            ActivityCompat.requestPermissions(this@MainActivity, arrPermission, LOCATION_PERMISSIONS)
        } else {
            //Do Action
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            123 -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //Do Action
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                } else { //Denied
                    if (shouldShowRequestPermissionRationale()) {
                        //Show why we needed to accept location (educate stubborn user)
                        showPermissionDialog()
                    } else {
                        // user has check never show again, now we need to do something
                        // We can't do this ActivityCompat.requestPermissions(this@MainActivity, arrPermission, LOCATION_PERMISSIONS)
                        goToSettings()
                    }
                }
                return
            }
        }
    }

    private fun goToSettings() {
        AlertDialog.Builder(this)
            .setTitle("Please change the app permission")
            .setMessage("access location needed to run application, please change it in app setting")
            .setCancelable(false)
            .setPositiveButton("Oke") { _: DialogInterface, _: Int ->
                val myAppSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
                myAppSettings.apply {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivityForResult(this, LOCATION_PERMISSIONS)
                }
            }
            .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showPermissionDialog(){
        AlertDialog.Builder(this)
            .setTitle("Access location needed")
            .setMessage("access location needed to run application")
            .setCancelable(false)
            .setPositiveButton("Oke") { _: DialogInterface, _: Int ->
                ActivityCompat.requestPermissions(this, arrPermission, LOCATION_PERMISSIONS)
            }
            .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun intentToLocationSetting(){
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

}