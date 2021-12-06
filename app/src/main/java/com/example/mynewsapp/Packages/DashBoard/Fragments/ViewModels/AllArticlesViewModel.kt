package com.example.mynewsapp.Packages.DashBoard.Fragments.ViewModels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.*
import com.android.volley.toolbox.*
import com.example.mynewsapp.Packages.DashBoard.Data_Classes_package.All_articles_dataclass
import com.example.mynewsapp.Packages.DashBoard.Fragments.all_articles_data_set
import com.example.mynewsapp.Packages.DashBoard.Volley_ErrorListener_Handler
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap



class AllArticlesViewModel(application: Application) : AndroidViewModel( application) {

    private var all_articles_data_sets = ArrayList<All_articles_dataclass>()

      private var responsevalue: String=""
      private var volley_status: String="default"

    val all_articles_data_set = ArrayList<All_articles_dataclass>()

    val json_data: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val volley_response= MutableLiveData<String>()
    val volley_error= MutableLiveData<VolleyError>()
    val DATA_ARRAY= MutableLiveData <ArrayList<All_articles_dataclass>>()


    val volley_error_value: LiveData<VolleyError>
        get() {
            volley_error
            return volley_error
        }


    fun get_arraylist_data(): LiveData<ArrayList<All_articles_dataclass>> =DATA_ARRAY



     fun network_data_fetch (keyword: String,context: Context){
         if (volley_response.value=="successful")
         {

         }else
         {
             fetch_actual (keyword,context)

         }
     }


    fun fetch_actual (keyword: String,context: Context): String
    {


        //   Log.d("keywords_jsondata :",keyword.toString())

      //  val cache: Cache =  DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
      //  val network: Network =  BasicNetwork( HurlStack());

       // val requestQueue: RequestQueue =  RequestQueue(cache, network);
       // requestQueue.start();

        val requestQueue: RequestQueue =  Volley.newRequestQueue(context)

        val builder: Uri.Builder = Uri.Builder()
        builder.scheme("https")
            .authority("newsapi.org")
            .appendPath("v2")
            .appendPath("everything")
            .appendQueryParameter("q", keyword)
            .appendQueryParameter("apiKey", "581ecd6a0ec4430fb1cb37f97ff1a26e")
        val myUrl: String = builder.build().toString()


        val jsonObjectRequest = object :
            JsonObjectRequest(Request.Method.GET, myUrl, null,{ response ->
                try {

                    volley_response.value="successful"

                    responsevalue =response.toString()

                    iterate_data(responsevalue)

                }catch (e: JSONException)
                {
                    volley_status="JSONException"

                    volley_response.value=volley_status

                    responsevalue =e.toString()
                    json_data.value=responsevalue

                    Log.d("JSONEXCEPTION",e.toString())
                }
                //textView.text = "Response: %s".format(response.toString())
            },{
                volley_status="volley_error"

                Log.d("volley_error",it.toString())

               // var volleyhandler= Volley_ErrorListener_Handler()
             //   volleyhandler.check_error(it,view)
                volley_response.value=volley_status

                responsevalue =it.toString()
                json_data.value=responsevalue

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

        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT            )
        )

        return responsevalue

    }

    private fun iterate_data(data_from_api: String) {

        all_articles_data_set.clear()

        val json_data =data_from_api

        if (json_data.equals("error")) {

            Log.d("Volley_error :",json_data)

        } else if (json_data.equals("volley_error")) {
            Log.d("Volley_error :",json_data)

        }
        else if (json_data.equals("")) {
            Log.d("Volley_error :",json_data)

        }else {

            val jsonObject = JSONObject(json_data)

            val status = jsonObject.getString("status")
            if (status.equals("error")) {

            } else {

                val data = jsonObject.getJSONArray("articles")

                for (i in 0..data.length() - 1) {

                    val all_articles_data = data.getJSONObject(i)


                    val source_data = all_articles_data.getJSONObject("source")
                    val source_name = source_data.getString("name")
                    val source_id = source_data.getString("id")

                    val articles_data = All_articles_dataclass(
                        source_id,
                        source_name,
                        all_articles_data.getString("author"),
                        all_articles_data.getString("title"),
                        all_articles_data.getString("description"),
                        all_articles_data.getString("url"),
                        all_articles_data.getString("urlToImage"),
                        all_articles_data.getString("publishedAt"),
                        all_articles_data.getString("content")

                    )

                    all_articles_data_sets.add(articles_data)



                    if(i == (data.length()-1)){

                        DATA_ARRAY.value=all_articles_data_sets

                    }

                }





            }
        }
    }





}