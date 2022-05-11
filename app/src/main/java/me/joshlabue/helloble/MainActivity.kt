package me.joshlabue.helloble

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    fun View.showSnackbar(
        view: View,
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this)
            }.show()
        } else {
            snackbar.show()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission())
        {
                isGranted: Boolean ->
            if(isGranted) {
                Log.i("Permission: ", "Granted")
            }
            else {
                Log.i("Permission: ", "Denied");
            }
        }



    fun permCallbackBT(view: View) {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                    == PackageManager.PERMISSION_GRANTED -> {
                view.showSnackbar(view, "Permission granted for Bluetooth", Snackbar.LENGTH_LONG, null) {}
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.BLUETOOTH
            ) -> {
                view.showSnackbar(view, "Permission required",
                    Snackbar.LENGTH_INDEFINITE,
                    "ok"
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
            }
        }

        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
                    == PackageManager.PERMISSION_GRANTED -> {
                view.showSnackbar(view, "Permission granted for Bluetooth Admin", Snackbar.LENGTH_LONG, null) {}
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.BLUETOOTH_ADMIN
            ) -> {
                view.showSnackbar(view, "Permission required",
                    Snackbar.LENGTH_INDEFINITE,
                    "ok"
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)
            }
        }

    }

    fun permCallbackLocation(view: View) {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED -> {
                view.showSnackbar(view, "Permission granted for Location", Snackbar.LENGTH_LONG, null) {}
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                view.showSnackbar(view, "Permission required",
                    Snackbar.LENGTH_INDEFINITE,
                    "ok"
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

    }



}