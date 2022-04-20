package com.haruhi.bismark439.haruhiism.activities.navigation_ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val title: LiveData<String> = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
}