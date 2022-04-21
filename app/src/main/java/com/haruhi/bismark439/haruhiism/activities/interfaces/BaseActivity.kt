package com.haruhi.bismark439.haruhiism.activities.interfaces

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.haruhi.bismark439.haruhiism.MyApp
import com.haruhi.bismark439.haruhiism.system.Sleeper
import com.haruhi.bismark439.haruhiism.system.ui.Toaster



open class BaseActivity<T : ViewBinding>(
    private val inflater: ActivityViewBinder<T>
) : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    protected lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        MyApp.setContext(this)
        super.onCreate(savedInstanceState)
        binding = inflater(layoutInflater)
        setContentView(binding.root)
        //ScreenManager.hideStatusBar(window)
    }

    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        } else {
            doubleBackToExitPressedOnce = true
            Toaster.show(this, "Back pressed once")
            Sleeper.waitAndExecute(1000) {
                doubleBackToExitPressedOnce = false
            }
        }
    }

    fun showErrorSnackBar(message: String) {
        Toaster.showSnackBar(this, message)
    }

    fun toast(msg: String) {
        Toaster.show(this, msg)
    }

}

typealias ActivityViewBinder<T> = (lf: LayoutInflater) -> T