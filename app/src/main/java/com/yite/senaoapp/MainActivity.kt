package com.yite.senaoapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.yite.senaoapp.navigation.AppNavGraph
import com.yite.senaoapp.ui.theme.SenaoInterviewDemoTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SenaoInterviewDemoTheme {
                AppNavGraph()
            }
        }

        checkNotificationPermission()
    }

    // 讓chucker可以正常顯示api回傳資料，非必要權限
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            val permission = "android.permission.POST_NOTIFICATIONS"
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Timber.d("checkSelfPermission PERMISSION GRANTED")
            } else if (shouldShowRequestPermissionRationale(permission)) {
                Timber.d("shouldShowRequestPermissionRationale PERMISSION PERMISSION_DENIED")
            } else {
                val launcher =
                    registerForActivityResult(
                        ActivityResultContracts.RequestPermission(),
                    ) { isGranted ->
                        Timber.d("android.permission.POST_NOTIFICATIONS: $isGranted")
                    }
                launcher.launch(permission)
            }
        }
    }
}
