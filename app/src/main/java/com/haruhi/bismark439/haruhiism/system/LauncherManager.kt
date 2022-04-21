package com.haruhi.bismark439.haruhiism.system


import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

typealias ActivityResFunc = (res: ActivityResult) -> Unit

enum class LauncherType {
    CreateAlarm, SelectPath
}

object LauncherManager {
    private var dictionary = HashMap<LauncherType, ActivityResultLauncher<Intent>>()

    fun init(activity: AppCompatActivity, type: LauncherType, onResult: ActivityResFunc) {
        dictionary[type] =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                if (it.resultCode == Activity.RESULT_OK) {
                    onResult(it)
                }
            }
    }

    fun init(
        registerForActivityResult: (ActivityResultContracts.StartActivityForResult,
                                    f: (it:ActivityResult)->Unit)-> ActivityResultLauncher<Intent>,
        type: LauncherType,
        onResult: ActivityResFunc) : ActivityResultLauncher<Intent>{
        dictionary[type] =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                if (it.resultCode == Activity.RESULT_OK) {
                    onResult(it)
                }
            }
        return dictionary[type]!!
    }
    fun launch(type: LauncherType, intent: Intent) {
        if (dictionary.containsKey(type)) {
            dictionary[type]?.launch(intent)
        }
    }

}