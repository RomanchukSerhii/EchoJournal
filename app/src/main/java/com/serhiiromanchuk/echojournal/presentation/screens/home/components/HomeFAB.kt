package com.serhiiromanchuk.echojournal.presentation.screens.home.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.serhiiromanchuk.echojournal.presentation.core.utils.GradientScheme

@Composable
fun HomeFAB(
    onResult: (isGranted: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val activity = context as Activity

    var isPermissionDialogOpened by remember { mutableStateOf(false) }

    val recordAudioPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            isPermissionDialogOpened = !isGranted
            onResult(isGranted)
        }
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()

    Surface(
        onClick = {
            recordAudioPermissionResultLauncher.launch(Manifest.permission.RECORD_AUDIO)
        },
        modifier = modifier.semantics { role = Role.Button },
        shape = CircleShape,
        shadowElevation = if (isPressed.value) 7.dp else 6.dp,
        interactionSource = interactionSource
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(
                    minWidth = 56.dp,
                    minHeight = 56.dp,
                )
                .clip(CircleShape)
                .background(
                    brush = GradientScheme.PrimaryGradient,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    if (isPermissionDialogOpened) {
        PermissionDialog(
            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.RECORD_AUDIO
            ),
            onDismiss = { isPermissionDialogOpened = false },
            onOkClick = {
                isPermissionDialogOpened = false
                recordAudioPermissionResultLauncher.launch(Manifest.permission.RECORD_AUDIO)
            },
            onGoToAppSettingsClick = {
                isPermissionDialogOpened = false
                activity.openAppSettings()
            }
        )
    }
}

private fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}