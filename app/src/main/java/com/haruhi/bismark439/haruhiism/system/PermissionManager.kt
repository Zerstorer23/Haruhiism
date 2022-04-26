package com.haruhi.bismark439.haruhiism.system

import android.app.Activity
import androidx.core.app.ActivityCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object PermissionManager {
    const val READ_CODE = 1
    fun checkPermission(activity : Activity, vararg requestedPermissions:String , onPermissionChecked:VoidReturn ){
        Dexter.withActivity(activity).withPermissions(
            *requestedPermissions
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                onPermissionChecked()
            }
            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    requestedPermissions,
                    READ_CODE
                )
            }

        }).onSameThread().check()

    }
}