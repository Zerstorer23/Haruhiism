package com.haruhi.bismark439.haruhiism.activities.interfaces

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.haruhi.bismark439.haruhiism.MyApp
import com.haruhi.bismark439.haruhiism.system.ActivityResFunc


abstract class IFragmentActivity<T : ViewBinding>(
    private val inflater: FragmentInflater<T>
) : Fragment() {
    protected lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onActivityStart()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MyApp.setContext(requireContext())
        binding = inflater(inflater, container, false)
        onRefreshUI()
        return binding.root
    }

    abstract fun onRefreshUI()
    abstract fun onActivityStart()

    protected fun initActivityLauncher(onResult:ActivityResFunc): ActivityResultLauncher<Intent> {
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (it.resultCode == Activity.RESULT_OK) {
                onResult(it)
            }
        }
        return launcher
    }
}

typealias FragmentInflater<T> = (lf: LayoutInflater, vg: ViewGroup?, b: Boolean) -> T
