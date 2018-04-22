package com.hisab.kheti.ui

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.hisab.kheti.R
import com.hisab.kheti.utils.addFragment
import com.hisab.kheti.utils.addFragmentWithBackstack
import com.hisab.kheti.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolBar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.nav_crop)
        loadCropFragment()
    }

    private fun loadCropFragment() {
        replaceFragment(CropListFragment(), R.id.fragment_container)
    }

    private fun loadCategoryFragment() {
        replaceFragment(CategoryListFragment(), R.id.fragment_container)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_crop -> {
                loadCropFragment()
            }
            R.id.nav_categories -> {
                loadCategoryFragment()
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
