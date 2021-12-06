package com.example.mynewsapp.Packages.DashBoard.Fragments.ViewModels

import androidx.lifecycle.ViewModelProvider
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel

class AllArticlesViewModelFactory(private var application: Application): ViewModelProvider.NewInstanceFactory( ) {

    private var mParam: String? = null


   override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllArticlesViewModel(application) as T
    }


}