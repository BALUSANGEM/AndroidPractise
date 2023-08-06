package com.nosort.composeone

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.nosort.composeone.ui.theme.ComposeOneTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeOneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UrlTextField()
                }
            }
        }
    }
}

@Composable
fun FileReadPermissionScope(
    onRejected : @Composable () -> Unit,
    onGranted : @Composable () -> Unit,
) {
    val context = LocalContext.current
    var result = remember<Boolean?>{ null }
    Log.d("ComposeOne", "FileReadPermissionScope $result")
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        Log.d("ComposeOne", "rememberLauncherForActivityResult $it")
        result = it
    }
    val permission = Manifest.permission.READ_EXTERNAL_STORAGE
    val checkPermission = ContextCompat.checkSelfPermission(context, permission)
    if(checkPermission == PackageManager.PERMISSION_GRANTED) {
        Log.d("ComposeOne", "PackageManager.PERMISSION_GRANTED")
        ShowPermissionedContent(true, onGranted, onRejected)
    } else {
        Log.d("ComposeOne", "PackageManager.PERMISSION_GRANTED NOT")
        SideEffect {
            Log.d("ComposeOne", "PackageManager.PERMISSION_GRANTED NOT SIDE_EFFECT")
            permissionLauncher.launch(permission)
        }
    }
    Log.d("ComposeOne", "Result available $result")
    result?.let {
        Log.d("ComposeOne", "Result not null $result")
        ShowPermissionedContent(it, onGranted, onRejected)
    }
}

@Composable
private fun ShowPermissionedContent(
    granted: Boolean,
    onGranted: @Composable () -> Unit,
    onRejected: @Composable () -> Unit
) {
    if (granted) {
        onGranted()
    } else {
        onRejected()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeOneTheme {
        Greeting("Android")
    }
}