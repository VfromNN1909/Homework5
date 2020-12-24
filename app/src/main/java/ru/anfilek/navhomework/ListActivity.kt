package ru.anfilek.navhomework

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.Manifest.permission.CAMERA as CAMERA_PERMISSION

class ListActivity : AppCompatActivity() {

    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001

    private lateinit var group: RadioGroup

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 12548
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        group = findViewById(R.id.radioGroup)

        findViewById<FloatingActionButton>(R.id.fabStartCamera).setOnClickListener {
            when(group.checkedRadioButtonId){
                // если выбрана активность, то запускаем её
                R.id.rbCameraActivity -> startCameraFeature()
                // иначе запускаем камеру
                R.id.rbOtherCamera -> {
                    val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, IMAGE_CAPTURE_CODE)
                }
            }

        }

        findViewById<Button>(R.id.buttonItem).setOnClickListener {
            startItemActivity()
        }

    }

    private fun startCameraFeature() {
        // check camera permission
        // handle the check result
        // show dialog if it is needed
        // feel free to customise the button if it is needed
        val permission = CAMERA_PERMISSION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            handleCheckResult(
                    permission,
                    checkPermission(permission)
            )
        }
    }

    // ниже мы получаем разрешения
    private fun checkPermission(permission: String): CheckPermissionResult {
        return when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> CheckPermissionResult.GRANTED

            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> CheckPermissionResult.GRANTED

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                    permission
            ) -> CheckPermissionResult.NEED_TO_EXPLAIN
            else -> CheckPermissionResult.NEED_TO_REQUEST

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun handleCheckResult(permission: String, result: CheckPermissionResult) {
        when (result) {
            CheckPermissionResult.GRANTED -> {
                startActivity(Intent(this, CameraActivity::class.java))
            }
            CheckPermissionResult.DENIED -> failedGracefully()
            CheckPermissionResult.NEED_TO_REQUEST -> askForPermission(permission)
            CheckPermissionResult.NEED_TO_EXPLAIN -> showRationale()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showRationale() {
        AlertDialog.Builder(this).apply {
            setTitle("Camera Permission")
            setMessage("Camera permission is needed to allow this future work.")
            setPositiveButton("I understand!") { _, _ -> askForPermission(CAMERA_PERMISSION) }
            show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun askForPermission(permission: String) {
        requestPermissions(arrayOf(permission), LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission was granted", Toast.LENGTH_SHORT)
                        .show()
            } else {
                Toast.makeText(this, "Location permission was not granted", Toast.LENGTH_SHORT)
                        .show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun failedGracefully() {
        AlertDialog.Builder(this).apply {
            setTitle("Camera Permission")
            setMessage("Camera not granted. We respect your decision!")
            setNegativeButton("I understand!") { _, _ -> askForPermission(CAMERA_PERMISSION) }
            setPositiveButton("OK.", null)
            show()
        }
    }

    private fun startItemActivity() {
        startActivity(Intent(this, ItemActivity::class.java))
    }
}

enum class CheckPermissionResult {
    GRANTED,
    DENIED,
    NEED_TO_REQUEST,
    NEED_TO_EXPLAIN
}