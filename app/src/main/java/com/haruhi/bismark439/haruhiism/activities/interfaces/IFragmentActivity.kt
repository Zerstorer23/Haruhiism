package com.haruhi.bismark439.haruhiism.activities.interfaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.haruhi.bismark439.haruhiism.MyApp

typealias FragmentInflater<T> = (lf: LayoutInflater, vg: ViewGroup?, b: Boolean) -> T

abstract class IFragmentActivity<T : ViewBinding, U : ViewModel>(
    private val inflater: FragmentInflater<T>,
    private val myClass: Class<U>
) : Fragment() {
    protected lateinit var binding: T
    protected lateinit var viewModel: U
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
        viewModel = ViewModelProvider(this)[myClass]
        binding = inflater(inflater, container, false)
        onRefreshUI()
        return binding.root
    }

    abstract fun onRefreshUI()
    abstract fun onActivityStart()

}