package com.example.mynewsapp.Packages.DashBoard.Fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.Packages.DashBoard.Adapter_package.All_sources_adapter
import com.example.mynewsapp.Packages.DashBoard.Data_Classes_package.All_sources_dataclass
import com.example.mynewsapp.R
import org.json.JSONObject
import android.widget.ArrayAdapter
import android.widget.AdapterView
import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.example.mynewsapp.Packages.DashBoard.All_sources_networkfetch
import com.example.mynewsapp.Packages.DashBoard.Volley_ErrorListener_Handler
import org.json.JSONException
import java.util.HashMap


var all_sources_data_set = ArrayList<All_sources_dataclass>()
var category_list =ArrayList<String>()
var news_sources_list = ArrayList<String>()
private   var category: String="All"
private   var news_source_value: String="All"
private lateinit var sources_data :All_sources_dataclass



class All_Sources : Fragment() {
    private lateinit var  filter_button: ImageView
    private lateinit var  un_filter_button: ImageView
    private lateinit var  linearLayout_: LinearLayout
    private lateinit var  filter_layout: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var recycler_adapter: All_sources_adapter
    private  var responsevalue: String=""
private lateinit var  progress_bar: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?
    ): View? {

        category_list= arrayListOf("Click to Choose Category","All")

        news_sources_list= arrayListOf("Click to choose News Source","All")


        val view= inflater.inflate(R.layout.fragment_all__sources, container, false)

        network_fetch(view, "activity")


        recycler_adapter = view?.context?.let { All_sources_adapter(all_sources_data_set, it) }!!

        filter_button=view.findViewById<ImageView>(R.id.filter_button)
        un_filter_button= view.findViewById<ImageView>(R.id.un_filter_button)
        filter_layout=view.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.filter_layout)
        linearLayout_=view.findViewById<LinearLayout>(R.id.linearLayout)
        progress_bar=view.findViewById<LinearLayout>(R.id.progressbar_layout)

        //On click listener to open filter view
        view.findViewById<ImageView>(R.id.filter_button).setOnClickListener {

            view.findViewById<ImageView>(R.id.filter_button).visibility=View.GONE
            view.findViewById<ImageView>(R.id.un_filter_button).visibility=View.VISIBLE

            view.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.filter_layout).visibility=View.VISIBLE
            view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view_allsources).visibility=View.GONE
        }
        //On click listener to close filter view

        view.findViewById<ImageView>(R.id.un_filter_button).setOnClickListener {

            view.findViewById<ImageView>(R.id.filter_button).visibility=View.VISIBLE
            view.findViewById<ImageView>(R.id.un_filter_button).visibility=View.GONE

            view.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.filter_layout).visibility=View.GONE
            view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view_allsources).visibility=View.VISIBLE
        }



        //Spinner for choosing category
        val spinner: Spinner = view.findViewById(R.id.category_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        val dataAdapter: ArrayAdapter<String> =ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, category_list)
         dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner.setAdapter(dataAdapter);

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                category=parent.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        //Spinner for choosing news source
        val news_spinner: Spinner = view.findViewById(R.id.source_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        val news_dataAdapter: ArrayAdapter<String> =ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, news_sources_list)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)

        news_spinner.setAdapter(news_dataAdapter);

        news_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                news_source_value=parent.selectedItem.toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val root_view= inflater.inflate(R.layout.fragment_all__sources, container, false)


        //onclick listener for search button
        view.findViewById<Button>(R.id.button).setOnClickListener {

            progress_bar.visibility = View.VISIBLE
           filter_button.visibility = View.VISIBLE
           un_filter_button .visibility = View.GONE
           filter_layout.visibility = View.GONE

            view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view_allsources).visibility=View.VISIBLE


            search_algorithm(view)
        }

        return view

    }

    private fun network_fetch(view: View, from: String): String
    {
        progress_bar=view.findViewById<LinearLayout>(R.id.progressbar_layout)

        progress_bar.visibility=View.VISIBLE
     all_sources_data_set.clear()

        val context=view.context
        val cache: Cache =  DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network: Network =  BasicNetwork( HurlStack());

        val requestQueue: RequestQueue =  RequestQueue(cache, network);
        requestQueue.start();

        val builder: Uri.Builder = Uri.Builder()
        builder.scheme("https")
            .authority("newsapi.org")
            .appendPath("v2")
            .appendPath("top-headlines")
            .appendPath("sources?")
            .appendQueryParameter("apiKey", "581ecd6a0ec4430fb1cb37f97ff1a26e")
        val myUrl: String = builder.build().toString()

        var url="https://newsapi.org/v2/top-headlines/sources?apiKey=581ecd6a0ec4430fb1cb37f97ff1a26e"

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,{ response ->
            try {
                responsevalue =response.toString()

                Log.d("response_value",response.toString())


                iterate_data(view,responsevalue)

            }catch (e: JSONException)
            {
                responsevalue =e.toString()
                Log.d("JSONEXCEPTION",e.toString())

            }
        },{
            Log.d("volley_error",it.toString())

            var volleyhandler= Volley_ErrorListener_Handler()
            volleyhandler.check_error(it,view)

            responsevalue =it.toString()
            // TODO: Handle error
        }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"]="Mozilla/5.0"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)

// Access the RequestQueue through your singleton class.
//    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT            )
        )


        return responsevalue

    }

    private fun iterate_data(view: View, json_data: String) {

          Log.i("response", json_data.toString())

        if (json_data.equals(""))
        {

        }
        else {

            Log.d("response_state",json_data)

            val jsonObject = JSONObject(json_data)

            val status = jsonObject.getString("status")
            if (status.equals("error")) {

                Toast.makeText(view.context, "Data fetch error", Toast.LENGTH_LONG).show()

            } else {

                all_sources_data_set.clear()
                val data = jsonObject.getJSONArray("sources")

                for (i in 0..data.length() - 1) {

                    val all_sources_data = data.getJSONObject(i)

                    val sources_data = All_sources_dataclass(
                        all_sources_data.getString("id"),
                        all_sources_data.getString("name"),
                        all_sources_data.getString("description"),
                        all_sources_data.getString("url"),
                        all_sources_data.getString("category"),
                        all_sources_data.getString("language"),
                        all_sources_data.getString("country")

                    )

                    extract_categorylist(all_sources_data.getString("category"))
                    extract_news_sourceslist(all_sources_data.getString("name"))

                    all_sources_data_set.add(sources_data)


                    if(i == (data.length()-1)){

    set_to_recycleradapter(view)
}

                }

                Log.d("all_sources_data_set", all_sources_data_set.toString())
            }
        }}


    private fun search_algorithm(view: View)
    {
        all_sources_data_set.clear()
        recycler_adapter.notifyDataSetChanged()


        var network_fetch=All_sources_networkfetch()
        var json_data=network_fetch.network_fetch(view)




           if (json_data.equals(""))
           {}
        else {

               Log.d("jspnn", json_data)


               val jsonObject = JSONObject(json_data.toString())

               val status = jsonObject.getString("status")
               if (status.equals("error")) {

                   Toast.makeText(view.context, "Data fetch error", Toast.LENGTH_LONG).show()

               } else {

                   all_sources_data_set.clear()
                   val data = jsonObject.getJSONArray("sources")

                   for (i in 0..data.length() - 1) {

                       var all_sources_data = data.getJSONObject(i)

                       sources_data = All_sources_dataclass(
                           all_sources_data.getString("id"),
                           all_sources_data.getString("name"),
                           all_sources_data.getString("description"),
                           all_sources_data.getString("url"),
                           all_sources_data.getString("category"),
                           all_sources_data.getString("language"),
                           all_sources_data.getString("country")

                       )

                       var search_keyword_text =
                           view.findViewById<com.google.android.material.textfield.TextInputEditText>(
                               R.id.search_view
                           ).text.toString()


                       if (all_sources_data.getString("name").equals(news_source_value))
                       {
                           if (category.equals("All") || category.equals("Click to Choose Category")) {


                               for_keyword_check(search_keyword_text, sources_data)

                           } else if (all_sources_data.getString("category").equals(category)) {
                               for_keyword_check(search_keyword_text, sources_data)


                           }
                       }
                       else if (news_source_value.equals("All") || category.equals("Click to choose News Source")) {
                           if (all_sources_data.getString("category").equals(category)) {
                               for_keyword_check(search_keyword_text, sources_data)

                           } else if (category.equals("All") || category.equals("Click to Choose Category")) {
                               for_keyword_check(search_keyword_text, sources_data)

                           }
                       }

                       if (i == (data.length() - 1)) {
                           set_to_recycleradapter(view)
                       }
                   }
                   }
               }
           }


    private fun for_keyword_check(search_keyword_text: String,sources_data: All_sources_dataclass)
    {
        if (search_keyword_text.isBlank())
        {
            all_sources_data_set.add(sources_data)
        }
        else
        {
            if (sources_data.toString().contains(search_keyword_text))
            {
                all_sources_data_set.add(sources_data)

            }

        }

    }

    private  fun set_to_recycleradapter(view: View)
    {

        var recycler_view_for_searches = view.findViewById<RecyclerView>(R.id.recycler_view_allsources)

        recycler_view_for_searches.layoutManager = LinearLayoutManager(view.context)

        val adap = All_sources_adapter(all_sources_data_set, view.context)
        Log.d("all_sources_data,", all_sources_data_set.toString())

        progress_bar.visibility = View.GONE

        adap.notifyDataSetChanged()
        recycler_view_for_searches.adapter = adap

    }

    private fun extract_categorylist(category: String)
    {
        if (!category_list.contains(category))
        {
            category_list.add(category)

        }

    }

    private fun extract_news_sourceslist(news_source: String) {
        if (!news_sources_list.contains(news_source)) {
            news_sources_list.add(news_source)

        }


    }

    }

