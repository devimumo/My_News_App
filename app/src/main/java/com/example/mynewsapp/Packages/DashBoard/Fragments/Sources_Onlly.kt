package com.example.mynewsapp.Packages.DashBoard.Fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.example.mynewsapp.Packages.DashBoard.Adapter_package.Sources_Only_Adapter
import com.example.mynewsapp.Packages.DashBoard.All_sources_networkfetch
import com.example.mynewsapp.Packages.DashBoard.Data_Classes_package.All_sources_dataclass
import com.example.mynewsapp.Packages.DashBoard.Volley_ErrorListener_Handler
import com.example.mynewsapp.R
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

private var all_sources_data_setss = ArrayList<All_sources_dataclass>()
private var category_lists =ArrayList<String>()
private   var category: String=""
private lateinit var sources_data : All_sources_dataclass



class Sources_Onlly : Fragment() {
    private lateinit var  filter_button: ImageView
    private lateinit var  un_filter_button: ImageView
    private lateinit var  linearLayout_: LinearLayout
    private lateinit var  filter_layout: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var recycler_adapter: Sources_Only_Adapter
    private  var responsevalue: String=""
    private lateinit var  progress_bar: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?
    ): View? {

        category_lists= arrayListOf("Click to Choose Category","All")



        val view= inflater.inflate(R.layout.fragment_sources__onlly, container, false)
        network_fetch(view,"activity")


        recycler_adapter = view?.context?.let { Sources_Only_Adapter(all_sources_data_setss, it) }!!

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
        val dataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, category_lists)
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
                Log.d("category", category)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        //Spinner for choosing news source
        //onclick listener for search button
        view.findViewById<Button>(R.id.button).setOnClickListener {

            progress_bar.visibility = View.VISIBLE
            filter_button.visibility = View.VISIBLE
            un_filter_button .visibility = View.GONE
            filter_layout.visibility = View.GONE

            view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view_allsources).visibility=View.VISIBLE

            all_sources_data_setss.clear()

            network_fetch(view,"search")
        }

        return view

    }

    private fun network_fetch(view: View,from: String): String
    {
        progress_bar=view.findViewById<LinearLayout>(R.id.progressbar_layout)

        progress_bar.visibility=View.VISIBLE
        all_sources_data_setss.clear()

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

                if (from.equals("search"))
                {
                    search_algorithm(view,responsevalue)
                }
                else
                {
                    iterate_data(view,responsevalue)

                }

                Log.d("response_value",response.toString())

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

                all_sources_data_setss.clear()
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

                    all_sources_data_setss.add(sources_data)


                    if(i == (data.length()-1)){

                        set_to_recycleradapter(view)
                    }

                }

                Log.d("all_sources_data_setss", all_sources_data_setss.toString())
            }
        }}


    private fun search_algorithm(view: View, json_data: String)
    {
        all_sources_data_setss.clear()
        recycler_adapter.notifyDataSetChanged()


        if (json_data.equals(""))
        {


        }
        else {

            Log.d("jspnn", json_data)


            val jsonObject = JSONObject(json_data.toString())

            val status = jsonObject.getString("status")
            if (status.equals("error")) {

                Toast.makeText(view.context, "Data fetch error", Toast.LENGTH_LONG).show()

            } else {

                all_sources_data_setss.clear()

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

                     if (all_sources_data.getString("category").equals(category)) {

                        all_sources_data_setss.add(sources_data)
                    }

                    if (i == (data.length() - 1)) {

                        set_to_recycleradapter(view)
                    }
                }
            }
        }
    }




    private  fun set_to_recycleradapter(view: View)
    {

        Log.d("category", all_sources_data_setss.size.toString())


        var recycler_view_for_searches = view.findViewById<RecyclerView>(R.id.recycler_view_allsources)

        recycler_view_for_searches.layoutManager = LinearLayoutManager(view.context)

        val adap = Sources_Only_Adapter(all_sources_data_setss, view.context)
        Log.d("all_sources_data,", all_sources_data_setss.toString())

        progress_bar.visibility = View.GONE

        adap.notifyDataSetChanged()
        recycler_view_for_searches.adapter = adap

        // progress_bar_layout.visibility = View.GONE
    }

    private fun extract_categorylist(category: String)
    {
        if (!category_lists.contains(category))
        {
            category_lists.add(category)

        }

    }


}
