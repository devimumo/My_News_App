package com.example.mynewsapp.Packages.DashBoard

import android.app.Application
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.example.mynewsapp.Packages.DashBoard.Fragments.ViewModels.AllArticlesViewModel
import com.example.mynewsapp.Packages.DashBoard.Fragments.ViewModels.AllArticlesViewModelFactory
import com.example.mynewsapp.Packages.DashBoard.ui.main.SectionsPagerAdapter
import com.example.mynewsapp.R

class DashBoard : AppCompatActivity() {
    private lateinit var factory: AllArticlesViewModelFactory
    private lateinit var viewmodel: AllArticlesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dash_board)

        factory= AllArticlesViewModelFactory(Application())

        viewmodel = ViewModelProvider(this, factory).get(AllArticlesViewModel::class.java)


           // viewmodel.network_data_fetch("Kenya", applicationContext)


        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

       // network_fetch()
            }


}