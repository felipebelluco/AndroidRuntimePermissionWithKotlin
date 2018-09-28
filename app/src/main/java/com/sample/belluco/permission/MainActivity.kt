package com.sample.belluco.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val thisActivity = this

    private val CALL_PHONE_RESULT_CODE = 111

    // Altere o valor da constante para um número válido
    // e.g. +5561999999999
    private val PHONE_NUMBER = "PHONE_NUMBER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun makeCall(view: View) {
        if (ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                createMoreInfoDialog()
            } else {
                requestCallPermission()
            }
        } else {
            makeCall(PHONE_NUMBER)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CALL_PHONE_RESULT_CODE -> {
                if (grantResults.isNotEmpty()
                        && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    makeCall(PHONE_NUMBER)
                } else if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    createNeverAskAgainDialog()
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createNeverAskAgainDialog() {
        AlertDialog.Builder(thisActivity).apply {
            setMessage(R.string.never_ask_again_dialog)
            setTitle(R.string.permission_dialog_title)
            setPositiveButton(R.string.go_to_settings) { _, _ -> goToAppDetailsSettings() }
            setNegativeButton(R.string.no) { d, _ -> d.dismiss() }
        }.show()
    }

    private fun createMoreInfoDialog() {
        AlertDialog.Builder(thisActivity).apply {
            setMessage(R.string.more_info_dialog)
            setTitle(R.string.permission_dialog_title)
            setPositiveButton(R.string.yes) { _, _ -> requestCallPermission() }
            setNegativeButton(R.string.no) { d, _ -> d.dismiss() }
        }.show()
    }

    private fun requestCallPermission() {
        ActivityCompat.requestPermissions(
                thisActivity,
                arrayOf(Manifest.permission.CALL_PHONE),
                CALL_PHONE_RESULT_CODE)
    }

    private fun goToAppDetailsSettings() {
        val appSettings = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", thisActivity.packageName, null)
        }
        startActivity(appSettings)
    }

    private fun makeCall(number: String) {
        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(callIntent)
    }

}
