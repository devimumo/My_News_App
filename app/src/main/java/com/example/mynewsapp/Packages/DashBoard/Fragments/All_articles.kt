package com.example.mynewsapp.Packages.DashBoard.Fragments

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.Packages.DashBoard.Adapter_package.All_articles_adapter
import com.example.mynewsapp.Packages.DashBoard.All_articles_networkfetch
import com.example.mynewsapp.Packages.DashBoard.Data_Classes_package.All_articles_dataclass
import com.example.mynewsapp.Packages.DashBoard.Fragments.ViewModels.AllArticlesViewModel
import com.example.mynewsapp.Packages.DashBoard.Fragments.ViewModels.AllArticlesViewModelFactory
import com.example.mynewsapp.Packages.DashBoard.Volley_ErrorListener_Handler
import com.example.mynewsapp.R
import org.json.JSONObject
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar

var all_articles_data_sets = ArrayList<All_articles_dataclass>()

var all_articles_data_set = ArrayList<All_articles_dataclass>()
private lateinit var  fetch_data: All_articles_networkfetch

private lateinit var recycler_view_value: RecyclerView
private lateinit var searchView: SearchView
private lateinit var linearLayout2: androidx.constraintlayout.widget.ConstraintLayout
 private lateinit var  adap: All_articles_adapter

private  var responsevalue: String=""
private lateinit var  adaps: All_articles_adapter



class All_articles : Fragment() {

    private lateinit var factory:AllArticlesViewModelFactory
    private lateinit var viewmodel:AllArticlesViewModel

    private var keyword_value="Kenya"
    private lateinit var  progress_bar: LinearLayout

    private lateinit var json_data_fromviewmodel: String
    private lateinit var volley_response: String

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_all_articles, container, false)
         adap = All_articles_adapter(all_articles_data_set, view.context)

        recycler_view_value = view.findViewById<RecyclerView>(R.id.recycler_view)
        linearLayout2 = view.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.constraintLayout2)
        progress_bar = view.findViewById<LinearLayout>(R.id.progressbar_layout)

        progress_bar.visibility=View.VISIBLE

        factory= AllArticlesViewModelFactory(Application())

        viewmodel = ViewModelProvider(this, factory).get(AllArticlesViewModel::class.java)


        viewmodel.network_data_fetch("kenya",this.requireContext())



        val network_fetch_status_observer = Observer<String> { data ->
            // Update the UI, in this case, a TextView.
            //  iterate_data(view,data.toString())

                 check_networkfetchstatus(data)
        }

        val array_observer = Observer<ArrayList<All_articles_dataclass>> { data ->
            // Update the UI, in this case, a TextView.
            //  iterate_data(view,data.toString())

            all_articles_data_set=data

         //   set_to_adapter(view)
        }

        viewmodel.get_arraylist_data().observe(this,array_observer)

        viewmodel.volley_response.observe(this,network_fetch_status_observer)




Log.d("Lifecycle_value",getLifecycle().getCurrentState().toString())
//iterate_data(view, viewmodel.json_data_value.toString())




      //  network_fetch(view,keyword_value)

        var scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recycler_view_value, newState)
                val totalItemCount = recyclerView!!.layoutManager?.itemCount


                when(newState)
                {
                    RecyclerView.SCROLL_STATE_IDLE->{
                        linearLayout2.visibility=View.VISIBLE

                    }
                    RecyclerView.SCROLL_STATE_DRAGGING->{
                        linearLayout2.visibility=View.GONE

                    }
                    RecyclerView.SCROLL_STATE_SETTLING->{
                        linearLayout2.visibility=View.GONE

                    }

                }

            }

        }
        recycler_view_value.addOnScrollListener(scrollListener)

        val searchManager= view.context.getSystemService(Context.SEARCH_SERVICE)
         searchView = view.findViewById<SearchView>(R.id.searchview_value)

      //  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {


                viewmodel.fetch_actual(query,view.context)

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                 //   recycler_view.visibility = View.GONE
                } else {

                   // all_articles_data_set.clear()
                  //  adap.notifyDataSetChanged()
                  //  iterate_data(view,newText)

                }

                return false
            }
        })

      //  fetch_data= All_articles_networkfetch()
      //  fetch_data.network_fetch(view,keyword_value)


        return view

    }

    private fun check_networkfetchstatus(data: String)
    {
        when (data)
        {
            "successful"->{
                if (data.isEmpty())
                {
                    progress_bar.visibility=View.GONE
                    snack_bar("Failed to Load data with said parameters\nKindly try again",requireView())

                }else
                {
                    set_to_adapter(requireView())

                }

            }
            "JSONException"->{

                snack_bar("Failed to Load data.\nKindly try again",requireView())
                progress_bar.visibility=View.GONE


            }
            "volley_error"->{
                viewmodel.volley_error_value.observe(viewLifecycleOwner, Observer { error_value->

                    var volley_error_handler=Volley_ErrorListener_Handler()
                    volley_error_handler.check_error(error_value,requireView())
                })

                progress_bar.visibility=View.GONE

            }
            else->{
                snack_bar("Failed to Load data.\nKindly try again",requireView())

                progress_bar.visibility=View.GONE

            }
        }

    }


    private fun snack_bar(error: String?, view: View) {
        val mysnackbar = Snackbar.make(view, error!!, Snackbar.LENGTH_LONG)
        mysnackbar.show()
    }

    private fun set_to_adapter(view: View)
    {
        var recycler_view = view.findViewById<RecyclerView>(R.id.recycler_view)

        recycler_view.layoutManager = LinearLayoutManager(view.context)

        adap = All_articles_adapter(all_articles_data_set, view.context)
        adap.notifyDataSetChanged()
        recycler_view.adapter = adap

        progress_bar.visibility=View.GONE
    }

    override fun onResume() {
        super.onResume()




    }

}