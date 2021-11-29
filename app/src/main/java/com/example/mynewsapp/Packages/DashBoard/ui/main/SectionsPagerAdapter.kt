package com.example.mynewsapp.Packages.DashBoard.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mynewsapp.Packages.DashBoard.Fragments.All_Sources
import com.example.mynewsapp.Packages.DashBoard.Fragments.All_articles
import com.example.mynewsapp.Packages.DashBoard.Fragments.Sources_Onlly
import com.example.mynewsapp.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3

)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when(position){

            0->{
                return All_articles()
            }
            1->{

                return All_Sources()

            }
            2->{

                return Sources_Onlly()

            }
            else->return Sources_Onlly()

        }
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 3
    }
}