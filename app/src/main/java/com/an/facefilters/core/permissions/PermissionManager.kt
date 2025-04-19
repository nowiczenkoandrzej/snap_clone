package com.an.facefilters.core.permissions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PermissionManager (
    private val context: Context
) {

    private val _permissionStatusMap = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val permissionStatusMap = _permissionStatusMap.asStateFlow()

    private val _rationaleShownMap = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val rationaleShownMap = _rationaleShownMap.asStateFlow()


    init {

        val permMap = mutableStateMapOf<String, Boolean>()
        val shownMap = mutableStateMapOf<String, Boolean>()

        permissionList.forEach { permission ->
            val isGranted = isPermissionGranted(permission)
            permMap[permission] = isGranted
            shownMap[permission] = false

        }

        _permissionStatusMap.value = permMap
        _rationaleShownMap.value = shownMap
    }



    fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun updatePermissionStatus(permission: String, isGranted: Boolean) {

        val newMap = _permissionStatusMap.value.toMutableMap().apply {
            this[permission] = isGranted
        }.toMap()

        _permissionStatusMap.value = newMap
    }

    fun markRationaleAsShown(permission: String) {
        val newMap = _rationaleShownMap.value.toMutableMap().apply {
            this[permission] = true
        }.toMap()
        _rationaleShownMap.value = newMap
    }

    fun openAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).also { intent ->
            context.startActivity(intent)
        }
    }

    companion object {
        private const val PERMISSION_CAMERA = Manifest.permission.CAMERA
        private const val PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

        val permissionList = listOf(
            PERMISSION_CAMERA,
            PERMISSION_READ_STORAGE,
            PERMISSION_WRITE_STORAGE
        )
    }





}