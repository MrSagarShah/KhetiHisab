package com.hisab.kheti.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hisab.kheti.R
import com.hisab.kheti.utils.addFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadCropFragment()
    }

    private fun loadCropFragment() {
        addFragment(CropListFragment(), R.id.fragment_container)
    }
}
